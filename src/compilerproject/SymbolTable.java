package compilerproject;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, SymbolInfo> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }
    public boolean insert(String name, String type, Object value) {
        table.put(name, new SymbolInfo(type, value));
        return true;
    }
    public SymbolInfo lookup(String name) {
        if (!table.containsKey(name)) {
            return null;  // just return null, no error print
        }
        return table.get(name);
    }
    public boolean contains(String name) {
        return table.containsKey(name);
    }
    public void updateValue(String name, Object newValue) {
        if (table.containsKey(name)) {
            table.get(name).value = newValue;
        }
    }
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