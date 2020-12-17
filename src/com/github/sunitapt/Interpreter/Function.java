package com.github.sunitapt.Interpreter;

import com.github.sunitapt.parser.AST_Node;

import java.util.List;

public class Function {
    public AST_Node fbody;
    public List<String> fparam;
    public Environment env;

    public Function(AST_Node fbody, List<String> fparam, Environment env) {
        this.fbody = fbody;
        this.fparam = fparam;
        this.env=env;
    }
}
