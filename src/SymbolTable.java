/**
 * is a hashTable where each element has a name, type, and value.
 * @TODO: confirm this is an acceptable implementation
 */
import java.util.Hashtable;
public class SymbolTable<String, Symbol> extends Hashtable{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}