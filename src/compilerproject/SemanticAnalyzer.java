package compilerproject;

public class SemanticAnalyzer {

    private SymbolTable symbolTable;
    private int errorCount = 0;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /*
     * Check 1 - Variable declaration
     * Checks: unsupported type, duplicate declaration
     */
    public boolean declareVariable(String name, String type, String value) {
        // check supported types
        if (!type.equals("int") && !type.equals("string")) {
            System.out.println("Semantic Error: Unsupported type '" + type + 
                               "' for variable '" + name + "'");
            errorCount++;
            return false;
        }
        if (type.equals("int") && !value.matches("[0-9]+")) {
            System.out.println("Semantic Error: Type mismatch - variable '" + name + 
                               "' is int but assigned string value " + value);
            errorCount++;
            return false;
        }
        // check duplicate declaration
        if (symbolTable.contains(name)) {
            System.out.println("Semantic Error: Variable '" + name + 
                               "' already declared.");
            errorCount++;
            return false;
        }
        // all good - insert into symbol table
        symbolTable.insert(name, type, value);
        System.out.println("  [SA] Declared: " + name + " (" + type + ") = " + value);
        return true;
    }

    /*
     * Check 2 - Variable used before declaration
     * Called from Factor() when an ID is used in expression
     */
    public boolean checkVariableDeclared(String name) {
        if (!symbolTable.contains(name)) {
            System.out.println("Semantic Error: Variable '" + name + 
                               "' used before declaration.");
            errorCount++;
            return false;
        }
        return true;
    }

    /*
     * Check 3 - Assignment to undeclared variable
     * Called from Assignment() for left-hand side variable
     */
    public boolean checkAssignment(String name) {
        if (!symbolTable.contains(name)) {
            System.out.println("Semantic Error: Cannot assign to undeclared variable '" + 
                               name + "'");
            errorCount++;
            return false;
        }
        return true;
    }

    /*
     * Check 4 - Type mismatch
     * Checks if variable matches expected type
     */
    public boolean checkType(String name, String expectedType) {
        SymbolTable.SymbolInfo info = symbolTable.lookup(name);
        if (info == null) {
            System.out.println("Semantic Error: Variable '" + name + "' not found.");
            errorCount++;
            return false;
        }
        if (!info.type.equals(expectedType)) {
            System.out.println("Semantic Error: Type mismatch for '" + name + 
                               "'. Expected " + expectedType + 
                               " but found " + info.type);
            errorCount++;
            return false;
        }
        return true;
    }

    /*
     * Print summary of semantic analysis
     */
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
