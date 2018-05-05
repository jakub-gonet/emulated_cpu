package emulated_cpu.cpu.cu;

import emulated_cpu.command.Arguments;
import emulated_cpu.opcode.OpCode;
import emulated_cpu.OperatingUnit;
import emulated_cpu.cpu.data_storage.StatusRegister;
import emulated_cpu.cpu.data_storage.Memory;
import emulated_cpu.cpu.data_storage.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CU class is used to control flow of the program.
 */
public class CU implements OperatingUnit {
    private Logger logger = LogManager.getLogger(CU.class);

    public int instructionPointer = 0;
    private boolean stopped = false;
    private StatusRegister statusRegister;
    private Memory memory = Memory.getInstance();
    private Stack stack;

    private final ArrayList<OpCode> CU_OP_CODES = new ArrayList<>(Arrays.asList(
        new OpCode((x, y) -> null, 0),  //NOP
        new OpCode((x, y) -> {
            logger.info("Stopping the CPU");
            stopped = true;
            return null;
        }, 0),                          //HLT
        new OpCode((x, y) -> y, 2),     //MOV
        new OpCode((x, y) -> {                         //JMP
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JMP opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JE
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JE opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            if (statusRegister.getZeroFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JNE
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JNE opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            if (!statusRegister.getZeroFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JL
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JL opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            if (statusRegister.getNegativeFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JLE
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JLE opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            if (statusRegister.getZeroFlagState()
                || statusRegister.getNegativeFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JG
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JG opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            if (statusRegister.getCarryFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //JGE
            if (x < 0 || x >= memory.getMemory()
                                    .size()) {
                logger.error("Jumping out of memory in JMP opcode");
                throw new IllegalArgumentException("Jumping out bound of memory");
            }
            if (statusRegister.getZeroFlagState()
                || statusRegister.getCarryFlagState())
                instructionPointer = x;
            return null;
        }, 1),
        new OpCode((x, y) -> {                         //PUSH
            stack.push(x);
            return null;
        }, 1),
        new OpCode((x, y) -> stack.pop(), 1), //POP
        new OpCode((x, y) -> {                            //CALL
            stack.push(instructionPointer);
            instructionPointer = x;
            return null;
        }, 1)
    ));

    /**
     * Creates new ControlUnit with specified StatusRegister (to be read by OP codes).
     *
     * @param statusRegister StatusRegister which is checked
     * @param stackSize      size of created stack
     */
    public CU(StatusRegister statusRegister, int stackSize) {
        this.statusRegister = statusRegister;
        this.stack = new Stack(stackSize);
    }

    public CU(StatusRegister statusRegister) {
        this.statusRegister = statusRegister;
        this.stack = new Stack();
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
        Integer value = CU_OP_CODES.get(opCode)
                                   .getOperation()
                                   .apply(args.arg1, args.arg2);

        logger.debug("Executed 0x{} CU opcode, got {} in result", Integer.toHexString(opCode), value);

        return value;
    }

    /**
     * Checks if passed OP code exist in lookup table.
     *
     * @param opCode OP code to be checked
     */
    private void checkIfCuOPCodeExists(int opCode) {

        if (opCode > CU_OP_CODES.size() - 1) {
            logger.error("CU OP code {} doesn't exist", opCode);
            throw new IndexOutOfBoundsException("OP code " + opCode + " doesn't exist");
        }
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

    public void setStopped(boolean isStopped){
        stopped = isStopped;
    }
}
