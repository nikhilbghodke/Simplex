package com.github.nikhilbghodke.Interpreter;

import java.util.HashMap;

public class Environment {
    public HashMap<String,Variable> variables;
    public HashMap<String,Function> functions;
    public Environment parent;

    public Environment(Environment parent) {
        this.parent = parent;
        this.functions= new HashMap<>();
        this.variables= new HashMap<>();
    }

    public void addVariable(String name, Variable a){
       variables.put(name,a);
    }
    public void addFunction(String name, Function function){
        functions.put(name,function);
    }

    public void updateVariable(String name, Variable a) throws Exception {
        if(variables.containsKey(name))
            variables.put(name,a);
        else if(parent==null)
            throw new Exception("No such variable "+name+" found");
        else
            parent.updateVariable(name,a);
    }

    public Variable variableLookUp(String name) throws Exception {
        if(variables.containsKey(name))
            return variables.get(name);
        if(parent==null)
            throw new Exception("No such variable "+name+" found");
        return parent.variableLookUp(name);
    }

    public Function functionLookUp(String name) throws Exception {
        if(functions.containsKey(name))
            return functions.get(name);
        if(parent==null)
            throw new Exception("No such function "+name+" found");
        return parent.functionLookUp(name);
    }
}
