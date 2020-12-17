package com.github.sunitapt.Interpreter;

public class ReturnException extends Exception{
    int num;
    public ReturnException(int num){
        super(num+"");
        this.num=num;
    }
}
