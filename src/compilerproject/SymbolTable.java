package compilerproject;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, SymbolInfo> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }

    public boolean insert(String name, String type, Object value) {
        if (!type.equals("int") && !type.equals("string")) {
            System.out.println("Semantic Error: Unsupported type '" + type + "'");
            return false;
        }
        if (table.containsKey(name)) {
            System.out.println("Semantic Error: Variable '" + name + "' already declared.");
            return false;
        }
        table.put(name, new SymbolInfo(type, value));
        return true;
    }

    public SymbolInfo lookup(String name) {
        if (!table.containsKey(name)) {
            System.out.println("Semantic Error: Variable '" + name + "' used before declaration.");
            return null;
        }
        return table.get(name);
    }

    public void updateValue(String name, Object newValue) {
        if (table.containsKey(name)) {
            table.get(name).value = newValue;
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
    public void printTable() {
        System.out.println("\n--- SYMBOL TABLE ---");
        System.out.println("Name       Type      Value");
        System.out.println("---------- --------- --------");
        for (String key : table.keySet()) {
            SymbolInfo info = table.get(key);
            System.out.println(key + "        " + info.type + "      " + info.value);
        }
    }
}