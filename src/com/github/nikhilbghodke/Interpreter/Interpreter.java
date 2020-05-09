package com.github.nikhilbghodke.Interpreter;

import com.github.nikhilbghodke.parser.AST_Node;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {
    public AST_Node tree;
    public Environment global;

    public Interpreter(AST_Node tree){
        this.global=new Environment(null);
        this.tree=tree;
    }

    public void eval() throws Exception {

        eval(tree,global);

    }
    public void eval(AST_Node tree,Environment env) throws Exception {
        List<AST_Node> children= tree.children;
        for(AST_Node child:children){
            if(child.name.equals("epsilon"))
                continue;
            else if(child.name.equals("assign")){
                int a=eval_exp(child.children.get(3), env);
                env.addVariable(child.children.get(1).value,new Variable("number",a+""));
            }
            else if(child.name.equals("update")){
                int a=eval_exp(child.children.get(2), env);
                env.updateVariable(child.children.get(0).value,new Variable("number",a+""));
            }
            else if(child.name.equals("if_else_statement")){
                boolean condition= eval_condition(child.children.get(2),env);
                if(condition){
                    Environment new_env=new Environment(env);
                    eval(child.children.get(5),new_env);
                }
                else
                {
                    eval(child.children.get(9),env);
                }
            }
            else if(child.name.equals("if_statement")){
                boolean condition= eval_condition(child.children.get(2),env);
                if(condition){
                    eval(child.children.get(5),env);
                }
            }
            else if(child.name.equals("while_statement")){
                boolean condition= eval_condition(child.children.get(2),env);
                while(condition){
                    Environment new_env=new Environment(env);
                    eval(child.children.get(5),new_env);
                    condition= eval_condition(child.children.get(2),env);
                }
            }
            else if(child.name.equals("function_definition")){
                List<String> list= eval_functional_definition_optional_params(child.children.get(3),env);
                AST_Node fbody= child.children.get(6);
                env.addFunction(child.children.get(1).value,new Function(fbody,list,env));
            }
            else if(child.name.equals("function_call")){
                eval_function_call(child,env);
            }
            else if(child.name.equals("return_statement")){
                if(env.parent==null)
                    throw new Exception("invalid return statement in global Environment");
                if(child.children.size()==2){
                    env.parent.addVariable("function output", new Variable("number",""));
                }
                else{
                    int a=eval_exp(child.children.get(1),env);
                    env.parent.addVariable("function output", new Variable("number",a+""));
                }
                throw new Exception("");

            }
            else if(child.name.equals("statement"))
                eval(child,env);
        }
    }
    public int eval_exp(AST_Node tree, Environment env) throws Exception {
        //System.out.println("exp "+tree);
        if(tree.children.size()==3){
            AST_Node operator=tree.children.get(1);
            int a=eval_exp_1(tree.children.get(0),env);
            int b=eval_exp(tree.children.get(2),env);
            if(operator.name.equals("add")){
                return a+b;
            }
            else if(operator.name.equals("sub")) {
                return a-b;
            }
        }
        return eval_exp_1(tree.children.get(0),env);
    }

    private int eval_exp_1(AST_Node tree, Environment env) throws Exception {
        //System.out.println("exp_1 "+tree);
        if(tree.children.size()==3){
            AST_Node operator=tree.children.get(1);
            int a=eval_exp_2(tree.children.get(0),env);
            int b=eval_exp_1(tree.children.get(2),env);
            if(operator.name.equals("mul")){
                return a*b;
            }
            else if(operator.name.equals("divide")) {
                //check if b is zero
                return a/b;
            }
            else if(operator.name.equals("modulo"))
                return a%b;
        }
        return eval_exp_2(tree.children.get(0),env);
    }

    private int eval_exp_2(AST_Node tree, Environment env) throws Exception {
       // System.out.println("exp_2 "+tree);
        if(tree.children.size()==2){
            AST_Node operator=tree.children.get(0);
            int a=eval_exp_3(tree.children.get(1),env);
            if(operator.name.equals("increment")){
                return ++a;
            }
            else if(operator.name.equals("decrement")) {
                //check if b is zero
                return --a;
            }
            else if(operator.name.equals("add"))
                return (+1)*a;
            else if(operator.name.equals("sub"))
                return (-1)*a;
        }
        return eval_exp_3(tree.children.get(0),env);
    }

    private int eval_exp_3(AST_Node tree, Environment env) throws Exception {
        if(tree.children.size()==2){
            AST_Node operator=tree.children.get(1);
            int a=eval_exp_4(tree.children.get(0),env);
            if(operator.name.equals("increment")){
                return ++a;
            }
            else if(operator.name.equals("decrement")) {
                //check if b is zero
                return --a;
            }

        }
        return eval_exp_4(tree.children.get(0),env);
    }

    private int eval_exp_4(AST_Node tree, Environment env) throws Exception {
        //System.out.println(tree);
        AST_Node node=tree.children.get(0);
        if(node.name.equals("identifier"))
        {
            Variable a=env.variableLookUp(node.value);
            if(!a.dataType.equals("number"))
                throw new Exception(node.value+" is not a number");
            return Integer.parseInt(a.value);
        }
        else if(node.name.equals("function_call")){
            eval_function_call(node, env);
            String ans=env.variableLookUp("function output").value;
            if(ans.equals(""))
                throw new Exception(" function does return anything");
            return Integer.parseInt(ans);
        }
        else{
            return number(node, env);
        }
    }
    private int number(AST_Node tree, Environment env){
        return Integer.parseInt(tree.value);
    }
    private boolean eval_condition(AST_Node tree, Environment env) throws Exception {
        int a = eval_exp(tree.children.get(0),env);
        int b = eval_exp(tree.children.get(2),env);
        return eval_comparator(tree.children.get(1),a,b);

    }

    private boolean eval_comparator(AST_Node tree,int a, int b) {
        tree=tree.children.get(0);
        if(tree.name.equals("equals"))
            return a==b;
        else if(tree.name.equals("greaterThanEqual"))
            return a>=b;
        else if(tree.name.equals("lesserThanEqual"))
            return a<=b;
        else if(tree.name.equals("lesserThan"))
            return a<b;
        else if(tree.name.equals("greaterThan"))
            return a>b;
        //unknown operator
        return true;
    }
    private List<Variable> eval_optional_params(AST_Node tree, Environment env) throws Exception {
        if(tree.children.size()==1){
          return new ArrayList<>();
        }
        else{
            List<Variable> ans= eval_params(tree.children.get(1),env);
            int a= eval_exp(tree.children.get(0),env);
            ans.add(0,new Variable("number",a+""));
            return ans;
        }
    }
    private List<Variable> eval_params(AST_Node tree, Environment env) throws Exception {

        if(tree.children.size()==1){
            return new ArrayList<>();
        }
        else{
            List<Variable> ans= eval_params(tree.children.get(2),env);
            int a= eval_exp(tree.children.get(1),env);
            ans.add(0,new Variable("number",a+""));
            return ans;
        }

    }
    private List<String>   eval_functional_definition_optional_params(AST_Node tree, Environment env){
        if(tree.children.size()==1)
        {
            return new ArrayList<>();
        }
        else{
            List<String> ans=eval_functional_definition_params(tree.children.get(1),env);
            ans.add(0,tree.children.get(0).value);
            return ans;
        }
    }

    private List<String> eval_functional_definition_params(AST_Node tree, Environment env) {
        if(tree.children.size()==1){
            return new ArrayList<>();
        }
        else{
            List<String> ans=eval_functional_definition_params(tree.children.get(2),env);
            ans.add(0,tree.children.get(1).value);
            return ans;
        }
    }
    public void eval_function_call(AST_Node tree, Environment env) throws Exception {
        Function function=env.functionLookUp(tree.children.get(0).value);
        List<Variable> params=eval_optional_params(tree.children.get(2),env);
        if(params.size()!=function.fparam.size()){
            throw new Exception(tree.children.get(0).name+" expects "+function.fparam.size()+" number of parameters but "+params.size()+" number of paramaters are be passed");
        }
        Environment new_env=new Environment(function.env);
        for(int i=0;i<function.fparam.size();i++){
            new_env.addVariable(function.fparam.get(i),params.get(i));
        }
        try {
            eval(function.fbody, new_env);
        }
        catch(Exception e){

        }
    }
}
