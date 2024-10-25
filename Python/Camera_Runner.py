import subprocess

try:
    while True:
        # Construct the command
        cmd = ["rpicam-vid", "-t", "10000", "--codec", "h264", "--inline" , "--segment", "1", "-o", "test%05d.h264"]

        # Run the command and check the result
        camera_process = subprocess.Popen(cmd, shell=False)
        camera_process.wait()
except Exception as ex:
    print(f"Camera shutting down: {ex}")