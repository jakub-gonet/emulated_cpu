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

    Core(MemoryManager memManager) throws IllegalStateException {
        this.registers = new Registers(10);
        this.stack = new Stack();

        memManager.addReadableWritableDevice(1, registers);
        memManager.addReadableWritableDevice(2, stack);
        this.memManager = memManager;

        cu = new Cu();
    }

    void executeNext() throws InvalidKeyException {
        Operation operation = new Operation(memManager);
        PC = operation.fetch(PC);

        cu.execute(operation);
    }

    /**
     * Checks if core is stopped.
     *
     * @return true if stopped, false otherwise
     */
    public boolean isStopped() {
        return registers
                .statusRegister()
                .state(StatusRegister.StatusFlags.STOPPED);
    }

    /**
     * Restarts core by resetting registers, PC and stack.
     */
    public void restart() {
        registers.resetRegisters();
        PC = 0;
        stack.reset();
        logger.info("Restarted the CPU");
    }
}
