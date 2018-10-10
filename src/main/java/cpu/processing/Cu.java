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

import java.security.InvalidKeyException;
import java.util.List;

public class Cu {
    private Logger logger = LogManager.getLogger(Cu.class);
    private int PC;
    private StatusRegister statusRegister;
    private Memory memory;
    private Stack stack;

    private final OpCodes OP_CODES = new OpCodes(
            List.of(//NOP
                    new OpCode((_x, _y) -> null, 0),
                    //HLT
                    new OpCode((_x, _y) -> {
                        logger.info("Stopping...");
                        statusRegister.set(StatusFlags.STOPPED, true);
                        return null;
                    }, 0),
                    //MOV
                    new OpCode((_from, to) -> to, 2),
                    //JMP
                    new OpCode((to, _y) -> {
                        setPCto(to);
                        return null;
                    }, 1),
                    //JE
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JNE
                    new OpCode((to, _y) -> {
                        if (!statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JL
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.NEGATIVE)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JLE
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.NEGATIVE)
                                || statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JG
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.POSITIVE)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //JGE
                    new OpCode((to, _y) -> {
                        if (statusRegister.state(StatusFlags.POSITIVE)
                                || statusRegister.state(StatusFlags.ZERO)) {
                            setPCto(to);
                        }
                        return null;
                    }, 1),
                    //PUSH
                    new OpCode((arg, _y) -> {
                        stack.push(arg);
                        return null;
                    }, 1),
                    //POP
                    new OpCode((_x, _y) -> {
                        return stack.pop();
                    }, 0),
                    //CALL
                    new OpCode((f_addr, _y) -> {
                        stack.push(PC);
                        setPCto(f_addr);
                        return null;
                    }, 1),
                    //RET
                    new OpCode((_x, _y) -> {
                        setPCto(stack.pop());
                        return null;
                    }, 0),
                    //INC
                    new OpCode((x, _y) -> ++x, 1),
                    //DEC
                    new OpCode((x, _y) -> --x, 1),
                    //ADD
                    new OpCode((x, y) -> x + y, 2),
                    //SUB
                    new OpCode((x, y) -> x - y, 2),
                    //MUL
                    new OpCode((x, y) -> x * y, 2),
                    //DIV
                    new OpCode((x, y) -> x / y, 2),
                    //NOT
                    new OpCode((x, _y) -> ~x, 1),
                    //AND
                    new OpCode((x, y) -> x & y, 2),
                    //OR
                    new OpCode((x, y) -> x | y, 2),
                    //RSHIFT
                    new OpCode((x, y) -> x >> y, 2),
                    //LSHIFT
                    new OpCode((x, y) -> x << y, 2),
                    //XOR
                    new OpCode((x, y) -> x ^ y, 2),
                    //CMP
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

    public Cu(MemoryManager memoryManager, Stack stack) throws InvalidKeyException {
        this.memory = memoryManager.readableWritableDevice(0);
        this.statusRegister = ((Registers) memoryManager.readableWritableDevice(1)).statusRegister();
        this.stack = stack;
    }

    public int execute(int PC, Operation operation) {
        this.PC = PC;
        OpCode opCode = OP_CODES.byId(operation.opCodeNum());
        int requiredArgsNum = opCode.requiredArguments();

        Integer result = opCode.applyOperation(operation.args());
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
        if (requiredArgsNum > 0) {
            operation.destinationDevice()
                     .write(operation.destinationAddress(), result);
        }
    }
}
