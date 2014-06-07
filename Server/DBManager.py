import MySQLdb

class DBManager:
	def __init__(self, host="", user="root", password="1", db="db"):
		self.host = host
		self.user = user
		self.password = password
		self.db = db
		try:
			self.conn = MySQLdb.connect(host=self.host, user=self.user, passwd=self.password ,db=self.db)
		except:
			print "Failed to connect to MySQL"
		
	def transaction(self):
		return self.conn.cursor(MySQLdb.cursors.DictCursor)

	def commit(self):
		self.conn.commit()
		
	def __del__(self):
		if self.conn:
			self.conn.close()
	