package compilerproject;

import java.util.ArrayList;

public class TargetCodeGenerator {

    private ArrayList<IRGenerator.Quadruple> instructions;
    private ArrayList<String> assembly;

    public TargetCodeGenerator(ArrayList<IRGenerator.Quadruple> optimizedInstructions) {
        this.instructions = optimizedInstructions;
        this.assembly = new ArrayList<>();
    }

    public void generate() {
        assembly.clear();

        for (IRGenerator.Quadruple q : instructions) {
            switch (q.op) {

                /* ── Simple assignment: result = arg1 ── */
                case "=":
                    emit("LD  R0, " + q.arg1);
                    emit("ST  " + q.result + ", R0");
                    break;

                /* ── Arithmetic operations ── */
                case "+":
                    emit("LD  R0, " + q.arg1);
                    emit("ADD R0, R0, " + q.arg2);
                    emit("ST  " + q.result + ", R0");
                    break;

                case "-":
                    emit("LD  R0, " + q.arg1);
                    emit("SUB R0, R0, " + q.arg2);
                    emit("ST  " + q.result + ", R0");
                    break;

                case "*":
                    emit("LD  R0, " + q.arg1);
                    emit("MUL R0, R0, " + q.arg2);
                    emit("ST  " + q.result + ", R0");
                    break;

                case "/":
                    emit("LD  R0, " + q.arg1);
                    emit("DIV R0, R0, " + q.arg2);
                    emit("ST  " + q.result + ", R0");
                    break;

                /* ── Print / outString ── */
                case "print":
                    emit("LD  R0, " + q.arg1);
                    emit("OUT R0");
                    break;

                /* ── Loop label ── */
                case "label":
                    emit(q.arg1 + ":");
                    break;

                /* ── Conditional jump (ifFalse) ── */
                case "ifFalse":
                    emit("LD  R0, " + q.arg1);
                    emit("JF  R0, " + q.result);   
                    break;

                /* ── Comparison operators ── */
                case "==":
                    emit("LD  R0, " + q.arg1);
                    emit("CMP R0, " + q.arg2);
                    emit("SEQ " + q.result);       
                    break;

                case "<":
                    emit("LD  R0, " + q.arg1);
                    emit("CMP R0, " + q.arg2);
                    emit("SLT " + q.result);
                    break;

                case ">":
                    emit("LD  R0, " + q.arg1);
                    emit("CMP R0, " + q.arg2);
                    emit("SGT " + q.result);
                    break;

                case "<=":
                    emit("LD  R0, " + q.arg1);
                    emit("CMP R0, " + q.arg2);
                    emit("SLE " + q.result);
                    break;

                case ">=":
                    emit("LD  R0, " + q.arg1);
                    emit("CMP R0, " + q.arg2);
                    emit("SGE " + q.result);
                    break;

                case "<>":
                    emit("LD  R0, " + q.arg1);
                    emit("CMP R0, " + q.arg2);
                    emit("SNE " + q.result);
                    break;

                /* ── Unconditional jump (goto) ── */
                case "goto":
                    emit("JMP " + q.result);
                    break;

                /* ── Switch/case ── */
                case "switch":
                    emit("LD  R0, " + q.arg1);
                    break;

                case "endswitch":
                    emit("; endswitch");
                    break;

                default:
                    emit("; [unhandled] " + q.op + " " + q.arg1
                         + " " + q.arg2 + " " + q.result);
                    break;
            }
        }
    }

    /** Add one assembly instruction to the output list */
    private void emit(String instruction) {
        assembly.add(instruction);
    }

    /** Print the generated assembly code */
    public void printAssembly() {
        System.out.println("\n==============================================");
        System.out.println("         TARGET CODE (CISC Assembly)          ");
        System.out.println("==============================================");

        for (int i = 0; i < assembly.size(); i++) {
            String line = assembly.get(i);
            /* Labels are not indented, everything else is */
            if (line.endsWith(":")) {
                System.out.println(line);
            } else {
                System.out.println("    " + line);
            }
        }

        System.out.println("==============================================");
    }

    /** Return generated assembly (for future use) */
    public ArrayList<String> getAssembly() {
        return assembly;
    }
}