import socket
import ipaddress
import threading
import time
import os

#The basic functionality of PyConnect is this:
#Some remote server sends us a message.
#We can garner the IP address from their message to us, 
#and use this to send them camera data.
#We send them an 'ack', which 'ack'nowledges that we've received the message,
#And then we connect to this remote server using the ip they sent us.

# Flag to signal the main thread to exit
exit_flag = False
#Signals the camera sender to quit sending data (we lost heartbeat)
connection_lost_flag = True

#Ports
TCP_PORT = 13002
UDP_PORT = 13003


#####################################################################
# wait_for_remote_ip
# Waits for the phone app to connect to us before we continue with heartbeats
# and udp data.
def get_app_data():
    # Create a socket object
    pi_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    our_address = "0.0.0.0"
    # Bind the socket to the address and port
    pi_socket.bind((our_address, TCP_PORT))
    
    waitingOnIPAddr = True
    while waitingOnIPAddr:
        # Listen for incoming connections (we only need one connection from the phone app, so 1 backlog)
        pi_socket.listen(1)
        # Accept a connection
        print(f"Server listening on {our_address}:{TCP_PORT}")
        app_socket, app_address = pi_socket.accept()
        print(f"Connection from {app_address}")
        
        try:
            # Receive data from the client
            data = app_socket.recv(32)
            if data:
                print(f"Received: {data.decode()}")
                try:
                    #If this doesn't fail, its valid
                    ipaddress.ip_address(app_address[0])
                    # legal
                    print('Received valid IP Address- %s' % (app_address[0]))
                    waitingOnIPAddr = False #We got it
                    # Send a response back to the client letting them know we've received their message
                    response = "ack" #Acknowledged rx
                    app_socket.sendall(response.encode())
                    return app_socket, app_address
                except socket.error:
                    # Not legal - try again!
                    print('ERROR: Received invalid IP Address- %s' % (app_address))

        
        except Exception as e:
            print(f"Error: {e}")

def run_camera_thread(app_address):
    camera_thread = threading.Thread(target=run_camera, args=(app_address, UDP_PORT))
    camera_thread.start()

def run_heartbeat_thread(app_socket):
    heartbeat_thread = threading.Thread(target=heartbeat, args=(app_socket))
    heartbeat_thread.start()
    
def state_machine():
    global exit_flag
    global connection_lost_flag
    app_socket = socket.socket
    try:
        while not exit_flag:
            app_socket, app_address_tuple = get_app_data()
            # Create this camera thread to run the whole duration of the program
            run_heartbeat_thread(app_socket)
            run_camera_thread(app_address_tuple[0])
            while not connection_lost_flag:
                time.sleep(.5) #Cycle spinning

            app_socket.close()
    except KeyboardInterrupt as ki:
        exit_flag = True
        connection_lost_flag = True
    finally:
        # Wait for the producer thread to finish before exiting
        print("Main program exiting.")
        exit_flag = True
        connection_lost_flag = True
        app_socket.close()



#####################################################################
# run_camera
# A thread created to run over the lifecycle of the program, 
# it collects camera footage and saves it to the current working directory. 
def run_camera(app_address, port):
    global exit_flag  # Declare exit_flag as global to modify it
    try:
        while(not exit_flag):
            #Retrieve camera data
            #Execute bash script to write raw data to a file
            #Write camera data to file consumer.txt
            if os.system("rpicam-vid -t 0 --inline -o udp://"+str(app_address)+":" + str(port)) != 0:
                exit_flag = True
    except KeyboardInterrupt as ki:
        exit_flag = True




#####################################################################
# heartbeat
# Thread created to ensure that we stay connected with the main app.
# If we lose connection, we need to stop sending data, 
# and then attempt to reconnect.
def heartbeat(app_socket):
    global connection_lost_flag
    
    try:
        
        # Set a timeout on the client socket to handle lost heartbeats
        app_socket.settimeout(20)  # Timeout in seconds
        
        while not connection_lost_flag:
            try:
                # Receive data from the client
                data = app_socket.recv(32)
                if data:
                    print("Received heartbeat")

            except socket.timeout:
                connection_lost_flag = True
                # Handle timeout if no heartbeat is received in time
                print("Connection timed out. No heartbeat received.")

            except Exception as e:
                print(f"Error: {e}")
                connection_lost_flag = True
    except KeyboardInterrupt as ki:
        connection_lost_flag = True
    finally:
        connection_lost_flag = True
        # Close the client socket
        app_socket.close()
        print(f"Heartbeat lost.")
        
        # Close the server socket
        app_socket.close()
        print("Heartbeat socket closed.")


#####################################################################
# Where all the code above gets run
if __name__ == "__main__":
    state_machine()


#State machine:
# LISTEN
# get IP address of remote app
# SEND
# Send video footage over tcp network


