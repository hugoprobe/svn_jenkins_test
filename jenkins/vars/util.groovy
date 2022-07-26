def test(message){
	echo "[util.test]: $message"
}
def mkdir(String path) {
        bat """	@echo off 
		echo [util.mkdir] "${path}"
		if NOT EXIST "${path}" (
			mkdir "${path}"
		)"""
}

def deepmkdir(String path)
{       
        int lastSlashIdx = path.lastIndexOf("\\")
        def parentDir=path.substring(0, lastSlashIdx)
        if ( !(fileExists(path) || ( lastSlashIdx==1 && path[0]==path[1]) || path[lastSlashIdx-1]==':') ){                
                deepmkdir(parentDir)
        }
        mkdir(path)
}

def mklink(type, String link, String target){	
	bat """ @echo off
		echo [util.mklink] /$type $link $target
		IF EXIST $link rmdir $link		
              	mklink /$type $link $target """
}

def delete(String path, String options="")
{	
	bat """ @echo off		
		echo [util.delete] "${path}"
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
		echo [util.rename] "${ori_path}" into "${renamed_target}"
		ren "${ori_path}" "${renamed_target}" """	
}

def copy(srcs, String dst, String opt='')
{	
	def assembleScript =""" 
	   :copyPath
		echo [util.copy] ${opt} %1 to "${dst}"
		if exist  %1\\* (
		    xcopy %1 "${dst}" """ + (opt?:'/Y /R /I /S /E') + """
		) else if exist %1 (
		    xcopy %1 "${dst}" """ + (opt?:'/Y /R /I') + """
		) else (
		    echo [util.copy] Failed to copy, source NOT FOUND: %1		    
		)
		exit \b 0
	"""
	if((srcs instanceof Collection) || (srcs instanceof String[])){
		assembleScript=	''' @echo off
			setlocal enabledelayedexpansion
			for  %%i in ("'''+ srcs.join('" "')+'''") do (     
			    call :copypath %%i
			) 
			goto :eof''' +assembleScript	
		echo "Copy many"
	}else{
		assembleScript= """@echo off && call :copypath ${srcs}"""+assembleScript
		echo "Copy one"
	}
	return  bat (assembleScript+":eof")
}

def getLastFolderAlphabetical(String parentPath='%CD%'){		
	return	(bat(script	: '''	@echo off
					setlocal enabledelayedexpansion

					for /F "tokens=* delims=" %%a in ('dir /AD /B /O-N "''' + parentPath +'''"') do (
				    		set lastvalue=%%a
				    		goto break
					)
					:break
					echo !lastvalue!''', 
		returnStdout:true)).trim()
}

//For getting environment variables via batch
def getBatchEnv(String strScript, String vars)
{
    def result=[]
    printedKeys="%"+vars.replace(',' , "%:%")+"%:null"    
    def printedScript  = ''' @echo off
                         ''' + strScript + '''
                            echo ''' + printedKeys
    echo "printedScript \n" + printedScript
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
