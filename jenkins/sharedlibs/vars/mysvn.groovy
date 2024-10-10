def call(Map config) {
	if(config?.test)
	{
		dotest(config['test'])
	}
}

def dotest(String message){
	def okey="okey"
	echo "[util2.test]: $message " + okey + " " + getStringTest()
}

@NonCPS
def getStringTest(){
	return "HAHAHAHA"
}
