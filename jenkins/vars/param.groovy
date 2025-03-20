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

 def table(str,arg=""){ return """<table $arg >$str\n</table>\n"""}
 def th(str, arg="") {return """<th $arg>$str</th>\n"""}
 def tr(str) {return "<tr>$str\n</tr>\n"} 
 def td(str) {return "<td>$str</td>\n"}


def getAgentsTableScriptHTML(Map Agents, List ELIGIBLE_AGENTS){ 
    def html_node_options   = {default_label-> """<select name="value" >""" + ([default_label]+ ( ELIGIBLE_AGENTS - default_label)).collect{ item -> return "<option value=$item>$item "}.join(' ') + "</select>"}
    def html_workdir        = {default_workdir-> """<input type="text" name="value" style="width:100%" value="${default_workdir}" >"""}
    def html_table_title    = tr(th("") + th("NodeLabel") + th("WorkingDirectory",'style="width:75%"'))
    def html_table_rows     = ""
    traverseAgents(Agents, "") { variants, _agent ->
        html_table_rows += tr(td("${variants}")+td(html_node_options(_agent.label)) + td(html_workdir(_agent.workdir)) )
    }
    return table(html_table_title+html_table_rows);
}

def agentsTable(Map it){	
    def idx=0, max_idx=-1, flattenedArray = []
    if (params[it.name]){
	flattenedArray = params[it.name].split(',')
	max_idx = params[it.name].trim().isEmpty() ? -1 : (flattenedArray.size() - 1)
    }
    traverseAgents(it.agents, "") { variants, _agent ->
 	println "bbb ${variants}: label ${_agent.label} workdir: ${_agent.workdir} max_idx: $max_idx, idx: $idx"
        _agent.label      = (idx>max_idx)?_agent.label:flattenedArray[idx++]
        _agent.workdir    = (idx>max_idx)?_agent.workdir:flattenedArray[idx++]
        println "aaa ${variants}: label ${_agent.label} workdir: ${_agent.workdir}"
        if(!_agent.workdir || _agent.workdir?.trim().isEmpty())
            error("Workdir at agent $_agent CANNOT BE NULL. Stopping the build early...")        
    }
	return activeChoiceReactiveReference ([  name:(it.name), type:'ET_FORMATTED_HTML',   script:'return """' +  getAgentsTableScriptHTML(it.agents, it.eligibleNodes) + '"""'])
}
