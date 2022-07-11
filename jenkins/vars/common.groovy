def ELIGIBLE_NODES        = ['JOGBLD0021','JOGBLD0020','JOGBLD0010','JOGBLD0006']
def PUBLISH_DIRECTORIES   = [$/\\\\jogbld0001\\DEV_SHARED_FOLDER_8TB\\JOG_PROJECT\\Android\\DMK\\QA-Release/$,
                             $/\\\\gameloft.org\\jog\\DEV\\HD\\Project\\Android\\DMK\\QA-Release/$ ]

def getEligibleNodes(){
  return ELIGIBLE_NODES
}

def getPublishDirectories(){  
  return PUBLISH_DIRECTORIES
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
    return [ $class: 'ChoiceParameter', name: (it.name),  choiceType: (it.type), omitValueField:(it?.omit?:true), description:(it?.desc?:''), script: [ $class: 'GroovyScript', script: (setStrScript(it.script)), fallbackScript:(setStrScript('return "error script"'))]]
}
def activeChoiceReactiveParameter(it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [ $class: 'CascadeChoiceParameter', name: (it.name),  choiceType: (it.type), omitValueField:(it?.omit?:true), description:(it?.desc?:''), referencedParameters:(it?.references?:'') ,script: [ $class: 'GroovyScript', script: (setStrScript(it.script)), fallbackScript:(setStrScript('return "error script"'))]]
}
def activeChoiceReactiveReferenceParameter(it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [ $class: 'DynamicReferenceParameter', name: (it.name),  choiceType: (it.type), omitValueField:(it?.omit?:true), description:(it?.desc?:''), referencedParameters:(it?.references?:'') ,script: [ $class: 'GroovyScript', script: (setStrScript(it.script)), fallbackScript:(setStrScript('return "error script"'))]]
}
return this
