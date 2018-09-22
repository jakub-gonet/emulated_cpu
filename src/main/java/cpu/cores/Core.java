package cpu.cores;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.processing.Alu;
import cpu.processing.Cu;
import cpu.processing.operations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidKeyException;

/**
 * This class resembles a single CPU core.
 */
public class Core {
    private Logger logger = LogManager.getLogger(Core.class);

    private int PC;
    private Memory memory;
    private MemoryManager memManager;
    private Registers registers;
    private Stack stack;
    private Cu cu;
    private Alu alu;

    Core(MemoryManager memManager) throws IllegalStateException {
        this.memManager = memManager;
        try {
            this.memory = memManager.readableWritableDevice(0);
        } catch (InvalidKeyException e) {
            logger.error("Could not obtain Memory from MemoryManager");
            throw new IllegalStateException();
        }
        registers = new Registers(10);
        cu = new Cu();
        alu = new Alu();
    }

    void executeNext() {

    }

    /**
     * Checks if core is stopped.
     *
     * @return true if stopped, false otherwise
     */
    public boolean isStopped() {
        return registers.statusRegister().state(StatusRegister.StatusFlags.STOPPED);
    }

    /**
     * Restarts core by resetting registers, PC and stack.
     */
    public void restart() {
        registers.resetRegisters();
        PC = 0;
        //        stack.reset();
        logger.info("Restarted the CPU");
    }
}
