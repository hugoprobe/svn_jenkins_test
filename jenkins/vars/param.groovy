def isRemoteBranchExistR(String gitRemote, String branchName){
	def error_status = bat(returnStatus:true, script: "git ls-remote --exit-code --heads ${gitRemote} ${branchName}")
	return (error_status != 0)
}

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


def agentsTable(Map it){	
    def flattenedArray = it.flattenedAgentsParam.split(',')
    def max_idx = flattenedArray.size() - 1	
    def idx = 0
    traverseAgents(it.Agents, "") { variants, _agent ->
 	println "b ${variants}: label ${_agent.label} workdir: ${_agent.workdir} max_idx: $max_iox, idx: $idx"
        _agent.label      = (idx>max_idx)?_agent.label:flattenedArray[idx++]
        _agent.workdir    = (idx>max_idx)?_agent.workdir:flattenedArray[idx++]
        println "a ${variants}: label ${_agent.label} workdir: ${_agent.workdir}"
        if(!_agent.workdir || _agent.workdir?.trim().isEmpty())
            error("Workdir at Agent $_agent CANNOT BE NULL. Stopping the build early...")        
    }
}
