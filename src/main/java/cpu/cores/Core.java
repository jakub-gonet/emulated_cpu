package cpu.cores;

import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.processing.Cu;
import cpu.processing.operations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidKeyException;

/**
 * This class resembles a single CPU core.
 */
class Core {
    private Logger logger = LogManager.getLogger(Core.class);

    private int PC;
    private MemoryManager memManager;
    private Registers registers;
    private Stack stack;
    private Cu cu;

    /**
     * Creates a new Core with memory mapping from MemoryManager
     *
     * @param memManager a MemoryManager
     * @throws IllegalStateException if mapping for id 1 or 2 already exists in provided MemoryManager
     */
    Core(MemoryManager memManager) {
        this.registers = new Registers(10);
        this.stack = new Stack();

        boolean success = memManager.addReadableWritableDevice(1, registers) &&
                memManager.addReadableWritableDevice(2, stack);
        if (!success)
            throw new IllegalStateException("Could not create mapping for registers or stack");
        this.memManager = memManager;

        cu = new Cu();
    }

    /**
     * Executes next OpCode loaded from Memory on address pointed by ProgramCounter.
     *
     * @throws InvalidKeyException   if Memory wasn't mapped to id 0
     * @throws IllegalStateException if OpCode contained device id not mapped in MemoryManager
     *                               or if OpCode argument count exceeded max argument count
     * @see MemoryManager
     * @see Operation
     * @see Operation#fetch(int)
     */
    void executeNext() throws InvalidKeyException, IllegalStateException {
        Operation operation = new Operation(memManager);
        PC = operation.fetch(PC);

        cu.execute(operation);
    }

    /**
     * Checks if core is stopped.
     *
     * @return true if stopped, false otherwise
     */
    boolean isStopped() {
        return registers
                .statusRegister()
                .state(StatusRegister.StatusFlags.STOPPED);
    }

    /**
     * Restarts core by resetting registers, PC and stack.
     */
    void restart() {
        registers.resetRegisters();
        PC = 0;
        stack.reset();
        logger.info("Restarted the CPU");
    }
}
