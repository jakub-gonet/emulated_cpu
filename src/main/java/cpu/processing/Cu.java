package cpu.processing;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.memory.registers.StatusRegister.StatusFlags;
import cpu.processing.operations.OpCode;
import cpu.processing.operations.OpCodes;
import cpu.processing.operations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Cu {
    private Logger logger = LogManager.getLogger(Cu.class);
    private int PC;
    private StatusRegister statusRegister;
    private Memory memory;
    private Stack stack;

    private final OpCodes OP_CODES = new OpCodes(
            List.of(//NOP - 0
                    new OpCode((_x, _y) -> null, 0),
                    //HLT - 1
                    new OpCode((_x, _y) -> {
                        logger.info("Stopping...");
                        statusRegister.set(StatusFlags.STOPPED, true);
                        return null;
                    }, 0),
                    //MOV - 2
                    new OpCode((_to, from) -> from, 2),
                    //JMP - 3
                    new OpCode((to, _y) -> {
                        setPCto(to);
                        return null;
                    }, 1),
                    //JE - 4
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JNE - 5
                    new OpCode((to, _y) -> {
                        if (!statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JL - 6
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.NEGATIVE)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JLE - 7
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.NEGATIVE)
                                || statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JG - 8
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.POSITIVE)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JGE - 9
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.POSITIVE)
                                || statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //PUSH - 10
                    new OpCode((arg, _y) -> {
                        stack.push(arg);
                        return null;
                    }, 1),
                    //POP - 11
                    new OpCode((_x, _y) -> {
                        return stack.pop();
                    }, 0),
                    //CALL - 12
                    new OpCode((f_addr, _y) -> {
                        stack.push(PC);
                        setPCto(f_addr);
                        return null;
                    }, 1),
                    //RET - 13
                    new OpCode((_x, _y) -> {
                        setPCto(stack.pop());
                        return null;
                    }, 0),
                    //INC - 14
                    new OpCode((x, _y) -> ++x, 1),
                    //DEC - 15
                    new OpCode((x, _y) -> --x, 1),
                    //ADD - 16
                    new OpCode((x, y) -> x + y, 2),
                    //SUB - 17
                    new OpCode((x, y) -> x - y, 2),
                    //MUL - 18
                    new OpCode((x, y) -> x * y, 2),
                    //DIV - 19
                    new OpCode((x, y) -> x / y, 2),
                    //NOT - 20
                    new OpCode((x, _y) -> ~x, 1),
                    //AND - 21
                    new OpCode((x, y) -> x & y, 2),
                    //OR - 22
                    new OpCode((x, y) -> x | y, 2),
                    //RSHIFT - 23
                    new OpCode((x, y) -> x >> y, 2),
                    //LSHIFT - 24
                    new OpCode((x, y) -> x << y, 2),
                    //XOR - 25
                    new OpCode((x, y) -> x ^ y, 2),
                    //CMP - 26
                    new OpCode((x, y) -> {
                        int result = x - y;
                        StatusRegister.StatusFlags flag;

                        if (result > 0) {
                            flag = StatusRegister.StatusFlags.POSITIVE;
                        } else if (result == 0) {
                            flag = StatusRegister.StatusFlags.ZERO;
                        } else {
                            flag = StatusRegister.StatusFlags.NEGATIVE;
                        }

                        statusRegister.set(flag, true);
                        return null;
                    }, 2)
            )
    );

    public Cu(MemoryManager memoryManager, Stack stack) {
        this.memory = memoryManager.readableWritableDevice(1);
        this.statusRegister = ((Registers) memoryManager.readableWritableDevice(2)).statusRegister();
        this.stack = stack;
    }

    public int execute(int PC, Operation operation) {
        this.PC = PC;
        OpCode opCode = OP_CODES.byId(operation.opCodeNum());
        int requiredArgsNum = opCode.requiredArguments();

        Integer result = opCode.applyOperation(operation.args());
        logger.info("Executed {} opcode with {} args, got {} in result", operation.opCodeNum(), operation.args(), result);
        writeResultIfNecessary(result, operation, requiredArgsNum);
        return this.PC;
    }

    private void setPCto(int to) {
        if (memory.canReadAt(to)) {
            PC = to;
        } else {
            throw new IllegalStateException("Jumping out of memory bounds");
        }
    }

    private void writeResultIfNecessary(Integer result, Operation operation, int requiredArgsNum) {
        if (requiredArgsNum > 0 && result != null) {
            logger.info("Writing {} to {} device at {} address", result, operation.destinationDevice(), operation.destinationAddress());
            operation.destinationDevice()
                     .write(operation.destinationAddress(), result);
        }
    }
}
