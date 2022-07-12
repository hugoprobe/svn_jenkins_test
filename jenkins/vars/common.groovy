
def getEligibleNodes(){
  return ['JOGBLD0021','JOGBLD0020','JOGBLD0010','JOGBLD0006']
}

def getPublishDirectories(){  
  return [$/\\\\jogbld0001\\DEV_SHARED_FOLDER_8TB\\JOG_PROJECT\\Android\\DMK\\QA-Release/$,
                             $/\\\\gameloft.org\\jog\\DEV\\HD\\Project\\Android\\DMK\\QA-Release/$ ]
}

def makedir(String path) {
        if (!fileExists(path)) {                               
                bat 'mkdir "' + path +'"'                         
        }        
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

def IsNeedCheckoutSVN(svn_url, target_dir="%CD%"){
    def isNeedCheckout=false, current_svn_url=""
    def CURRENT_SVN_URL_SCRIPT  = ''' @echo off  
rem                                       pushd ''' + target_dir +'''  
                                      call :fetch_svn_url
rem                                      popd
                                      goto :end
                                      :fetch_svn_url
                                        set "SVN_URL="
                                        for /f %%i in ('svn info --show-item url') do set SVN_URL=%%i
                                        echo %SVN_URL%
										exit /b 0
                                      :end
                                  '''
    try {
        echo 'svn url : ' + svn_url
        current_svn_url = bat(script:CURRENT_SVN_URL_SCRIPT, returnStdout:true)
        current_svn_url = current_svn_url.trim()
        isNeedCheckout  = (current_svn_url==svn_url)?false:true        
    } catch (Exception e) {
        isNeedCheckout  = true
    }
    return isNeedCheckout
}

def CheckoutSVN(svn_url, target_dir="%CD%"){
  dir(target_dir){
    withCredentials([usernamePassword(credentialsId: 'BUILD_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {    
        checkout([$class: 'SubversionSCM', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'BUILD_USER', depthOption: 'unknown', ignoreExternalsOption: false, local: '.', remote: svn_url]], quietOperation: false, workspaceUpdater: [$class: 'UpdateWithCleanUpdater']])
    }
  }
}

def UpdateSVN(svn_url, target_dir="%CD%"){
  dir(target_dir){
    withCredentials([usernamePassword(credentialsId: 'BUILD_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {          
        bat "svn update "+SVN_UPDATE_TAG+" --set-depth infinity --username $USERNAME --password $PASSWORD"
    }
  }
}

def CleanUpSVN_WithTortoiseProc(target_dir="%CD%"){
  dir(target_dir){
    bat 'TortoiseProc.exe /command:cleanup /path:%cd%  /delunversioned /noui /nodlg  /delignored  /externals ' 
    bat 'TortoiseProc.exe /command:cleanup /path:%cd% /noui /nodlg  /externals /revert '
  }
}
def xcopy(String src, String dst, String flag="")
{
        bat """xcopy "${src}" "${dst}" ${flag}"""       
}

def methodA(){
        bat  " echo Method AAAAB is execute new"
}
def methodB(){
        bat  " echo Method BBBC is execute"
}

def activeChoiceParameter(it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [ $class: 'ChoiceParameter', name: (it.name),  choiceType: (it.type),  description:(it?.desc?:''), script: [ $class: 'GroovyScript', script: (setStrScript(it.script)), fallbackScript:(setStrScript('return "error script"'))]]
}
def activeChoiceReactiveParameter(it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [ $class: 'CascadeChoiceParameter', name: (it.name),  choiceType: (it.type), description:(it?.desc?:''), referencedParameters:(it?.references?:'') ,script: [ $class: 'GroovyScript', script: (setStrScript(it.script)), fallbackScript:(setStrScript('return "error script"'))]]
}
def activeChoiceReactiveReferenceParameter(it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [ $class: 'DynamicReferenceParameter', name: (it.name),  choiceType: (it.type), omitValueField:(it?.omit?:true), description:(it?.desc?:''), referencedParameters:(it?.references?:'') ,script: [ $class: 'GroovyScript', script: (setStrScript(it.script)), fallbackScript:(setStrScript('return "error script"'))]]
}
return this
