import java.util.*;
import java.io.*;
/**
 * <p>extending the LittleBaseListener</p>
 * <li>enter program</li>
 * <li>enter string decl</li>
 * <li>ctx object has the good stuff
 * (nodes from current context, use to get to terminals with information)</li>
 * <li>can get info about children from parent if you need (probably bad)</li>
 * @TODO: <li>Figure out how to read {BEGIN} and {END} rules to push to and pop off stack properly</li>
 *        <li>create method to print all the symbols from all the tables in the order they were created</li>
 *
 */
public class SimpleTableBuilder extends LittleBaseListener{
    //ArrayList<HashMap<String, Symbol>> = new ArrayList;
    ArrayList<SymbolTable<String,Symbol>> tableList = new ArrayList<>();
    SymbolTable global = new SymbolTable();
    // scope stack used to keeping track of which table to add variables to
    // (only add symbols to the topmost table in the stack)
    Stack<SymbolTable> scopeStack = new Stack<>();
    public static int blockCounter;
    @Override public void enterProgram(LittleParser.ProgramContext ctx) {
        tableList.add(global);
        scopeStack.push(global);
        global.setName("GLOBAL");
        blockCounter = 0;
    }
    /*
        make new symbol tables for:
            if blocks
            while blocks
            function blocks
    */
    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        // make a name for the thing
        // make new symbol table
        // add to scope stack and TableList
    }

    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
        // pop off of scope stack
    }

    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        // make new symboltab
        // generate name
        // add to scopestack and tableList
    }

    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        // pop off scope stack
    }

    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        String name = ctx.id().IDENTIFIER().getText();
        SymbolTable thisTable = new SymbolTable();
        thisTable.setName(name);
        scopeStack.push(thisTable);
        tableList.add(thisTable);
    }
    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {

    }

    @Override public void enterString_decl(LittleParser.String_declContext ctx) {
        SymbolTable thisTab = scopeStack.pop(); // get current symbol table

        // pull values from tree
        String name = ctx.id().IDENTIFIER().getText();
        String type = "STRING";
        String value = ctx.str().STRINGLITERAL().getText();

        // insert new symbol into table and push it back onto the stack
        Symbol newSymbol = new Symbol(name, type, value);
        thisTab.put(name, newSymbol);
        scopeStack.push(thisTab);
    }


    /**
     * for naming tables with generated scope names (while loops, if statements)
     */
    public static void blockNamer(SymbolTable block){
        blockCounter++;
        block.setName("BLOCK" + (blockCounter));
    }

}


