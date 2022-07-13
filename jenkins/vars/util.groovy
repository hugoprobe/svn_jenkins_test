def mkdir(String path) {
        bat 'if NOT EXIST ' + path + ' mkdir '+ path        
}

def deepmkdir(String path)
{       
        int lastSlashIdx = path.lastIndexOf("\\")
        def parentDir=path.substring(0, lastSlashIdx)
        if ( !(fileExists(path) || ( lastSlashIdx==1 && path[0]==path[1]) || path[lastSlashIdx-1]==':') ){                
                deepmkdir(parentDir)
        }
        makedir(path)
}

//For getting environment variables via batch
def getBatchEnv(String strScript="", List<String> args)
{
    def result=[:]
    args="%"+args.join("%:%")+"%:null"
    echo "args $args"
    def getVariables  = ''' @echo off
                        ''' + strScript + '''
                            echo ''' + args
    def vars = bat(script:getVariables, returnStdout:true)
    def var_array = vars.trim().split(":")
    for(int i=0; i<args.size();i++)
        result[args[i]]=var_array[i]
    return result 
}
