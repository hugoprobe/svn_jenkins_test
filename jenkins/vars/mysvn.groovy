def call(Map config) {
	if(config?.test)
	{
		test(config.test.value)
	}
}

def test(message){
	echo "[util2.test]: $message"
}
