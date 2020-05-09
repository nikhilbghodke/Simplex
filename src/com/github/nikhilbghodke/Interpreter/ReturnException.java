package com.github.nikhilbghodke.Interpreter;

public class ReturnException extends Exception{
    int num;
    public ReturnException(int num){
        super(num+"");
        this.num=num;
    }
}
