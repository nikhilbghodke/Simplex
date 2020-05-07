package com.github.nikhilbghodke.Interpreter;

import java.util.HashMap;

public class Environment {
    public HashMap<String,Variable> variables;
    public HashMap<String,Function> functions;

    public void addVariable(String name,Variable a){
       variables.put(name,a);
    }
    public void addFunction(String name, Function function){
        functions.put(name,function);
    }

    public Variable variableLookup(String name){
        return variables.getOrDefault(name,null);
    }

    public Function functionLookUp(String name){
        return functions.getOrDefault(name, null);
    }
}
