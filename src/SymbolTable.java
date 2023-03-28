/**
 * is a hashTable where each element has a name, type, and value.
 * @TODO: confirm this is an acceptable implementation
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class SymbolTable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public SymbolTable() {
        this.table = new LinkedHashMap<String, Symbol>();
    }

    private String name;
    public LinkedHashMap<String, Symbol> table;

}
