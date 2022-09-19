def isNeedCheckout(String svn_url){
    def _isNeedCheckout=false, current_svn_url=""
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
        _isNeedCheckout  = (current_svn_url==svn_url)?false:true        
    } catch (Exception e) {
        _isNeedCheckout  = true
    }		
    return _isNeedCheckout
}

def checkout(String svn_url){  
  /*
    This checkout calling below will call 'checkout' from jenkins 'Pipeline: SCM Step' plugin. https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/.
    NOT calling nested/recursively to 'checkout' method inside this file (because they have different argument type parameter).
  */
    checkout(
	[	$class: 'SubversionSCM', 
		locations: [
			     [	
				cancelProcessOnExternalsFail: true, 
				credentialsId: 'BUILD_USER', 
			      	depthOption: 'unknown', 
			      	ignoreExternalsOption: false, 
			      	local: '.', 
			      	remote: svn_url
			     ]
			   ], 
	      	quietOperation: false, 
	      	workspaceUpdater: [
				    $class: 'UpdateWithCleanUpdater'
				  ]
	]
    )    
}

def update(revision="head"){
    withCredentials([usernamePassword(credentialsId: 'BUILD_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {          
        //bat """svn update --set-depth infinity --username "$USERNAME" --password "$PASSWORD" """
	    bat(script:'svn update --set-depth infinity --username ' + "${USERNAME}" + ' --password ' + "${PASSWORD}")
    }
}

def cleanupWithTortoise(){  
    bat '''TortoiseProc.exe /command:cleanup /path:%cd%  /delunversioned /noui /nodlg  /delignored  /externals 
           TortoiseProc.exe /command:cleanup /path:%cd% /noui /nodlg  /externals /revert ''' 
}
