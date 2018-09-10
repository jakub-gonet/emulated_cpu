package cpu.cores;

import cpu.memory.Memory;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.processing.Alu;
import cpu.processing.Cu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Core {
    private Logger logger = LogManager.getLogger(Core.class);

    private int PC;
    private Memory memory;
    private Registers registers;
    private Stack stack;
    private Cu cu;
    private Alu alu;

    Core(Memory memory) {
        this.memory = memory;
    }

    void executeNext() {

    }

    public Registers registers() {
        return registers;
    }

    public boolean isStopped() {
        return registers.statusRegister().state(StatusRegister.StatusFlags.STOPPED);
    }

    public void restart() {
        registers.resetRegisters();
        PC = 0;
        stack.reset();
        logger.info("Restarted the CPU");
    }
}
