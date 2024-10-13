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

def wait_for_remote_ip():
    server_port = 13000 #Our port
    # Create a socket object
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = "0"
    # Bind the socket to the address and port
    server_socket.bind(("0.0.0.0", server_port))
    print(f"Server listening on {server_address}:{server_port}")
    
    waitingOnIPAddr = True
    while waitingOnIPAddr:
        # Listen for incoming connections (we only need one connection from the phone app, so 1 backlog)
        server_socket.listen(1)
        # Accept a connection
        client_socket, client_address = server_socket.accept()
        print(f"Connection from {client_address}")
        
        try:
            # Receive data from the client
            data = client_socket.recv(32)
            if data:
                print(f"Received: {data.decode()}")
                server_address = client_address
                try:
                    #If this doesn't fail, its valid
                    ipaddress.ip_address(server_address)
                    # legal
                    print('Received valid IP Address- %s' % (server_address))
                    waitingOnIPAddr = False #We got it
                    # Send a response back to the client letting them know we've received their message
                    response = "ack" #Acknowledged rx
                    client_socket.sendall(response.encode())
                    print(f"Sent: {response}")
                except socket.error:
                    # Not legal - try again!
                    print('ERROR: Received invalid IP Address- %s' % (server_address))

        
        except Exception as e:
            print(f"Error: {e}")
        
        finally:
            # Close the client connection
            client_socket.close()
            print(f"Connection with {client_address} closed.")
            return server_address
    
def send_frame(client_socket, server_address, data):
    byteMessage = bytearray()
    byteMessage.extend(map(ord, data)) #Transform string into byte array
    print(f'Sending: {byteMessage}')
    client_socket.sendto(byteMessage, server_address) 

# Flag to signal the main thread to exit
exit_flag = False

def run_camera():
    global exit_flag  # Declare exit_flag as global to modify it
    while(not exit_flag):
        #Retrieve camera data
        #Execute bash script to write raw data to a file
        #Write camera data to file consumer.txt
        if os.system("rpicam-raw -t 2000 --segment 1 -o test%05d.raw") != 0:
            exit_flag = True

# Collect all data from .raw files

def collect_raw_data_from_directory(client_socket, server_address):
    # Get the current working directory
    directory_path = os.getcwd()

    # Iterate over all files in the directory
    for filename in os.listdir(directory_path):
        # Check if the file has a .raw extension
        if filename.endswith(".raw"):
            file_path = os.path.join(directory_path, filename)

            # Open and read the .raw file
            with open(file_path, 'r') as file:
                file_data = file.read()
                send_frame(client_socket, server_address, file_data)
            # Delete the file after reading its contents
            os.remove(file_path)
            print(f"Deleted file: {filename}")

            print(f"Collected data from: {filename}")


def run_client(server_address):
    # Create a socket object
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    
    try: 
        time.sleep(1) #Sleep for a bit to let some frames accumulate
        while(not exit_flag):
            collect_raw_data_from_directory(client_socket, server_address)
            time.sleep(.2)
    
    except Exception as e:
        print(f"Error: {e}")
    
    finally:
        # Close the connection
        client_socket.close()
        print("Connection closed.")

if __name__ == "__main__":
    # Create this camera thread to run the whole duration of the program
    producer_thread = threading.Thread(target=run_camera)
    producer_thread.start()

    while not exit_flag:
        server_address = wait_for_remote_ip()
        run_client(server_address)
        time.sleep(2) 

    # Wait for the producer thread to finish before exiting
    producer_thread.join()
    print("Main program exiting.")