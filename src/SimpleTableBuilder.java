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
    ArrayList<SymbolTable> tableList = new ArrayList<>();
    SymbolTable global = new SymbolTable();
    Stack<SymbolTable> scopeStack = new Stack<>();
    @Override public void enterProgram(LittleParser.ProgramContext ctx) {
        tableList.add(global);
        scopeStack.add(global);
    }
    @Override public void enterString_decl(LittleParser.String_declContext ctx) {
        SymbolTable thisTab = scopeStack.pop(); // get current symbol table

        // pull values from tree
        String name = ctx.id().getText();
        String type = "STRING";
        String value = ctx.str().STRINGLITERAL().getText();

        // insert new symbol into table and push it back onto the stack
        Symbol newSymbol = new Symbol(name, type, value);
        thisTab.put(name, newSymbol);
        scopeStack.push(thisTab);
    }

}
