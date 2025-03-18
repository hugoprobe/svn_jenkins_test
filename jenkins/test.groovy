
NODES=[:]

pipeline {
    agent none
    environment{
        ELIGIBLE_NODES          ='JOGBLD0006,JOGBLD0010,JOGBLD0020,JOGBLD0021'
        PUBLISH_DIRECTORIES     ='A,B'
        SCM_URL                 = $/${String.valueOf(scm.getUserRemoteConfigs()[0].getUrl())}/$
        GIT_BRANCH              = $/${String.valueOf(scm.branches[0].name).replaceFirst(/^refs\/(heads|remotes\/[^\/]+)\/?/, "")}/$
    }
    stages {
        stage("Nodes Allocation & Verification")
        {
            steps {
                script{
                    bat "echo SCM_URL ___test2_ = $SCM_URL"
                    bat "echo GIT_BRANCH ___test2_ = $GIT_BRANCH" 
                }
                
            }
        }
        
    }
}
