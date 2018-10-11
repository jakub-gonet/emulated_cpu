package cpu.memory.addressing_modes;

import cpu.memory.Memory;
import cpu.memory.Readable;
import cpu.memory.Writable;
import cpu.memory.registers.Registers;

public class AddressFromRegister implements Readable, Writable {

    private Registers registers;
    private Memory mem;

    public AddressFromRegister(Memory mem, Registers registers) {
        this.registers = registers;
        this.mem = mem;
    }

    @Override
    public int read(int address) {
        return mem.read(registers.read(address));
    }

    @Override
    public boolean canReadAt(int address) {
        return registers.canReadAt(address) && mem.canReadAt(registers.read(address));
    }

    @Override
    public void write(int address, int data) {
        mem.write(registers.read(address), data);
    }

    @Override
    public boolean canWriteAt(int address) {
        return registers.canReadAt(address) && mem.canReadAt(registers.read(address));
    }
}
