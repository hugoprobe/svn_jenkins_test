def isNeedCheckout(svn_url, target_dir="%CD%"){
    def isNeedCheckout=false, current_svn_url=""
    def CURRENT_SVN_URL_SCRIPT  = ''' @echo off   
                                      call :fetch_svn_url
                                      goto :end
                                      :fetch_svn_url
                                        set "SVN_URL="
                                        for /f %%i in ('svn info --show-item url') do set SVN_URL=%%i
                                        echo %SVN_URL%
					exit /b 0
                                      :end
                                  '''
    try {
        
        current_svn_url = bat(script:CURRENT_SVN_URL_SCRIPT, returnStdout:true)
        current_svn_url = current_svn_url.trim()
	echo 'svn url : ' + svn_url
	echo 'current svn url : ' + current_svn_url
        isNeedCheckout  = (current_svn_url==svn_url)?false:true        
    } catch (Exception e) {
        isNeedCheckout  = true
    }
	echo 'return isNeedCheckout : ' + String.valueOf(isNeedCheckout)	
    return isNeedCheckout
}

def checkout(svn_url){
    withCredentials([usernamePassword(credentialsId: 'BUILD_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {    
        checkout([$class: 'SubversionSCM', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'BUILD_USER', depthOption: 'unknown', ignoreExternalsOption: false, local: '.', remote: svn_url]], quietOperation: false, workspaceUpdater: [$class: 'UpdateWithCleanUpdater']])
    }  
}

def update(svn_url, revision="head"){
    withCredentials([usernamePassword(credentialsId: 'BUILD_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {          
        bat "svn update --revision $revision --set-depth infinity --username $USERNAME --password $PASSWORD"
    }
}

def cleanupUsingTortoise(target_dir="%CD%"){  
    bat '''TortoiseProc.exe /command:cleanup /path:%cd%  /delunversioned /noui /nodlg  /delignored  /externals 
           TortoiseProc.exe /command:cleanup /path:%cd% /noui /nodlg  /externals /revert ''' 
}
