package emulated_cpu.cpu.memory;

import emulated_cpu.cpu.alu.Registers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemoryAddressInRegister implements IOInterface {
    private Logger logger = LogManager.getLogger(MemoryAddressInRegister.class);
    private Memory memory = Memory.getInstance();
    private Registers registers;

    public MemoryAddressInRegister(Registers registers) {
        this.registers = registers;
    }

    @Override
    public int read(int address) {
        int value = memory.read(registers.read(address));
        logger.trace("Reading {} from memory address saved in register {}", value, address);
        return value;
    }

    @Override
    public void write(int address, int data) {
        logger.trace("Writing {} to memory address saved in register {}", data, address);
        memory.write(registers.read(address), data);
    }
}
