import socket
import subprocess
import os
import datetime
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

from picamera2 import Picamera2
from picamera2.encoders import H264Encoder
from picamera2.outputs import FfmpegOutput

picam2 = Picamera2()
video_config = picam2.create_video_configuration()
picam2.configure(video_config)






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
            encoder = H264Encoder(10000000)
            output = FfmpegOutput(f'-f mpegts udp://{app_address}:{UDP_PORT}', audio=True)

            last_frame_time = time.time()

            def frame_callback(frame):
                global last_frame_time
                last_frame_time = time.time()
                # Process the frame as needed

            picam2.request_callback = frame_callback
            picam2.start_recording(encoder, output)

            # Check for frame activity
            while True:
                if time.time() - last_frame_time > 5:  # 5 seconds threshold
                    print("Camera has failed or stopped streaming.")
                    break  # Optionally, reinitialize the camera or take other action
            
    except Exception as ex:
        print(f'Error: {ex}')
    finally:
        # Wait for the producer thread to finish before exiting
        print("Main program exiting.")
        app_socket.close()
        picam2.stop_recording()


#####################################################################
# Where all the code above gets run
if __name__ == "__main__":
    udp_state_machine()


#State machine:
# LISTEN
# get IP address of remote app
# SEND
# Send video footage over tcp network


