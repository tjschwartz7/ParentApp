import subprocess
import time

cmd = ["python", "Camera_Runner.py"]
camera_runner = subprocess.Popen(cmd, shell=False)
cmd = ["python", "PyConnect.py"]
pyconnect = subprocess.Popen(cmd, shell=False)

try:
    while True:
        time.sleep(1)
except KeyboardInterrupt as ki:
    camera_runner.terminate()
    pyconnect.terminate()
    camera_runner.wait()
    pyconnect.wait()
finally:
    print("Thank you for enjoying PyExec")