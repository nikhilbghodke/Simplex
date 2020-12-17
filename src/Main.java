import com.github.sunitapt.Interpreter.Interpreter;
import com.github.sunitapt.lexer.Lexer;
import com.github.sunitapt.parser.AST_Node;
import com.github.sunitapt.parser.Parser;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        Lexer lex= new Lexer("./trial.js","./JavascriptLexer.def");
        lex.lex();

        Parser parser= new Parser(lex, "./ParserGrammar.def");
        AST_Node tree=parser.parse("statement");
        System.out.println(tree);

        Interpreter in=new Interpreter(tree);
        in.eval();
    }
}
