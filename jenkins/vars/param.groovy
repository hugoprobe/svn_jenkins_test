def isRemoteBranchExistR(String gitRemote, String branchName){
	def error_status = bat(returnStatus:true, script: "git ls-remote --exit-code --heads ${gitRemote} ${branchName}")
	return (error_status != 0)
}
def test(){
	return true
}
