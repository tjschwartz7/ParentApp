import subprocess

try:
    while True:
        # Construct the command
        cmd = ["rpicam-vid", "-t", "0",  "--inline", "--listen", "-o", "tcp://0.0.0.0:13003"]

        # Run the command and check the result
        camera_process = subprocess.Popen(cmd, shell=False)
        camera_process.wait()
except Exception as ex:
    print(f"Camera shutting down: {ex}")