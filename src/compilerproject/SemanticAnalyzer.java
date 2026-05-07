package compilerproject;

public class SemanticAnalyzer {

    private SymbolTable symbolTable;
    private int errorCount = 0;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Requirement: Check if variable type is supported (int, float, string, or char).
     * Requirement: Match assigned value to declared type.
     */
    public boolean declareVariable(String name, String type, String value) {
        // 1. Check for Unsupported Types (Now including float and char) 
        if (!type.equals("int") && !type.equals("string") && 
            !type.equals("float") && !type.equals("char")) {
            reportError("Unsupported type '" + type + "' for variable '" + name + "'");
            return false;
        }

        // 2. Check for Type Mismatches 
        if (type.equals("int") && !value.matches("[0-9]+")) {
            reportError("Type mismatch: '" + name + "' is int but assigned " + value);
            return false;
        } 
        else if (type.equals("float") && !value.matches("[0-9]*\\.[0-9]+")) {
            reportError("Type mismatch: '" + name + "' is float but assigned " + value);
            return false;
        } 
        else if (type.equals("char") && !value.matches("'.{1}'")) {
            reportError("Type mismatch: '" + name + "' is char but assigned " + value);
            return false;
        }

        // 3. Check for Duplicate Declarations [cite: 550]
        if (symbolTable.contains(name)) {
            reportError("Variable '" + name + "' already declared.");
            return false;
        }
       
        symbolTable.insert(name, type, value);
        System.out.println("  [SA] Declared: " + name + " (" + type + ") = " + value);
        return true;
    }

    /**
     * Requirement: Verify variable is declared before usage[cite: 553].
     */
    public boolean checkVariableDeclared(String name) {
        if (!symbolTable.contains(name)) {
            reportError("Variable '" + name + "' used before declaration.");
            return false;
        }
        return true;
    }

    /**
     * Requirement: Verify assignment targets are declared[cite: 557].
     */
    public boolean checkAssignment(String name) {
        if (!symbolTable.contains(name)) {
            reportError("Cannot assign to undeclared variable '" + name + "'");
            return false;
        }
        return true;
    }
    
    private void reportError(String message) {
        System.out.println("Semantic Error: " + message);
        errorCount++;
    }

    public void printSummary() {
        System.out.println("\n--- SEMANTIC ANALYSIS SUMMARY ---");
        if (errorCount == 0) {
            System.out.println("No semantic errors found.");
        } else {
            System.out.println("Total semantic errors found: " + errorCount);
        }
        symbolTable.printTable();
    }
}