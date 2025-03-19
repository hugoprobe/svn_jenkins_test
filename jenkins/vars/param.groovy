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


