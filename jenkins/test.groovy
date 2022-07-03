

/*
def common(){
    s_common =[:]
    if(NODE_LABELS && WORKSPACE) {
        if(!s_common.containsKey(NODE_LABELS)) {
            echo "s load common lib at label: ${NODE_LABELS}, ws: ${WORKSPACE} "        
            s_common[NODE_LABELS] = load "jenkins/common.Groovy"
        }
        echo " return common lib at label: ${NODE_LABELS}, ws: ${WORKSPACE}"
        return s_common[NODE_LABELS]
    }
    echo "cannot checkout on null LABEL or WORKSPACE"
    return null;
}
*/

def  loadLibrary(){
    library identifier: "${NODE_LABELS}@", retriever: modernSCM(scm: [$class: 'SubversionSCMSource', credentialsId: '4eaa4918-7fd4-4338-becd-3c76a30aeed9', remoteBase: 'file:///D:/PROGRAMMING/Projects/RnD/svn/repo/trunk/', workspaceUpdater: [$class: 'CheckoutUpdater']], libraryPath: 'jenkins/')
}
pipeline {
    agent none
    stages{         
        stage("Build Flow"){
            agent {
                node {
                    label "slave1"
                    customWorkspace "D:\\test_jenkins"
                }
            }
            stages {
                stage('Hello') {
                    steps {
                        script {
                            
                         //   def s = checkout scm
                         //   echo "node name : ${NODE_LABELS} svn url : " + s.SVN_URL  
                         loadLibrary()
                         loadLibrary()
                            common.methodA() 
                            common.methodB()
                                  /*                   
                        // common.makedir("test\\test\\test")
                            // common.deepmkdir("D:\\test2a\\test2b\\test2c\\taklsdgj\\asldghjajsdh\\oiur\\qwer")
                            common.makedir("C:\\copy_share")
                            common.makedir("C:\\test2a_share")
                            common.xcopy("D:\\test2a\\*","C:\\test2a_share", "/Y /R /I /S /E");
                            common.xcopy("D:\\share\\*","C:\\copy_share", "/Y /R /I /S /E");
                            common.xcopy("D:\\bundletool.jar","C:\\");
                            common.xcopy("D:\\USAHA","C:\\");
                                */
                        }
                        
                    }
                }
            }
        }
    }
        
    
}