

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
   // def c=checkout scm;
    library identifier: "common@", retriever: modernSCM(scm: [$class: 'SubversionSCMSource', credentialsId: '4eaa4918-7fd4-4338-becd-3c76a30aeed9', remoteBase: 'https://github.com/hugoprobe/svn_jenkins_test/trunk/jenkins', workspaceUpdater: [$class: 'CheckoutUpdater']])
}
pipeline {
    agent none
    stages{     
        stage("Checkout"){
            agent any            
            stages {
                stage('Hello') {
                    steps {
                        script {
                         //def c=checkout scm;
                        // echo "svn_url: " + String.valueOf(env.SVN_URL)
                         loadLibrary()
                       
                        }
                        
                    }
                }
            }
        }   
        /*
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
                            def c=checkout scm;
                            echo "test: " 
                          //  echo "scm url: "  + scm
                         //   def s = checkout scm
                         //   echo "node name : ${NODE_LABELS} svn url : " + s.SVN_URL  
                         //loadLibrary()
                         //loadLibrary()
                          //  common.methodA() 
                           // common.methodB()
                                                     
           
                                
                        }
                        
                    }
                }
            }
        }*/
    }
        
    
}
