import socket
import subprocess
import os
import time

#The basic functionality of PyConnect is this:
#Some remote server sends us a message.
#We can garner the IP address from their message to us, 
#and use this to send them camera data.
#We send them an 'ack', which 'ack'nowledges that we've received the message,
#And then we connect to this remote server using the ip they sent us.

#Ports
TCP_PORT = 13002
UDP_PORT = 13003




#####################################################################
# wait_for_remote_ip
# Waits for the phone app to connect to us before we continue with heartbeats
# and udp data.
def udp_state_machine():

    try:
        while True:
            # Create a socket object
            pi_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # Set the SO_REUSEADDR option to allow the address to be reused
            pi_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            our_address = "0.0.0.0"
            # Bind the socket to the address and port
            pi_socket.bind((our_address, TCP_PORT))
            
            # Listen for incoming connections (we only need one connection from the phone app, so no backlog needed)
            pi_socket.listen(0)
            # Accept a connection
            print(f"Server listening on {our_address}:{TCP_PORT}")
            #App socket represents our connection to the app (duh)
            app_socket, app_address_tuple = pi_socket.accept()
            pi_socket.close()
            print(f"Connection from {app_address_tuple[0]}")

            # Set the SO_REUSEADDR option to allow the address to be reused
            app_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            app_address = app_address_tuple[0]


            #Assume we're currently connected (at this point we are)
            connection_lost_flag = False
            
            #Manage the heartbeat
            # Set a timeout on the client socket to handle lost heartbeats
            app_socket.settimeout(20)  # Timeout in seconds
            
            while not connection_lost_flag:
                try:
                    # Specify the directory containing the .h264 files
                    print("Begin file reading")
                    directory = os.getcwd()

                    # Iterate through all files in the directory
                    for filename in os.listdir(directory):
                        # Check if the file ends with .jpeg
                        if filename.endswith(".h264"):
                            file_path = os.path.join(directory, filename)
                            try:
                                print(app_address)
                                
                                cmd = ["ffmpeg","-i", f"{file_path}", "-vcodec", "copy",  f"udp://{app_address}:{UDP_PORT}?output.mkv"]
                                #cmd = ["ffmpeg", "-i", f"{file_path}", "-flags", "-global_header", "-vcodec", "libx264", "-map", "0", "-f", "mpegts", f"udp://{app_address}:{UDP_PORT}"]

                                ffmpeg_process = subprocess.Popen(cmd, shell=False)
                                ffmpeg_process.wait()
                                # Delete the file
                                os.remove(file_path)
                                print(f"Deleted {file_path}")
                            except Exception as e:
                                print(f"Exception occurred with file {file_path}: {e}")

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

            print("Lost connection. Closing app socket.")
            app_socket.close()
    except Exception as ex:
        print(f'Error: {ex}')
    finally:
        # Wait for the producer thread to finish before exiting
        print("Main program exiting.")
        app_socket.close()



#####################################################################
# Where all the code above gets run
if __name__ == "__main__":
    udp_state_machine()


#State machine:
# LISTEN
# get IP address of remote app
# SEND
# Send video footage over tcp network


