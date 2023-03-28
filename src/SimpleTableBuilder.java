import java.util.*;
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
    public static ArrayList<SymbolTable> tableList = new ArrayList<>();
    public static SymbolTable global = new SymbolTable();
    // scope stack used to keeping track of which table to add variables to
    // (only add symbols to the topmost table in the stack)
    public static Stack<SymbolTable> scopeStack = new Stack<>();
    public static String currentVarType;
    public static int blockCounter;
    @Override public void enterProgram(LittleParser.ProgramContext ctx) {
        tableList.add(global);
        scopeStack.push(global);
        global.setName("GLOBAL");
        //System.out.println(scopeStack.peek().getName());
        blockCounter = 0;
    }
    /*
        make new symbol tables for:
            if blocks
            while blocks
            function blocks
    */
    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        SymbolTable newTable = new SymbolTable();
        blockNamer(newTable);
        scopeStack.push(newTable);
        tableList.add(newTable);
    }

    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
        // pop off of scope stack
        scopeStack.pop();
    }

    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        // make new symboltab
        SymbolTable newTable = new SymbolTable();
        // generate name
        blockNamer(newTable);
        // add to scopestack and tableList
        scopeStack.push(newTable);
        tableList.add(newTable);
    }

    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        // pop off scope stack
        scopeStack.pop();
    }

    /**
     *
     * @param ctx the parse tree
     */
    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        String name = ctx.id().IDENTIFIER().getText();
        // System.out.println(name);
        SymbolTable thisTable = new SymbolTable();
        thisTable.setName(name);
        scopeStack.push(thisTable);
        //System.out.println(scopeStack.peek().getName());
        tableList.add(thisTable);
    }
    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        scopeStack.pop();
    }

    @Override public void enterString_decl(LittleParser.String_declContext ctx) {
        SymbolTable thisTab = scopeStack.peek(); // get current symbol table

        // pull values from tree
        String name = ctx.id().IDENTIFIER().getText();
        String type = "STRING";
        String value = ctx.str().STRINGLITERAL().getText();

        // insert new symbol into table and push it back onto the stack
        Symbol newSymbol = new Symbol(name, type, value);
        // System.out.println(thisTab.getName());
        thisTab.table.put(name, newSymbol);
    }

    @Override public void enterVar_decl(LittleParser.Var_declContext ctx) {
        // create symbol table entry for each variable declaration
        SymbolTable thisTable = scopeStack.peek();

        // getting var type is easy, needs to handle recursive declarations though
        String type = ctx.var_type().getText().toString();
        currentVarType = type;
        String name = (ctx.id_list().id().IDENTIFIER().getText());
        thisTable.table.put(name, new Symbol(name, type, null));
    }
    @Override public void exitVar_decl(LittleParser.Var_declContext ctx) {
        currentVarType = null;
    }
    @Override public void enterId_tail(LittleParser.Id_tailContext ctx) {
        SymbolTable thisTable = scopeStack.peek();
        if (ctx.id()!=null) {
            String name = ctx.id().IDENTIFIER().getText();
            String type = currentVarType;
            thisTable.table.put(name, new Symbol(name, type, null));
        }
    }


    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) {
        SymbolTable thisTable = scopeStack.peek();

        String name = ctx.id().IDENTIFIER().getText();
        String type = ctx.var_type().getText().toString();
        Symbol thisSymbol = new Symbol(name, type, null);

        thisTable.table.put(name, thisSymbol);
        System.out.println(name + " " + type + thisTable.getName());
    }





    /**
     * for naming tables with generated scope names (while loops, if statements)
     */
    public static void blockNamer(SymbolTable block){
        blockCounter++;
        block.setName("BLOCK" + (blockCounter));
    }

    /*
    TODO make method that prints all the symbols from all of the tables
    within tableList
     */
    public static void prettyPrint(){
        for (SymbolTable table : tableList){
            System.out.println("Symbol table " + table.getName());
            for (String name : table.table.keySet()) {
                Symbol sybil = table.table.get(name);
                if (sybil.getValue()==null) {
                    System.out.println("name " + name
                                     + " type " + sybil.getType()
                    );
                } else {
                    System.out.println("name " + name
                            + " type " + sybil.getType()
                            + " value " + sybil.getValue()
                    );
                }
            }
            System.out.println();
        }
        /*
        for each symbolTable in tableList:
            print "Symbol table <table name>
            for each symbol in the table:
                print "name <name> type <type> value <value>"
                   OR "name <name> type <type>"


        make sure to print newlines between different symbol tables
         */
    }

}


