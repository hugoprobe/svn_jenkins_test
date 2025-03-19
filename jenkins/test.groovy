
NODES=[:]

pipeline {
    agent none
    environment{
        ELIGIBLE_NODES          ='JOGBLD0006,JOGBLD0010,JOGBLD0020,JOGBLD0021'
        PUBLISH_DIRECTORIES ='A,B'
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

                    util.getChangeString()
                }
            }
        }
        /*
        stage("Setup-Parameters")
        {
            steps {
                script{
                
                    getNodeHTML={ default_node, trunkdir ->
                        return '''
                                def     trunkdir    ="'''+trunkdir+'''", default_node="''' + default_node +'''", nodes_options= ["''' + ELIGIBLE_NODES.replace(",",'","') + '''"]
                                def     html        ="""  <select name="value" >""" + ([default_node]+ (nodes_options-default_node)).collect{ it -> return """<option value=$it>$it """}.join(" ") + """</select>"""
                                return  (html       += """ <input type="text" name="value"  style="width:75%" value="$trunkdir" >""")
                            '''
                    }
                    
                    properties([
                        parameters([
                            param.activeChoice                  ([  name:("BuildEnvironment"), type:'PT_RADIO', script:'''return ['DailyBuild:selected', 'QA-Release']''', desc:'Please choose your preferred build. It will automatically configures some parameters below.']),
                            param.activeChoice                  ([  name:("BuildVariant"), type:'PT_CHECKBOX', script:'''return ['ETC:selected', 'ASTC:selected', 'FBCL:selected']''']),
                            param.activeChoiceReactiveReference ([  name:("Node_ETC") , type:'ET_FORMATTED_HTML', script:getNodeHTML('JOGBLD0012',$/C:\\Project\\DMK\\etc/$ )]),
                            param.activeChoiceReactiveReference ([  name:("Node_ASTC"), type:'ET_FORMATTED_HTML', script:getNodeHTML('JOGBLD0032',$/D:\\Project\\DMK\\astc/$)]),
                            
                            param.activeChoiceReactive          ([  name:("BuildFlow"), type:'PT_CHECKBOX', references:'BuildEnvironment', 
                                                                    script:'''  
                                                                                def OrderedFlow=['CleanUpdate', 'ApplyPatches', 'MakeData','PackData','Backup','Publish','UploadCrashlytics'].collectEntries{[it, ":selected"]}
                                                                                if(BuildEnvironment.contains('DailyBuild')) {
                                                                                    OrderedFlow['Publish']=""
                                                                                }
                                                                                return OrderedFlow.collect{k,v -> "$k$v"}''' ]),

                            param.activeChoiceReactiveReference ([  name:("SVN_Revision"), type:'ET_FORMATTED_HTML', references:'BuildFlow', 
                                                                    script:'''  return """<input type="text" name="value"  style="width:30%" value="head" >"""''' ]),
                                                                                
                        ])
                    ])
                 
                }
            }
        }
        stage("Nodes Allocation & Verification")
        {
            steps {
                script{
                    ['ETC','ASTC'].each{ var-> def label=null, workdir=null
                        if(params.BuildVariant.contains(var)){
                            try{
                                label   = params["Node_${var}"].split(",")[0]  
                                workdir = params["Node_${var}"].split(",")[1]
                            }catch(Exception e){
                                //echo "Error:"
                                echo "label: $label"
                                echo "workdir: $workdir"
                            }

                        }
                        NODES[var]   = [ label:(label), workdir:(workdir)]
                        
                    }
                }
                
            }
        }
        stage("Multi-Config Build")
        {
            matrix {
                when { 
                    beforeAgent true
                    expression { params.BuildVariant.contains(Variant) } 
                }              
                agent {
                    node {
                      label            params["Node_${Variant}"].split(",")[0] 
                      customWorkspace   params["Node_${Variant}"].split(",")[1] 
                    }
                }
                axes {
                    axis {
                        name 'Variant'
                        values 'ETC', 'ASTC'
                    }
                }
                
                stages {
                    stage("*")
                    {
                        steps{ 
                            script{
                                
                                    echo "Checked ${Variant}"
                                    
                                
                            }
                        }
                    }
        
                    
                }
                
            }
        }*/
    }
}
