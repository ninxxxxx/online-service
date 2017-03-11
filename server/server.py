
# Echo server program

import sys
import socket
import json
import requests
import fabric.exceptions
from fabric.api import *
from fabric.contrib.files import append
from fabric.operations import put, get

from os import listdir


test_addr = ["192.168.6.49:38000", "root", "d"]
addr = "127.0.0.1"

return_codes = {
	0: "DONE",
	1: "Code Error",
	3: "EQUAL",
	5: "NOT_EQUAL",
	66: "Connected, Recieved script's list",
	127: "Something Error",
	555: "Failed to connect: Incorrect Port or The box is reboot",
	999: "Failed to connect: username or password incorrect"
}

setting = {
	"username": "root",
	"password": "dd",
}


env.abort_on_prompts = True


def checkAuthen(username, password):
	if username in setting.values() and password in setting.values():
		print "Yes, you're here"
		return True
	else:
		print "No, you are not here"
		return False

def isInFiles(file_name):
	t = return_codes[0]
	files = getFileScript()
	if file_name in files:
		return True
	else:
		return False

def getFileScript():
	file_list = listdir("./scripts/")
	file_list.sort()
	return file_list


def getCodeMeaning(code):
	if code in return_codes:
		return return_codes[code]
	else:
		return "code is not in a correction"

def login(email, password):
    r = requests.post('https://accounts.thevcgroup.com/apis/v1/quicklogin/',
                      data={'email': email, 'password': password}, verify=False)
    if r.status_code == 200:
        return True, 'Login success'
    elif r.status_code == 403:
        return False, 'Invalid email or password'
    else:
        return False, 'Unknown error'


class MyStdout(object):

	def write(self, val):
		self.val = val
		self.sendData()
		# print val
		
	def flush(self):
		pass

	def setConn(self, conn):
		self.conn = conn
	
	def sendData(self):
		if self.val and self.val != "[localhost] out: ":
			print "from sendData: " + self.val
			self.conn.sendall(self.val)


def sendExec(file_name, username, password, socketNumber, timeout, conn):
	file = open("scripts/" + file_name, "r")
	line = file.read()
	file.close()
	conn.sendall("Run file: " + file_name + "\n")

	with settings(host_string="localhost", port=socketNumber, user=username, password=password, timeout=3,):
		   
		try:
			m = MyStdout()
			m.setConn(conn)
			print "sent conn "
			append('/tmp/'+file_name, line)
			code = run('sh /tmp/' + file_name, warn_only=True, stdout=m, stderr=m).return_code
			run('rm /tmp/' + file_name)
			print "Return code = ",code
			print "Meaning : " + getCodeMeaning(code)
			return code
		except SystemExit as e:
			return 999
		except fabric.exceptions.NetworkError:
			return 555




def startServer():
	HOST = ''                 # Symbolic name meaning all available interfaces
	PORT = 8000              # Arbitrary non-privileged port
	 
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	s.bind((HOST, PORT))
	s.listen(5)

	while 1:
		conn, addr = s.accept()
		data = conn.recv(1024)
		
		print "RESULT: " + data
		
		r = {}
		r = json.loads(data)

		username = r["username"]
		password = r["password"]
		sUsername = r["sUsername"]
		sPassword = r["sPassword"]
		file_name = r["fileName"]
		socketNumber = str(r["socketNumber"])
		timeout = int(r["timeout"])
		isAuth, status = login(sUsername, sPassword) 
		
		if isAuth:
			if isInFiles(file_name):
				code = sendExec(file_name, username, password, socketNumber, timeout, conn)
				code_meaning = getCodeMeaning(code)
				
				if code_meaning == return_codes[66]:
					file_list = getFileScript()
					print "give file to client"
					for file in file_list:
						conn.sendall(str(file) + "\n")
				conn.sendall(code_meaning)

			else:
				conn.sendall("file not in list please reconnect")
		else:
			conn.sendall(status)
		conn.close()

startServer()


# -------------Test Code----------------



# file_name = selectFile('method0')
# sendExec(file_name, "arnon", "a.546548688465", "46545", 3)
# d = {"values": result["values"], "result": getCodeMeaning(result["code"])}



# --------------------------------------