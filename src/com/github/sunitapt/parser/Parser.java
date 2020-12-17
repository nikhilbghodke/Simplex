package com.github.sunitapt.parser;

import com.github.sunitapt.lexer.Lexer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parser {
    public Lexer lexer;
    public HashMap<String,List<List<String>>> rules;
    HashSet<String> terminals;
    HashSet<String> nonterminals;
    public Parser(Lexer lexer,String grammarFile) throws Exception {
        this.lexer=lexer;
        this.rules= new HashMap<>();
        this.nonterminals= new HashSet<>();
        loadRules(grammarFile);
        System.out.println(rules);
        this.terminals= new HashSet<>(lexer.rules.keySet());
        terminals.add("epsilon");
        System.out.println(rules);
    }
    public String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
    public void loadRules(String grammarFile) throws Exception {
        String temp= readFileAsString(grammarFile);
        System.out.println(temp);
        String[] lines= temp.split("\\n");
        for(String s:lines){
            s=s.trim();
            // when the there is a empty line
            if(s.length()==0)
                continue;
            if(s.charAt(0)=='#')
                continue;
            int index=s.indexOf('=');
            String key=s.substring(0,index).trim();
            nonterminals.add(key);
            List<List<String>> cur= rules.getOrDefault(key, new ArrayList<>());
            ArrayList<String> list= new ArrayList<>();
            cur.add(list);
            rules.put(key,cur);
            String[] values=s.substring(index+1).trim().split("\\s");
            for(String value:values) {
                value=value.trim();
                if(value.length()!=0)
                list.add(value);
            }
        }


    }
    public AST_Node parse(String start) throws Exception {
        List<List<String>> copy= new ArrayList<>(lexer.tokens);
        AST_Node ans=solve(copy,start);
        if(!copy.isEmpty())
            throw new Exception("Compilation error");
        return ans;
    }

    public AST_Node solve(List<List<String>> tokens, String start) throws Exception {
        AST_Node node = null;
        //System.out.println(tokens+" "+start);
        List<AST_Node> children = new ArrayList<>();
        if (!nonterminals.contains(start))
                throw new Exception(start + " is not in nonterminal set -->" + nonterminals);
        List<List<String>> productions = rules.get(start);
        for (List<String> production : productions) {
            List<List<String>> remTokens=new ArrayList<>(tokens);
            ArrayList<AST_Node> tempChildren= new ArrayList<>();
            boolean broken=false;
            System.out.println(production);
            for (String cur : production) {

                if (terminals.contains(cur)) {
                    if(cur.equals("epsilon")) {
                        tempChildren.add(new AST_Node("epsilon",""));
                        continue;
                    }
                    if(remTokens.isEmpty()) {
                        broken=true;
                        break;
                    }
                    String tokenType=remTokens.get(0).get(0);
                    String token=remTokens.get(0).get(1);
                    remTokens.remove(0);
                    //System.out.println(cur+" "+tokenType);
                    if(!tokenType.equals(cur)) {
                        broken=true;
                        break;
                    }
                    AST_Node leaf= new AST_Node(tokenType,token);
                    tempChildren.add(leaf);

                } else if (nonterminals.contains(cur)) {
                    AST_Node child=solve(remTokens,cur);
                    if(child==null)
                    {
                        System.out.println("breaking out");
                        broken=true;
                        break;
                    }
                    tempChildren.add(child);
                } else
                    throw new Exception(cur + "in the rules " + start + " --> " + production + "  is neither a terminal or non terminal");
            }
            if(!broken){
                //mil gaya
                //remove from tokens
                int tokensremoved=tokens.size()-remTokens.size();
                for(int i=0;i<tokensremoved;i++)
                    tokens.remove(0);
                node= new AST_Node(start,"");
                System.out.println(start+"    matched"+tokens);
                node.children=tempChildren;
                return node;
            }
        }
        return node;
    }
}
