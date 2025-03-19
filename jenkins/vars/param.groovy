def getVal(){
    return params.Sleep
}


def setParam(Map node){
    node['1'] = getVal()
}

def activeChoice(Map it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [  $class: 'ChoiceParameter', 
              name: (it.name),  
              choiceType: (it.type),  
              description:(it?.desc?:''), 
              script: [ $class: 'GroovyScript', 
                        script: (setStrScript(it.script)), 
                        fallbackScript:(setStrScript('return "error script"'))
                      ]
           ]
}
def activeChoiceReactive(Map it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [  $class: 'CascadeChoiceParameter', 
              name: (it.name),  
              choiceType: (it.type), 
              description:(it?.desc?:''), 
              referencedParameters:(it?.references?:''),
              script: [ $class: 'GroovyScript', 
                        script: (setStrScript(it.script + '  // references : '+ (it?.references?:'')  )), 
                        fallbackScript:(setStrScript('return ["error script"]'))
                      ]
           ]
}

def activeChoiceReactiveReference(Map it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [  $class: 'DynamicReferenceParameter', 
              name: (it.name),  
              choiceType: (it.type), 
              omitValueField:(it?.omit?:true), 
              description:(it?.desc?:''), 
              referencedParameters:(it?.references?:''),
              script: [ $class: 'GroovyScript', 
                        script: (setStrScript(it.script)), 
                        fallbackScript:(setStrScript('return "error script"'))
                      ]
           ]
}




def agentsTable(Map it){
    //String paramName, Map Agents, String[] eligibleNodes, String flattenedAgentsParam=""
    /*
    def flattenedArray = flattenedAgentsParam?.split(',')?:[]
    def flattenedIdx = 0
    traverseAgents(Agents, "") { variants, _agent ->
        _agent.label      = (flattenedIdx>=flattenedArray.size())?:flattenedArray[flattenedIdx++]
        _agent.workdir    = (flattenedIdx>=flattenedArray.size())?:flattenedArray[flattenedIdx++]
        //println "${variants}: label ${_agent.label} workdir: ${_agent.workdir}"
        if(!(_agent?.workdir) )
            error("Workdir at Agent $_agent CANNOT BE NULL. Stopping the build early...")        
    }*/
    return true
   // return activeChoiceReactiveReference ([  name:(paramName), type:'ET_FORMATTED_HTML',   script:'return """' +  getAgentsTableScriptHTML(Agents, eligibleNodes) + '"""'])
}


