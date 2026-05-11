package compilerproject;

import java.util.ArrayList;

public class Optimizer {

    private ArrayList<IRGenerator.Quadruple> original;
    private ArrayList<IRGenerator.Quadruple> optimized;

    /**
     * Constructor - receives the instruction list from IRGenerator
     *
     * @param instructions  the raw IR from IRGenerator.getQuadruples()
     */
    public Optimizer(ArrayList<IRGenerator.Quadruple> instructions) {
        this.original  = instructions;
        this.optimized = new ArrayList<>();
    }

    /**
     * Run all optimizations in order.
     * Call this once after parsing is complete.
     */
    public void optimize() {
        /* Start with a fresh copy of the original instructions */
        optimized = new ArrayList<>();
        for (IRGenerator.Quadruple q : original) {
            optimized.add(new IRGenerator.Quadruple(q.op, q.arg1, q.arg2, q.result));
        }

        /* Apply each optimization pass in order */
        removeIdentityOperations();
        removeRedundantTemporaries();
        constantFolding();

        System.out.println("\n[Optimizer] Optimization complete.");
        System.out.println("[Optimizer] Instructions before: " + original.size());
        System.out.println("[Optimizer] Instructions after:  " + optimized.size());
        int removed = original.size() - optimized.size();
        System.out.println("[Optimizer] Instructions removed: " + removed);
    }


    private void removeIdentityOperations() {
        ArrayList<IRGenerator.Quadruple> pass = new ArrayList<>();

        for (IRGenerator.Quadruple q : optimized) {

            boolean isIdentity = false;

            /* X = X + 0  or  X = X - 0 */
            if ((q.op.equals("+") || q.op.equals("-"))
                    && q.arg2.equals("0")
                    && q.arg1.equals(q.result)) {
                isIdentity = true;
                System.out.println("[Optimizer] Removed identity: "
                    + q.result + " = " + q.arg1 + " " + q.op + " " + q.arg2);
            }

            /* X = X * 1  or  X = X / 1 */
            if ((q.op.equals("*") || q.op.equals("/"))
                    && q.arg2.equals("1")
                    && q.arg1.equals(q.result)) {
                isIdentity = true;
                System.out.println("[Optimizer] Removed identity: "
                    + q.result + " = " + q.arg1 + " " + q.op + " " + q.arg2);
            }

          
            if ((q.op.equals("+") || q.op.equals("-"))
                    && isZero(q.arg2)) {
                /* t1 = x + 0 → just use x directly, skip this instruction */
                if (isTemp(q.result)) {
                    isIdentity = true;
                    /* Replace all future uses of q.result with q.arg1 */
                    replaceOperand(pass, q.result, q.arg1);
                    System.out.println("[Optimizer] Removed identity temp: "
                        + q.result + " = " + q.arg1 + " " + q.op + " 0");
                }
            }

            if ((q.op.equals("*") || q.op.equals("/"))
                    && isOne(q.arg2)) {
                if (isTemp(q.result)) {
                    isIdentity = true;
                    replaceOperand(pass, q.result, q.arg1);
                    System.out.println("[Optimizer] Removed identity temp: "
                        + q.result + " = " + q.arg1 + " " + q.op + " 1");
                }
            }

            if (!isIdentity) {
                pass.add(q);
            }
        }

        optimized = pass;
    }

    private void removeRedundantTemporaries() {
        ArrayList<IRGenerator.Quadruple> pass = new ArrayList<>();
        int i = 0;

        while (i < optimized.size()) {
            IRGenerator.Quadruple current = optimized.get(i);

            if (i + 1 < optimized.size()) {
                IRGenerator.Quadruple next = optimized.get(i + 1);

                boolean currentProducesTemp = isTemp(current.result);
                boolean nextAssignsTemp     = next.op.equals("=")
                                              && next.arg1.equals(current.result)
                                              && next.arg2.equals("");

                if (currentProducesTemp && nextAssignsTemp) {
                    /* Collapse: write the operation directly into the real variable */
                    System.out.println("[Optimizer] Collapsed temp: ("
                        + current.op + ", " + current.arg1 + ", " + current.arg2
                        + ", " + current.result + ") + ("
                        + next.op + ", " + next.arg1 + ", , " + next.result
                        + ")  →  ("
                        + current.op + ", " + current.arg1 + ", " + current.arg2
                        + ", " + next.result + ")");

                    pass.add(new IRGenerator.Quadruple(
                        current.op,
                        current.arg1,
                        current.arg2,
                        next.result       /* write directly to real variable */
                    ));
                    i += 2;   /* skip both current and next */
                    continue;
                }
            }

            pass.add(current);
            i++;
        }

        optimized = pass;
    }

