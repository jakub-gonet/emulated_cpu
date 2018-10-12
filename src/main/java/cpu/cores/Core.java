package cpu.cores;

import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.processing.Cu;
import cpu.processing.operations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     */
    Core(MemoryManager memManager) {
        this(memManager, 16, 64);
    }

    Core(MemoryManager memManager, int registersCount, int stackCapacity) {
        this.registers = new Registers(registersCount);
        this.stack = new Stack(stackCapacity);
        this.memManager = new MemoryManager(memManager, registers);

        cu = new Cu(this.memManager, stack);
    }

    /**
     * Executes next OpCode loaded from Memory on address pointed by ProgramCounter.
     *
     * @see MemoryManager
     * @see Operation
     * @see Operation#fetch(int)
     */
    void executeNext() {
        Operation operation = new Operation(memManager);
        PC = operation.fetch(PC);

        PC = cu.execute(PC, operation);
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
