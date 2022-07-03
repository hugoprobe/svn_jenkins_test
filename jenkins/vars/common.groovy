def makedir(String path) {
        if (!fileExists(path)) {                               
                bat 'mkdir "' + path +'"'                         
        }        
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

def xcopy(String src, String dst, String flag="")
{
        bat """xcopy "${src}" "${dst}" ${flag}"""       
}

def methodA(){
        bat  " echo Method AAAA is execute new"
}
def methodB(){
        bat  " echo Method BBB is execute"
}
return this