package compilerproject;

import java.util.ArrayList;

public class IRGenerator {
    // Requirements: Must use an array (or ArrayList converted to array) to store instructions 
    private ArrayList<Quadruple> quadruples;
    private int tempCount = 0;

    public IRGenerator() {
        this.quadruples = new ArrayList<>();
    }

    /**
     * Adds a new 3-address instruction in Quadruple format.
     */
    public void emit(String op, String arg1, String arg2, String result) {
        quadruples.add(new Quadruple(op, arg1, arg2, result));
    }

    /**
     * Generates a unique temporary variable name (t1, t2, etc.)[cite: 31].
     */
    public String newTemp() {
        return "t" + (++tempCount);
    }

    /**
     * Requirement: Show the contents of the array on the screen.
     */
    public void printIR() {
        System.out.println("\n==============================================");
        System.out.println("      INTERMEDIATE REPRESENTATION (3AC)       ");
        System.out.println("==============================================");
        System.out.printf("%-5s | %-10s | %-10s | %-10s | %-10s%n", 
                          "#", "OP", "ARG1", "ARG2", "RESULT");
        System.out.println("----------------------------------------------");
        
        for (int i = 0; i < quadruples.size(); i++) {
            Quadruple q = quadruples.get(i);
            System.out.printf("%-5d | %-10s | %-10s | %-10s | %-10s%n", 
                              i, q.op, q.arg1, q.arg2, q.result);
        }
        System.out.println("==============================================\n");
    }

    public ArrayList<Quadruple> getQuadruples() {
        return quadruples;
    }

    /**
     * Quadruple structure representing (OP, ARG1, ARG2, RESULT).
     */
    public static class Quadruple {
        String op, arg1, arg2, result;

        public Quadruple(String op, String arg1, String arg2, String result) {
            this.op = (op == null) ? "" : op;
            this.arg1 = (arg1 == null) ? "" : arg1;
            this.arg2 = (arg2 == null) ? "" : arg2;
            this.result = (result == null) ? "" : result;
        }
    }
}