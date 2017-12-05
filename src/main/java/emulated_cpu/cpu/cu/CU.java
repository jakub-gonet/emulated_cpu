package emulated_cpu.cpu.cu;

import emulated_cpu.Arguments;
import emulated_cpu.OpCode;
import emulated_cpu.OperatingUnit;
import emulated_cpu.cpu.alu.StatusRegister;
import emulated_cpu.cpu.memory.Memory;

import java.util.ArrayList;
import java.util.Arrays;

public class CU implements OperatingUnit {
    public int instructionPointer = 0;
    private boolean stopped = false;
    private StatusRegister statusRegister;
    private Memory memory = Memory.getInstance();

    private final ArrayList<OpCode> CU_OP_CODES = new ArrayList<>(Arrays.asList(
        new OpCode((x, y) -> null, 0),  //NOP
        new OpCode((x, y) -> {
            stopped = true;
            return null;
        }, 0),                          //HLT
        new OpCode((x, y) -> y, 2),     //MOV
        new OpCode((x, y) -> {                         //JMP
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JE
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            if (statusRegister.getZeroFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JNE
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            if (!statusRegister.getZeroFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JL
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            if (statusRegister.getNegativeFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JLE
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            if (statusRegister.getZeroFlagState()
                || statusRegister.getNegativeFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JG
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            if (statusRegister.getCarryFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JGE
            if (x < 0 || x >= memory.getMemory().size())
                throw new IllegalArgumentException("Jumping out bound of memory");
            if (statusRegister.getZeroFlagState()
                || statusRegister.getCarryFlagState())
                instructionPointer = x;
            return null;
        }, 1)
    ));

    /**
     * Creates new ControlUnit with specified StatusRegister (to be read by OP codes).
     *
     * @param statusRegister StatusRegister which is checked
     */
    public CU(StatusRegister statusRegister) {
        this.statusRegister = statusRegister;
    }

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

    public boolean isStopped() {
        return stopped;
    }
}
