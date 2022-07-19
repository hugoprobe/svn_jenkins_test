def mkdir(String path) {
        bat """	@echo off 
		echo [utils.mkdir] Trying to mkdir "${path}"
		if NOT EXIST "${path}" 
		mkdir "${path}" """
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
	bat """ @echo off
		echo [utils.mklink] Trying to mklink /$type $link $target
		IF EXIST $link rmdir $link		
              	mklink /$type $link $target """
}

def delete(String path, String options="")
{	
	bat """ @echo off		
		echo [utils.delete] Trying to delete "${path}"
		if exist "${path}"\\* (
		    rd   """ + (options?:'/Q /S') + """ "${path}" 		    
		) else if exist "${path}" (
		    del  """ + (options?:'/Q /F /S') + """ "${path}" 
		) else (
		    echo [util.delete] Failed to delete, path NOT FOUND: "${path}"
		)"""
}

def isExist (str_path) {
	return bat(script:'@echo off && if exist "'+str_path+'" (echo true) else echo false', returnStdout:true).trim()=="true"
}

def rename(String ori_path, String renamed_target)
{
	bat """	@echo off 
		echo [utils.rename] Trying to rename "${ori_path}" into ${renamed_target}
		rem if NOT EXIST ${renamed_target} 
		ren "${ori_path}" "${renamed_target}" """	
}

def copy(String src, String dst, String opt='')
{
	bat """ @echo off
		echo [utils.copy] Trying to copy "${src}" to "${dst}"
		if exist  "${src}\\*" (
		    xcopy "${src}" "${dst}" """ + (opt?:'/Y /R /I /S /E') + """
		) else if exist "${src}" (
		    xcopy "${src}" "${dst}" """ + (opt?:'/Y /R /I') + """
		) else (
		    echo [util.copy] Failed to copy, source NOT FOUND: "${src}"		    
		)"""
}

//For getting environment variables via batch
def getBatchEnv(String strScript, String vars)
{
    def result=[]
    printedKeys="%"+vars.replace(',' , "%:%")+"%:null"    
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
