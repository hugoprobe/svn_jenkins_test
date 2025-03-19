def myParams = [:]

def get(String name){
    return myParams[name];
}

def set(String name, String value){
    myParam[name]=value
}

def activeChoice(Map it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [  $class: 'ChoiceParameter', 
              name: (it.name),  
              choiceType: (it.type),  
              description:(it?.desc?:''), 
              script: [ $class: 'GroovyScript', 
                        script: (setStrScript(it.script)), 
                        fallbackScript:(setStrScript('return "error script"'))
                      ]
           ]
}
def activeChoiceReactive(Map it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [  $class: 'CascadeChoiceParameter', 
              name: (it.name),  
              choiceType: (it.type), 
              description:(it?.desc?:''), 
              referencedParameters:(it?.references?:''),
              script: [ $class: 'GroovyScript', 
                        script: (setStrScript(it.script + '  // references : '+ (it?.references?:'')  )), 
                        fallbackScript:(setStrScript('return ["error script"]'))
                      ]
           ]
}
def activeChoiceReactiveReference(Map it){
    def setStrScript={str-> return [classpath: [], sandbox: true, script:(str)]}
    return [  $class: 'DynamicReferenceParameter', 
              name: (it.name),  
              choiceType: (it.type), 
              omitValueField:(it?.omit?:false), 
              description:(it?.desc?:''), 
              referencedParameters:(it?.references?:''),
              script: [ $class: 'GroovyScript', 
                        script: (setStrScript(it.script)), 
                        fallbackScript:(setStrScript('return "error script"'))
                      ]
           ]
}
