package emulated_cpu.cpu.cu;

import emulated_cpu.OpCode;

import java.util.ArrayList;
import java.util.Arrays;

public class CU {
    private int instructionPointer = 0;
    private boolean stopped = false;

    private final ArrayList<OpCode> CU_OP_CODES = new ArrayList<>(Arrays.asList(
        new OpCode((x, y) -> null, 0),  //NOP
        new OpCode((x, y) -> {
            stopped = true;
            return null;
        }, 0),                          //HLT
        new OpCode((x, y) -> {                         //JMP
            instructionPointer = x;
            return null;
        }, 1)
    ));

    void executeCuOpcode(int opCode, Integer arg1) {
        checkIfCuOPCodeExists(opCode);
        CU_OP_CODES.get(opCode)
                   .checkIfArgumentsMatchRequiredCount(arg1, null);

        CU_OP_CODES.get(opCode)
                   .getOperation()
                   .apply(arg1, null);
    }

    void executeCuOpcode(int opCode) {
        executeCuOpcode(opCode, null);
    }

    /**
     * Checks if passed OP code exist in lookup table.
     *
     * @param opCode OP code to be checked
     */
    private void checkIfCuOPCodeExists(int opCode) {
        if (opCode > CU_OP_CODES.size() - 1)
            throw new IndexOutOfBoundsException("OP code " + opCode + " doesn't exist");
    }

    public int getInstructionPointer() {
        return instructionPointer;
    }

    public void setInstructionPointer(int instructionPointer) {
        this.instructionPointer = instructionPointer;
    }
}
