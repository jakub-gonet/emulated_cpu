package emulated_cpu.data_storage;

import emulated_cpu.data_storage.program_storage.ProgramHolder;
import emulated_cpu.data_storage.program_storage.ProgramHolderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProgramHolderAddressFromRegister implements Addressable {
    private Logger logger = LogManager.getLogger(ProgramHolderAddressFromRegister.class);
    private ProgramHolder holder = ProgramHolderManager.getCurrentProgramHolder();
    private Registers registers;

    public ProgramHolderAddressFromRegister(Registers registers) {
        this.registers = registers;
    }

    @Override
    public int read(int address) {
        int value = holder.read(registers.read(address));
        logger.trace("Reading {} from memory address saved in register {}", value, address);
        return value;
    }

    @Override
    public void write(int address, int data) {
        logger.trace("Writing {} to memory address saved in register {}", data, address);
        holder.write(registers.read(address), data);
    }

    @Override
    public int size() {
        return registers.size();
    }
}
