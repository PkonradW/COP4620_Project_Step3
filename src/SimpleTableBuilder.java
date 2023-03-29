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
    public static ArrayList<SymbolTable> tableList = new ArrayList<>();
    public static SymbolTable global = new SymbolTable();
    // scope stack used to keeping track of which table to add variables to
    // (only add symbols to the topmost table in the stack)
    public static Stack<SymbolTable> scopeStack = new Stack<>();
    public static ArrayList<String> errors = new ArrayList<>();
    public static String currentVarType;
    public static int blockCounter;
    @Override public void enterProgram(LittleParser.ProgramContext ctx) {
        tableList.add(global);
        scopeStack.push(global);
        global.setName("GLOBAL");
        blockCounter = 0;
    }

    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        SymbolTable newTable = new SymbolTable();
        blockNamer(newTable);
        scopeStack.push(newTable);
        tableList.add(newTable);
    }

    @Override public void enterElse_part(LittleParser.Else_partContext ctx) {
        if (ctx.decl()!=null) {
            SymbolTable newTable = new SymbolTable();
            blockNamer(newTable);
            scopeStack.push(newTable);
            tableList.add(newTable);
        }
    }
    @Override public void exitElse_part(LittleParser.Else_partContext ctx) {
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
        SymbolTable thisTable = new SymbolTable();
        thisTable.setName(name);
        scopeStack.push(thisTable);
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
        if (!thisTab.table.containsKey(name)) {
            thisTab.table.put(name, newSymbol);
        } else {
            String error = "DECLARATION ERROR " + name;
            errors.add(error);
        }
    }

    @Override public void enterVar_decl(LittleParser.Var_declContext ctx) {
        // create symbol table entry for each variable declaration
        SymbolTable thisTable = scopeStack.peek();

        // getting var type is easy, needs to handle recursive declarations though
        String type = ctx.var_type().getText().toString();
        currentVarType = type;
        String name = (ctx.id_list().id().IDENTIFIER().getText());
        if (!thisTable.table.containsKey(name)) {
            thisTable.table.put(name, new Symbol(name, type, null));
        } else {
            String error = "DECLARATION ERROR " + name;
            errors.add(error);
        }
        // pass id_tail context to resursive decl
        if (ctx.id_list().id_tail().children!=null) {
            resursiveDecl(type, ctx.id_list().id_tail());
        }
    }

    /**
     * Recursive variable declarations stuff like:
     * <li>INT x, y, z;</li>
     * @param ctx id_tail context, confirmed not null by caller
     */
    public void resursiveDecl(String type, LittleParser.Id_tailContext ctx){
        String name = ctx.id().IDENTIFIER().getText();
        SymbolTable thisTable = scopeStack.peek();
        if (!thisTable.table.containsKey(name)) {
            thisTable.table.put(name, new Symbol(name, type, null));
        } else {
            String error = "DECLARATION ERROR " + name;
            errors.add(error);
        }
        if (ctx.id_tail().children != null) {
            resursiveDecl(type, ctx.id_tail());
        }
    }
    @Override public void exitVar_decl(LittleParser.Var_declContext ctx) {
        currentVarType = null;
    }

    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) {
        SymbolTable thisTable = scopeStack.peek();

        String name = ctx.id().IDENTIFIER().getText();
        String type = ctx.var_type().getText().toString();
        Symbol thisSymbol = new Symbol(name, type, null);

        if (!thisTable.table.containsKey(name)) {
            thisTable.table.put(name, thisSymbol);
        } else {
            String error = "DECLARATION ERROR " + name;
            errors.add(error);
        }
    }





    /**
     * for naming tables with generated scope names (while loops, if statements)
     */
    public static void blockNamer(SymbolTable block){
        blockCounter++;
        block.setName("BLOCK " + (blockCounter));
    }

    /*
    TODO make method that prints all the symbols from all of the tables
    within tableList
     */
//    public void prettyPrint() {
//        if (errors.isEmpty()) {
//            for (SymbolTable table : tableList) {
//                if (!table.getName().equals("GLOBAL")) {
//                    System.out.println();
//                }
//                System.out.println("Symbol table " + table.getName());
//                Symbol symbol;
//                Set<Map.Entry<String, Symbol>> symbolSet = table.table.entrySet();
//                Iterator<Map.Entry<String, Symbol>> i = symbolSet.iterator();
//                while (i.hasNext()) {
//                    Map.Entry<String, Symbol> entry = i.next();
//                    String key = entry.getKey();
//                    symbol = table.table.get(key);
//                    if (symbol != null) {
//                        if (symbol.getValue() != null) {
//                            System.out.println("name " + symbol.getName() + " type "
//                                    + symbol.getType() + " value " + symbol.getValue());
//                        } else {
//                            System.out.println("name " + symbol.getName() + " type "
//                                    + symbol.getType());
//                        }
//                    }
//                }
//            }
//        } else {
//            System.out.println(errors.get(0));
//        }


    public void prettyPrint() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("output.txt"));
            if (errors.isEmpty()) {
                for (SymbolTable table : tableList) {
                    if (!table.getName().equals("GLOBAL")) {
                        writer.println();
                    }
                    writer.println("Symbol table " + table.getName());
                    Symbol symbol;
                    Set symbolSet = table.table.entrySet();
                    Iterator i = symbolSet.iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        String key = (String) entry.getKey();
                        symbol = table.table.get(key);
                        if (symbol != null) {
                            if (symbol.getValue() != null) {
                                writer.println("name " + symbol.getName() + " type "
                                        + symbol.getType() + " value " + symbol.getValue());
                            } else {
                                writer.println("name " + symbol.getName() + " type "
                                        + symbol.getType());
                            }
                        }
                    }
                }
            } else { //endif
                writer.println(errors.get(0));
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
        }
    } // end prettyPrint

}


