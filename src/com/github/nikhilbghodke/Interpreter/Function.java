package com.github.nikhilbghodke.Interpreter;

import com.github.nikhilbghodke.parser.AST_Node;

import java.util.List;

public class Function {
    public AST_Node fbody;
    public List<Variable> fparam;
    public Environment env;

    public Function(AST_Node fbody, List<Variable> fparam, Environment env) {
        this.fbody = fbody;
        this.fparam = fparam;
        this.env=env;
    }
}
