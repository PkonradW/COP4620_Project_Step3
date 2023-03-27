/**
 * is a hashTable where each element has a name, type, and value.
 * @TODO: confirm this is an acceptable implementation
 */
import java.util.HashMap;
public class SymbolTable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public SymbolTable() {
        this.table = new HashMap<>();
    }

    private String name;
    public HashMap<String, Symbol> table;
}
