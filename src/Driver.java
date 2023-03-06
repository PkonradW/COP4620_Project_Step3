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
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker () ;
        SimpleTableBuilder stb = new SimpleTableBuilder (); //Example
        //SymbolTableBuilder stb = new SymbolTableBuilder ();

        // Walk the tree created during the parse, trigger callbacks
        walker.walk(stb, tree);

        //stb.prettyPrint();

    }

}