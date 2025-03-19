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
@NonCPS
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


// Function to traverse the map tree recursively
@NonCPS
def traverseAgents(Map agents, String agentVariant, Closure closure) {
    agents.each { key, value ->
        def newAgentVariant = agentVariant?"${agentVariant}.${key}":key
        if ((value instanceof Map) && !(value.keySet().contains('label'))) {
            traverseAgents(value, newAgentVariant, closure)
        } else {            
            closure(newAgentVariant, value)
        }
    }
}

@NonCPS
def agentsTable(String paramName, Map Agents, String[] eligibleNodes, String flattenedAgentsParam=""){
     return null
   // return activeChoiceReactiveReference ([  name:(paramName), type:'ET_FORMATTED_HTML',   script:'return """' +  getAgentsTableScriptHTML(Agents, eligibleNodes) + '"""'])
}


