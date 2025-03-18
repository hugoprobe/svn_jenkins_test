
NODES=[:]

pipeline {
    agent none
    environment{
        ELIGIBLE_NODES          ='JOGBLD0006,JOGBLD0010,JOGBLD0020,JOGBLD0021'
        PUBLISH_DIRECTORIES ='A,B'
        SCM_URL = "${String.valueOf(scm.getUserRemoteConfigs()[0].getUrl())}"
    }
    stages {
        stage("Nodes Allocation & Verification")
        {
            steps {
                script{
                    echo "SCM_URL = $SCM_URL"
                }
                
            }
        }
        
    }
}
