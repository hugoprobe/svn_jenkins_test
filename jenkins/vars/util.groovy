def mkdir(String path) {
        bat """ @echo off		
		if NOT EXIST "${path}" (
			mkdir "${path}"
			echo [util.mkdir] Successfully created: "${path}"
		) else (
			echo [util.mkdir] Failed, path already exist: "${path}"
		)
		"""
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
		exit /b 0
	"""
	if((srcs instanceof Collection) || (srcs instanceof String[])){
		assembleScript=	''' @echo off
			setlocal enabledelayedexpansion
			for  %%i in ("'''+ srcs.join('" "')+'''") do (     
			    call :copypath %%i
			) 
			goto :eof''' +assembleScript	
		 //echo "Copy many"
	}else{
		assembleScript= """@echo off && call :copypath ${srcs} && goto :eof"""+assembleScript
		 // echo "Copy one"
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



def validateGitUrlInWorkspace(String gitRemote, String gitUrlSCM){
    def gitUrlWorkspace = bat(returnStdout:true, script: """  @echo off
                                                        for /f %%u in ('git config --get remote.${gitRemote}.url') do echo %%u""" ).trim()
    if(!gitUrlWorkspace.trim().equalsIgnoreCase(gitUrlSCM.trim())){
        error("gitUrlSCM=$gitUrlSCM \n gitUrlWorkspace=$gitUrlWorkspace \n The git url on your workspace does not match the one in the SCM. Stopping the build early")
    }
}


def isRemoteBranchExist(String gitRemote, String branchName){
	def error_status = bat(returnStatus:true, script: "git ls-remote --exit-code --heads ${gitRemote} ${branchName}")
	return (error_status != 0)
}

def saveParamToFile(Map params, String filePath){
	writeFile file: filePath, text: params.collect { "${it.key}=${it.value}" }.join('\n')
}
