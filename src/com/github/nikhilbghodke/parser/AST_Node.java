package com.github.nikhilbghodke.parser;

import java.util.ArrayList;

public class AST_Node {
    public String name;
    public String value;
    public ArrayList<AST_Node> children;
    public AST_Node(String name, String value){
        children= new ArrayList<>();
        this.name=name;
        this.value=value;
    }

    @Override
    public String toString() {
        return "AST_Node{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", children=" + children +
                '}';
    }
}
