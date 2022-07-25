def call(Map config) {
	if(config?.test)
	{
		dotest(config['test'])
	}
}

def dotest(String message){
	echo "[util2.test]: $message"
}
