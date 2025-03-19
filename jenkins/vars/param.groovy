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

@NonCPS def table(str,arg=""){ return """<table $arg >$str\n</table>\n"""}
@NonCPS def th(str, arg="") {return """<th $arg>$str</th>\n"""}
@NonCPS def tr(str) {return "<tr>$str\n</tr>\n"} 
@NonCPS def td(str) {return "<td>$str</td>\n"}

@NonCPS
def getAgentsTableScriptHTML(Agents, ELIGIBLE_AGENTS){ 
    def html_node_options   = {default_label-> """<select name="value" >""" + ([default_label]+ ( (ELIGIBLE_AGENTS.collect { it.trim() }) - default_label)).collect{ item -> return "<option value=$item>$item "}.join(' ') + "</select>"}
    def html_workdir        = {default_workdir-> """<input type="text" name="value" style="width:100%" value="${default_workdir}" >"""}
    def html_table_title    = tr(th("") + th("NodeLabel") + th("WorkingDirectory",'style="width:75%"'))
    def html_table_rows     = ""
    traverseAgents(Agents, "") { variants, _agent ->
        html_table_rows += tr(td("${variants}")+td(html_node_options(_agent.label)) + td(html_workdir(_agent.workdir)) )
    }
    return table(html_table_title+html_table_rows);
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


def agentsTable(String paramName, Map Agents, String[] eligibleNodes, String flattenedAgentsParam=""){
    def flattenedArray = flattenedAgentsParam?.split(',')?:[]
    def flattenedIdx = 0
    traverseAgents(Agents, "") { variants, _agent ->
        _agent.label      = (flattenedIdx>=flattenedArray.size())?:flattenedArray[flattenedIdx++]
        _agent.workdir    = (flattenedIdx>=flattenedArray.size())?:flattenedArray[flattenedIdx++]
        //println "${variants}: label ${_agent.label} workdir: ${_agent.workdir}"
        if(!(_agent?.workdir) )
            error("Workdir at Agent $_agent CANNOT BE NULL. Stopping the build early...")        
    }

    return activeChoiceReactiveReference ([  name:(paramName), type:'ET_FORMATTED_HTML',   script:'return """' +  getAgentsTableScriptHTML(Agents, eligibleNodes) + '"""'])
}



