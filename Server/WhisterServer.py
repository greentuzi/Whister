#-*- coding:utf-8 -*-

from SocketServer import ThreadingTCPServer, StreamRequestHandler
import traceback
import MySQLdb
import DBManager   
import json
import datetime
import time
import ast

class MyStreamRequestHandler(StreamRequestHandler):

	def handle(self):
		funcDict = {
			"register":self.register,\
			"login":self.login,\
			"modifyUserInfo":self.modifyUserInfo,\
			"requirePicWall":self.requirePicWall,\
			"requirePicInfo":self.requirePicInfo,\
			"requireUserShared":self.requireUserShared,\
			"requireFavorList":self.requireFavorList,\
			"favor":self.favor,\
			"unfavor":self.unfavor,\
			"requireFavorUsers":self.requireFavorUsers,\
			"deletePic":self.deletePic,\
			"uploadPic":self.uploadPic
			}
		while True:
			try:
				data = self.rfile.readline().strip()
				print data
				jobj = json.loads(data)
				funcDict[jobj["flag"]](jobj)
			except:
				traceback.print_exc()
				break
				
	def register(self, jobj):
		ret = {}
		ID = jobj.get("ID")
		password = jobj.get("password")
		cursor = DB.transaction()
		try:
			cursor.execute("insert into user(userID, password) value('%s','%s')" %(ID,password))
			cursor.execute("select uid from user where userID = '%s'" % ID)
			uid = cursor.fetchone()["uid"]
			DB.commit()
			ret["flag"] = "registerSucceed"
			ret["uid"] = uid
		except MySQLdb.IntegrityError:
			ret["flag"] = "registerFailed"
			ret["msg"] = "The ID is registered"
		except:
			traceback.print_exc()
			ret["flag"] = "registerFailed"
			ret["msg"] = "Unknown error"
		finally:
			cursor.close()		
			self.write(json.dumps(ret))
		
	def login(self, jobj):
		ret = {}
		ID = jobj.get("ID")
		password = jobj.get("password")
		cursor = DB.transaction()
		try:
			r = cursor.execute("select uid, userID, nickname, portrait, sex, signiture from user\
				where userID = '%s' and password = '%s'" % (ID,password))
			if r == 1:
				ret = cursor.fetchone()
				ret["flag"] = "loginSucceed"
				if ret["portrait"]:
					ret["portrait"] = ast.literal_eval(ret["portrait"])
			elif r == 0:
				ret["flag"] = "loginFailed"
				ret["msg"] = "ID and password don't match"
			else:
				ret["flag"] = "loginFailed"
				ret["msg"] = "Unknown error"
		except:
			traceback.print_exc()
			ret["flag"] = "loginFailed"
			ret["msg"] = "Unknown error"			
		finally:
			cursor.close()		
			self.write(json.dumps(ret))
			
	def modifyUserInfo(self,jobj):
		uid = jobj.get("uid")
		ID = jobj.get("ID")
		nickname = jobj.get("nickname")
		portrait = jobj.get("portrait")
		sex = jobj.get("sex")
		signiture = jobj.get("signiture")

		ret = {}
		cursor = DB.transaction()
		try:
			cursor.execute("update user set userID = '%s', nickname = '%s', portrait = '%s',\
				sex = %d, signiture = '%s' where uid = %d"\
				%(ID,nickname,portrait, sex, signiture, uid))
			DB.commit()
			ret["flag"] = "modifySucceed"
		except:
			traceback.print_exc()
			ret["flag"] = "modifyFailed"
			ret["msg"] = "Unknown Error"
		finally:
			cursor.close()		
			self.write(json.dumps(ret))

	def requirePicWall(self,jobj):
		num = jobj.get("num")
		ret = {}
		ret["flag"] = "picWall"
		cursor = DB.transaction()
		try:
			r = cursor.execute("select pid, large, picIntro, picFavor, submitter, date from picture\
				order by date limit %d, 1" % (num-1))
			if r == 1:
				p = cursor.fetchone()
				ret["picID"] = p["pid"]
				if p["large"]:
					ret["picData"] = ast.literal_eval(p["large"])
				ret["picIntro"] = p["picIntro"]
				ret["picFavor"] = p["picFavor"]
				if p["date"]:
					ret["date"] = p["date"].strftime('%Y-%m-%d')
				cursor.execute("select uid, nickname, portrait from user where uid = %d" %p["submitter"])
				u = cursor.fetchone()
				ret["uid"] = u["uid"]
				ret["submitterName"] = u["nickname"]
				if u["portrait"]:
					ret["submitterPortrait"] = ast.literal_eval(u["portrait"])
			else:
				ret["picID"] = -1
		except:
			traceback.print_exc()
			ret["picID"] = -1
		finally:
			cursor.close()
			self.write(json.dumps(ret))
				
	def requirePicInfo(self,jobj):
		pid = jobj.get("picID")
		ret = {}
		ret["flag"] = "picInfo"
		cursor = DB.transaction()
		try:
			r = cursor.execute("select pid, large, picIntro, picFavor, submitter, date from picture\
				where pid = %d" % pid)
			if r == 1:
				p = cursor.fetchone()
				ret["picID"] = p["pid"]
				if p["large"]:
					ret["picData"] = ast.literal_eval(p["large"])
				ret["picIntro"] = p["picIntro"]
				ret["picFavor"] = p["picFavor"]
				if p["date"]:
					ret["date"] = p["date"].strftime('%Y-%m-%d')
				cursor.execute("select uid, nickname, portrait from user where uid = %d" %p["submitter"])
				u = cursor.fetchone()
				ret["uid"] = u["uid"]
				ret["submitterName"] = u["nickname"]
				if u["portrait"]:
					ret["submitterPortrait"] = ast.literal_eval(u["portrait"])
			else:
				ret["picID"] = -1
		except:
			traceback.print_exc()
			ret["picID"] = -1
		finally:
			cursor.close()
			self.write(json.dumps(ret))
			
	def requireUserShared(self,jobj):
		uid = jobj.get("uid")
		beginPicNum = jobj.get("beginPicNum")
		num = jobj.get("num")
		ret = {}
		ret["flag"] = "userShared"
		cursor = DB.transaction()
		try:
			cursor.execute("select pid, small from picture where submitter = %d" % uid)
			d = cursor.fetchall()
			ret["picList"] = []
			print d
			for item in d:
				ret["picList"].append({"picID":item.get("pid"), "picData":ast.literal_eval(item.get("small"))})
		except:
			traceback.print_exc()
			ret["picList"] = []
		finally:
			cursor.close()
			self.write(json.dumps(ret))
		
	def requireFavorList(self,jobj):
		uid = jobj.get("uid")
		beginPicNum = jobj.get("beginPicNum")
		num = jobj.get("num")
		ret = {}
		ret["flag"] = "favorList"
		cursor = DB.transaction()
		try:

			cursor.execute("select picture.pid, small from picture join favor \
				on picture.pid = favor.pid and favor.uid = %d " % uid)
			d = cursor.fetchall()
			ret["picList"] = []
			for item in d:
				ret["picList"].append({"picID":item.get("pid"), "picData":ast.literal_eval(item.get("small"))})
		except:
			traceback.print_exc()
			ret["picList"] = []
		finally:
			cursor.close()
			self.write(json.dumps(ret))
	
	def favor(self,jobj):
		pid = jobj.get("picID")
		uid = jobj.get("uid")
		ret = {}
		ret["flag"] = "favorSucceed"
		cursor = DB.transaction()
		try:
			r = cursor.execute("update picture set picFavor = picFavor + 1 where pid = %d" % pid)
			print r
			if r==1:
				cursor.execute("insert into favor(pid,uid) value(%d,%d)" %(pid,uid))
				DB.commit()
			else:
				ret["flag"] = "favorFailed"
		except:
			traceback.print_exc()
			ret["flag"] = "favorFailed"
		finally:
			cursor.close()
			self.write(json.dumps(ret))
	
	def unfavor(self,jobj):
		pid = jobj.get("picID")
		uid = jobj.get("uid")
		ret = {}
		ret["flag"] = "unfavorSucceed"
		cursor = DB.transaction()
		try:
			r = cursor.execute("delete from favor where pid = %d and uid = %d" % (pid,uid))
			print r
			if r==1:
				cursor.execute("update picture set picFavor = picFavor - 1 where pid = %d" % pid)
				DB.commit()
			else:
				ret["flag"] = "unfavorFailed"
		except:
			traceback.print_exc()
			ret["flag"] = "unfavorFailed"
		finally:
			cursor.close()
			self.write(json.dumps(ret))
	
	def requireFavorUsers(self,jobj):
		pid = jobj.get("picID")
		ret = {}
		ret["flag"] = "favorUsers"
		ret["userList"] =[]
		cursor = DB.transaction()
		try:
			cursor.execute("select user.uid, user.portrait, user.nickname from user join favor\
				on user.uid = favor.uid and favor.pid = %d" %pid)
			d = cursor.fetchall()
			for item in d:
				item["portrait"] = ast.literal_eval(item["portrait"])
			ret["userList"] = list(d)
			
		except:
			traceback.print_exc()
			ret["userList"] = []
		finally:
			cursor.close()
			self.write(json.dumps(ret))
	
	def deletePic(self,jobj):
		pid = jobj.get("picID")
		ret = {}
		ret["flag"] = "deletePicSucceed"
		cursor = DB.transaction()
		try:
			r = cursor.execute("delete from picture where pid = %d " % pid)
			if r != 1:
				ret["flag"] = "deletePicFailed"
			else:
				DB.commit()
		except:
			traceback.print_exc()
			ret["flag"] = "deletePicFailed"
		finally:
			cursor.close()
			self.write(json.dumps(ret))
			
	def uploadPic(self,jobj):
		picData = jobj.get("picData")
		picIntro = jobj.get("picIntro")
		uid = jobj.get("uid")
		ret = {}
		ret["flag"] = "uploadPicSucceed"
		cursor = DB.transaction()
		date = time.strftime('%Y-%m-%d',time.localtime(time.time()))
		try:
			cursor.execute("select count(*) from picture where submitter = %d and date = '%s'" %(uid, date))
			if cursor.fetchone().get("count(*)") != 0:
				ret["flag"] = "uploadPicFailed"
			else:
				cursor.execute("insert into picture(large, picIntro, submitter,date) values('%s', '%s', %d , '%s')" \
					% (picData, picIntro, uid, date))
				if cursor.execute("select pid from picture where submitter = %d and date = '%s'" \
					% (uid, date)) == 1:
					ret["pid"] = cursor.fetchone().get("pid")
					DB.commit()
		except:
			traceback.print_exc()
			ret["flag"] = "uploadPicFailed"
		finally:
			cursor.close()
			self.write(json.dumps(ret))	
		
	def write(self,msg):
		self.wfile.write(msg+'\n')
if __name__ == "__main__":
	host = ""
	port = 80
	addr = (host, port)
    
	DB = DBManager.DBManager()
	
	server = ThreadingTCPServer(addr, MyStreamRequestHandler)
	server.serve_forever()
	