    private void constantFolding() {
        ArrayList<IRGenerator.Quadruple> pass = new ArrayList<>();

        for (IRGenerator.Quadruple q : optimized) {

            /* Only fold arithmetic operations */
            if (!q.op.equals("+") && !q.op.equals("-")
                    && !q.op.equals("*") && !q.op.equals("/")) {
                pass.add(q);
                continue;
            }

            /* Check if both operands are numeric constants */
            if (isNumeric(q.arg1) && isNumeric(q.arg2)) {
                try {
                    double a = Double.parseDouble(q.arg1);
                    double b = Double.parseDouble(q.arg2);
                    double result = 0;

                    switch (q.op) {
                        case "+": result = a + b; break;
                        case "-": result = a - b; break;
                        case "*": result = a * b; break;
                        case "/":
                            if (b == 0) {
                                /* Division by zero — don't fold, keep original */
                                pass.add(q);
                                continue;
                            }
                            result = a / b;
                            break;
                    }

                    /* Format result: if it's a whole number, show as integer */
                    String resultStr;
                    if (result == Math.floor(result) && !Double.isInfinite(result)) {
                        resultStr = String.valueOf((long) result);
                    } else {
                        resultStr = String.valueOf(result);
                    }

                    System.out.println("[Optimizer] Constant fold: "
                        + q.result + " = " + q.arg1 + " " + q.op + " " + q.arg2
                        + "  →  " + q.result + " = " + resultStr);

                    /* Replace with a simple assignment of the computed value */
                    pass.add(new IRGenerator.Quadruple("=", resultStr, "", q.result));
                    continue;

                } catch (NumberFormatException e) {
                    /* Not numeric after all — keep original */
                }
            }

            pass.add(q);
        }

        optimized = pass;
    }


    /** Check if a string is a temporary variable (starts with 't' + digit) */
    private boolean isTemp(String s) {
        if (s == null || s.isEmpty()) return false;
        return s.matches("t\\d+");
    }

    /** Check if a string is a numeric constant (int or float) */
    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Check if a value equals zero */
    private boolean isZero(String s) {
        try { return Double.parseDouble(s) == 0; }
        catch (NumberFormatException e) { return false; }
    }

    /** Check if a value equals one */
    private boolean isOne(String s) {
        try { return Double.parseDouble(s) == 1; }
        catch (NumberFormatException e) { return false; }
    }

    private void replaceOperand(ArrayList<IRGenerator.Quadruple> list,
                                 String oldOperand, String newOperand) {
        for (IRGenerator.Quadruple q : list) {
            if (q.arg1.equals(oldOperand))   q.arg1   = newOperand;
            if (q.arg2.equals(oldOperand))   q.arg2   = newOperand;
            if (q.result.equals(oldOperand)) q.result = newOperand;
        }
    }

    public void printOptimized() {
        if (optimized.isEmpty()) {
            System.out.println("No instructions after optimization.");
            return;
        }

        System.out.println("\n==============================================");
        System.out.println("         OPTIMIZED IR (Three-Address Code)    ");
        System.out.println("==============================================");
        System.out.printf("%-5s | %-10s | %-10s | %-10s | %-10s%n",
                          "#", "OP", "ARG1", "ARG2", "RESULT");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < optimized.size(); i++) {
            IRGenerator.Quadruple q = optimized.get(i);
            System.out.printf("%-5d | %-10s | %-10s | %-10s | %-10s%n",
                              i, q.op, q.arg1, q.arg2, q.result);
        }

        System.out.println("==============================================");
    }

    public ArrayList<IRGenerator.Quadruple> getOptimizedInstructions() {
        return optimized;
    }
}