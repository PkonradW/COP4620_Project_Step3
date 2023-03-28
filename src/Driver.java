import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;

public class Driver {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        LittleLexer lexer = new LittleLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LittleParser parser = new LittleParser(tokens);
        parser.setBuildParseTree(true);

        parser.removeErrorListeners();
        myListener listener = new myListener();
        parser.addErrorListener(listener);
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker () ;
        SimpleTableBuilder stb = new SimpleTableBuilder ();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(stb, tree);
        if (listener.getIsEmpty()){
            stb.prettyPrint();
        } else {
            System.out.println(listener.stack);
        }

    }
    public static class myListener extends BaseErrorListener {
        List<String> stack = new ArrayList<>();
        @Override

        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e)
        {
            this.stack = ((Parser)recognizer).getRuleInvocationStack(); Collections.reverse(stack);
           /* if (!stack.isEmpty()) {
                System.out.println("unacceptable");
            } else {
                System.out.println("acceptable");
            }*/
        }
        public boolean getIsEmpty(){
            if (this.stack.isEmpty()){
                return true;
            }
            return false;
        }
    }

}