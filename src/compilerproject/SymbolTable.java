package compilerproject;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, SymbolInfo> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }

    /*
     * insert - simply stores variable, no validation here
     * Validation is handled by SemanticAnalyzer
     */
    public boolean insert(String name, String type, Object value) {
        table.put(name, new SymbolInfo(type, value));
        return true;
    }

    /*
     * lookup - returns SymbolInfo if found, null if not found
     * No error printing here - SemanticAnalyzer handles errors
     */
    public SymbolInfo lookup(String name) {
        if (!table.containsKey(name)) {
            return null;  // just return null, no error print
        }
        return table.get(name);
    }

    /*
     * contains - simple check if variable exists
     */
    public boolean contains(String name) {
        return table.containsKey(name);
    }

    /*
     * updateValue - updates value of existing variable
     */
    public void updateValue(String name, Object newValue) {
        if (table.containsKey(name)) {
            table.get(name).value = newValue;
        }
    }

    /*
     * printTable - displays all variables in symbol table
     */
    public void printTable() {
        System.out.println("\n--- SYMBOL TABLE ---");
        System.out.println("Name       Type      Value");
        System.out.println("---------- --------- --------");
        for (String key : table.keySet()) {
            SymbolInfo info = table.get(key);
            System.out.println(key + "        " + info.type + "      " + info.value);
        }
    }

    public static class SymbolInfo {
        String type;
        Object value;

        public SymbolInfo(String type, Object value) {
            this.type = type;
            this.value = value;
        }
    }
}