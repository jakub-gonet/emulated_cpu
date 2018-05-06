package emulated_cpu.cpu;

import emulated_cpu.cpu.command.Arguments;
import emulated_cpu.cpu.opcode.OpCode;
import emulated_cpu.data_storage.Stack;
import emulated_cpu.data_storage.StatusRegister;
import emulated_cpu.data_storage.program_storage.ProgramHolder;
import emulated_cpu.data_storage.program_storage.ProgramHolderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
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
    private ProgramHolder holder = ProgramHolderManager.getCurrentProgramHolder();
    private Stack stack;

    private final ArrayList<OpCode> CU_OP_CODES = new ArrayList<>(Arrays.asList(
        //NOP
        new OpCode((x, y) -> null, 0, 0),
        //HLT
        new OpCode((x, y) -> {
            logger.info("Stopping the CPU");
            stopped = true;
            return null;
        }, 0, 1),
        //MOV
        new OpCode((x, y) -> y, 2, 2),
        //JMP
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JMP");
            instructionPointer = x;
            return null;
        }, 1, 2),
        //JE
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JN");
            if (statusRegister.getZeroFlagState())
                instructionPointer = x;
            return null;
        }, 1, 3),
        //JNE
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JNE");
            if (!statusRegister.getZeroFlagState())
                instructionPointer = x;
            return null;
        }, 1, 4),
        //JL
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JL");
            if (statusRegister.getNegativeFlagState())
                instructionPointer = x;
            return null;
        }, 1, 5),
        //JLE
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JLE");
            if (statusRegister.getZeroFlagState()
                || statusRegister.getNegativeFlagState())
                instructionPointer = x;
            return null;
        }, 1, 6),
        //JG
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JG");
            if (statusRegister.getCarryFlagState())
                instructionPointer = x;
            return null;
        }, 1, 7),
        //JGE
        new OpCode((x, y) -> {
            checkIfJumpNotOutOfBounds(x, "JGE");
            if (statusRegister.getZeroFlagState()
                || statusRegister.getCarryFlagState())
                instructionPointer = x;
            return null;
        }, 1, 8),
        //PUSH
        new OpCode((x, y) -> {
            stack.push(x);
            return null;
        }, 1, 9),
        //POP
        new OpCode((x, y) -> stack.pop(), 1, 10),
        //CALL
        new OpCode((x, y) -> {
            stack.push(instructionPointer);
            instructionPointer = x;
            return null;
        }, 1, 11)
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
                                   .apply(args.getArg1(), args.getArg2());

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

    private void checkIfJumpNotOutOfBounds(int address, String opcode) {
        if (address < 0 || address >= holder.size()) {
            logger.error(MessageFormat.format("Jumping out of memory in {0} opcode", opcode));
            throw new IllegalArgumentException("Jumping out bound of memory");
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

    public void setStopped(boolean isStopped) {
        stopped = isStopped;
    }
}
