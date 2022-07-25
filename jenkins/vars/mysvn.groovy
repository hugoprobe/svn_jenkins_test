def call(Closure body) {
    node('mysvn') {
        body()
    }
}

def test(message){
	echo "[util2.test]: $message"
}
