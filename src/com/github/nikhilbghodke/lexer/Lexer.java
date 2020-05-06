package com.github.nikhilbghodke.lexer;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public List<List<String>> tokens;
    public List<String> precedence;
    public HashMap<String,Pattern> rules;
    public String content;
    public Lexer(String fileName, String grammarFile) throws Exception {
        tokens= new ArrayList<>();
        rules= new HashMap<>();
        precedence= new ArrayList<>();
        content=readFileAsString(fileName);
        content=removeHTMLComments(content).trim();
        loadRules(grammarFile);
    }
    public String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public void lex() throws Exception {
        String temp=content;

        while(!temp.isEmpty()){
            temp=temp.trim();

            for(String terminal:precedence){
                Pattern regex=rules.get(terminal);
                String name=terminal;
                Matcher a=regex.matcher(temp);
                if(a.find()){
                    String match=a.group();
                    ArrayList<String> list= new ArrayList<>();
                    list.add(name);
                    list.add(match);
                    tokens.add(list);
                    temp=temp.substring(match.length());
                    break;
                }
            }

        }

        System.out.println(rules);
        System.out.println(tokens);
    }

    public String removeHTMLComments(@NotNull String s){
        return s.replaceAll("<!--.*?-->","");
    }

    public void loadRules(String grammarFile) throws Exception {
        String temp= readFileAsString(grammarFile);
        String[] lines= temp.split("\\n");
        for(String s:lines){
            int index=s.indexOf('=');
            String key=s.substring(0,index).trim();
            String value=s.substring(index+1).trim();
            precedence.add(key);
            try {
                rules.put(key, Pattern.compile("^(" + value + ")"));
            }
            catch (Exception e){
                throw new Exception(s+" regular expression cannot be compiled and hence PatternSyntaxException error is thrown");
            }
        }

    }
    public int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
}
