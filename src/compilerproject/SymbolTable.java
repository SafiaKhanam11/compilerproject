package compilerproject;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    // Stores the lexeme (name) as the key and SymbolInfo as the value [cite: 23, 561]
    private HashMap<String, SymbolInfo> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }

    /**
     * Inserts a new variable into the table. 
     * Requirement: Must store exact lexeme, type, and value[cite: 23].
     */
    public boolean insert(String name, String type, Object value) {
        if (table.containsKey(name)) {
            return false; // Useful for SemanticAnalyser to detect duplicates [cite: 550]
        }
        table.put(name, new SymbolInfo(type, value));
        return true;
    }

    /**
     * Finds a variable in the table[cite: 553].
     */
    public SymbolInfo lookup(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    /**
     * Updates the value of an existing variable[cite: 561].
     */
    public void updateValue(String name, Object newValue) {
        if (table.containsKey(name)) {
            table.get(name).value = newValue;
        }
    }

    /**
     * Requirement: Show the contents of the symbol table.
     */
    public void printTable() {
        System.out.println("\n==============================================");
        System.out.println("              SYMBOL TABLE SUMMARY            ");
        System.out.println("==============================================");
        System.out.printf("%-15s | %-10s | %-15s%n", "Lexeme", "Type", "Value");
        System.out.println("----------------------------------------------");
        
        if (table.isEmpty()) {
            System.out.println("            (Table is currently empty)        ");
        } else {
            for (Map.Entry<String, SymbolInfo> entry : table.entrySet()) {
                String name = entry.getKey();
                SymbolInfo info = entry.getValue();
                System.out.printf("%-15s | %-10s | %-15s%n", 
                                  name, 
                                  info.type, 
                                  (info.value == null ? "null" : info.value.toString()));
            }
        }
        System.out.println("==============================================\n");
    }

    /**
     * Inner class to store type and value information[cite: 13, 23].
     */
    public static class SymbolInfo {
        String type; // Supports int, float, string, char 
        Object value;

        public SymbolInfo(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        public String getType() { return type; }
        public Object getValue() { return value; }
    }
}