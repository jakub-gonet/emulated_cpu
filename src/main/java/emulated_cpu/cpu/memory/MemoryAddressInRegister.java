package emulated_cpu.cpu.memory;

import emulated_cpu.cpu.alu.Registers;

public class MemoryAddressInRegister implements IOInterface {
    private Memory memory = Memory.getInstance();
    private Registers registers;

    public MemoryAddressInRegister(Registers registers) {
        this.registers = registers;
    }

    @Override
    public int read(int address) {
        return memory.read(registers.read(address));
    }

    @Override
    public void write(int address, int data) {
        memory.write(registers.read(address), data);
    }
}
