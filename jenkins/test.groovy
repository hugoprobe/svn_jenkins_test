
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
        stage("Load-Library")
        {
            steps{
                script{
                    echo "here 77777777777777777777777777777777777777777777777"
                    SCM_URL= String.valueOf(scm.getUserRemoteConfigs()[0].getUrl())
                    SCM_Branch = String.valueOf(scm.branches[0].name)
                    echo "SCM_Branch = ${SCM_Branch}"
                    echo "SCM_URL = ${SCM_URL}"
                    libPath='jenkins/sharedlibs'
                    //library identifier: 'common@', retriever: modernSCM(scm: [$class: 'GitSCMSource', credentialsId: 'BUILD_USER', remote: (SCM_URL), traits: [sparseCheckoutPaths([sparseCheckoutPaths: [[path: (libPath)]]])]], libraryPath: ("${libPath}/"))
                    library identifier: 'common@', retriever: legacySCM(libraryPath: (libPath), scm: scmGit(branches: [[name: (SCM_Branch)]], extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: (libPath) ]]]], userRemoteConfigs: [[credentialsId: 'BUILD_USER', url: (SCM_URL)]]))
                }
            }
        }
        stage("Nodes Allocation & Verification")
        {
            steps {
                script{
                    echo "SCM_URL = $SCM_URL   --> ${params.SVN_Revision}"                     
                    echo "GIT_BRANCH = $GIT_BRANCH --> ${params.SVN_Revision}" 
                }
                
            }
        }
        
    }
}
