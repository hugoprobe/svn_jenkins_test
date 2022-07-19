def mkdir(String path) {
        bat 'if NOT EXIST "' + path + '" mkdir '+ path        
}

def deepmkdir(String path)
{       
        int lastSlashIdx = path.lastIndexOf("\\")
        def parentDir=path.substring(0, lastSlashIdx)
        if ( !(fileExists(path) || ( lastSlashIdx==1 && path[0]==path[1]) || path[lastSlashIdx-1]==':') ){                
                deepmkdir(parentDir)
        }
        makedir(path)
}

def mklink(type, String link, String target){	
	bat """ IF EXIST $link rmdir $link
              	mklink /$type $link $target """
}

def delete(String path, String options="")
{	
	bat ''' @echo off
		if exist "''' + path +'''"\\* (
		    rd   ''' + (options?:'/Q /S') +' "' + path + '''" 
		) else if exist "''' + path + '''" (
		    del  ''' + (options?:'/Q /F /S') + ' "' + path + '''" 
		) else (
		    echo [util.delete] Failed to delete: Path Not Found.
		)'''
}

def isExist (str_path) {
	return bat(script:'@echo off && if exist "'+str_path+'" (echo true) else echo false', returnStdout:true).trim()=="true"
}

def rename(String ori_path, String renamed_target)
{
	bat "if EXIST ${ori_path} ren ${ori_path} ${renamed_target}"	
}

def copy(String src, String dst, String opt='')
{
	bat """			
		if exist  "${src}\\*" (
		    xcopy "${src}" "${dst}" """ + (opt?:'/Y /R /I /S /E') + """
		) else if exist "${src}" (
		    xcopy "${src}" "${dst}" """ + (opt?:'/Y /R /I') + """
		) else (
		    echo [util.delete] Failed to copy: Source Not Found.
		)"""
}

//For getting environment variables via batch
def getBatchEnv(String strScript, String vars)
{
    def result=[]
    printedKeys="%"+vars.replace(',' , "%:%")+"%:null"
    echo "printedKeys $printedKeys"
    def printedScript  = ''' @echo off
                         ''' + strScript + '''
                            echo ''' + printedKeys
    def values 	= (bat(script:printedScript, returnStdout:true)).trim().split(":")  
    def keys	= vars.split(',')
	
    for(int i=0; i<keys.size();i++)
	result.add("${keys[i]}=${values[i]}")
    return result 
}

@NonCPS
def getChangeString() {
	MAX_MSG_LEN = 100
	def changeString = ""

	echo "Gathering SCM changes"
	def changeLogSets = currentBuild.changeSets
	for (int i = 0; i < changeLogSets.size(); i++) {
		def entries = changeLogSets[i].items
		for (int j = 0; j < entries.length; j++) {
			def entry = entries[j]
			truncated_msg = entry.msg.take(MAX_MSG_LEN)
			changeString += "* ${truncated_msg} [${entry.author}]\n"
		}
	}

	if (!changeString) {
		changeString = "* No new changes"
	}
	return changeString
}
