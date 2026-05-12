package compilerproject;

import java.util.ArrayList;

/**
 * IRGenerator - Milestone 2
 *
 * Generates Three-Address Code in Quadruple format.
 *
 * FIX: Added separate newLabel() method so loop labels (L1, L2)
 * never clash with temp variables (t1, t2).
 * Previously both used the same counter which caused bugs.
 */
public class IRGenerator {

    private ArrayList<Quadruple> quadruples;

    /* Separate counters for temps and labels */
    private int tempCount  = 0;
    private int labelCount = 0;

    public IRGenerator() {
        this.quadruples = new ArrayList<>();
    }

    /**
     * Add a new three-address instruction.
     */
    public void emit(String op, String arg1, String arg2, String result) {
        quadruples.add(new Quadruple(op, arg1, arg2, result));
    }

    /**
     * Generate a unique temporary variable: t1, t2, t3 ...
     * Used for expression results.
     */
    public String newTemp() {
        return "t" + (++tempCount);
    }

    /**
     * FIX: Generate a unique loop label: L1, L2, L3 ...
     * Separate from newTemp() so labels never collide with temps.
     * Used by LoopIf() for jump targets.
     */
    public String newLabel() {
        return "L" + (++labelCount);
    }

    /**
     * Print the full IR quadruple table.
     * Required by PDF: "Show the contents of the array on screen."
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
        System.out.println("==============================================");
        System.out.println("Total instructions: " + quadruples.size());
    }

    public ArrayList<Quadruple> getQuadruples() {
        return quadruples;
    }

    /* ======================== */
    /*   Quadruple inner class  */
    /* ======================== */
    public static class Quadruple {
        public String op, arg1, arg2, result;

        public Quadruple(String op, String arg1, String arg2, String result) {
            this.op     = (op     == null) ? "" : op;
            this.arg1   = (arg1   == null) ? "" : arg1;
            this.arg2   = (arg2   == null) ? "" : arg2;
            this.result = (result == null) ? "" : result;
        }
    }
}