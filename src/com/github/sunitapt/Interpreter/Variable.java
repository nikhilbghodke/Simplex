package com.github.sunitapt.Interpreter;

public class Variable {
    public String dataType;
    public String value;

    public Variable(String dataType, String value) {
        this.dataType = dataType;
        this.value = value;
    }
    public String toString(){
        return "(data Type:"+dataType+" value:"+value+")";
    }
}
