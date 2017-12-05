package emulated_cpu.cpu.cu;

import emulated_cpu.Arguments;
import emulated_cpu.OpCode;
import emulated_cpu.OperatingUnit;

import java.util.ArrayList;
import java.util.Arrays;

public class CU implements OperatingUnit {
    public int instructionPointer = 0;
    private boolean stopped = false;

    private final ArrayList<OpCode> CU_OP_CODES = new ArrayList<>(Arrays.asList(
        new OpCode((x, y) -> null, 0),  //NOP
        new OpCode((x, y) -> {
            stopped = true;
            return null;
        }, 0),                          //HLT
        new OpCode((x, y) -> y, 2),     //MOV
        new OpCode((x, y) -> {                         //JMP
            instructionPointer = y;
            return null;
        }, 1)
    ));

    /**
     * Execute CU OP code.
     *
     * @param opCode OP code to select operation
     * @param args   arguments passed as operation arguments
     * @return result of the OP code, mostly null
     */
    @Override
    public Integer execute(int opCode, Arguments args) {
        checkIfCuOPCodeExists(opCode);
        CU_OP_CODES.get(opCode)
                   .checkIfArgumentsMatchRequiredCount(args);

        return CU_OP_CODES.get(opCode)
                          .getOperation()
                          .apply(args.arg1, args.arg2);
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

    /**
     * Gets CU OP codes.
     *
     * @return ArrayList of OP codes
     */
    @Override
    public ArrayList<OpCode> getOpCodes() {
        return CU_OP_CODES;
    }
}
