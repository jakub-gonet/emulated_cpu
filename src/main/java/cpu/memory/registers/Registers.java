package cpu.memory.registers;

import cpu.memory.Readable;
import cpu.memory.Writable;

import java.util.ArrayList;
import java.util.List;

public class Registers implements Readable, Writable {
    private List<Register> registers = new ArrayList<>();

    /**
     * Creates new Register object with `count` number of general purpose `Register`s.
     *
     * @param count how many registers should be created
     */
    public Registers(int count) {
        registers.add(new StatusRegister());
        registers.addAll(createRegisters(count));
    }


    /**
     * Returns StatusRegister.
     *
     * @return StatusRegister object
     */
    public StatusRegister statusRegister() {
        return (StatusRegister) byId(0);
    }


    /**
     * Gets general purpose register by given id.
     *
     * @param id id of register
     * @return Register object
     */
    public Register byId(int id) {
        return registers.get(id);
    }

    /**
     * Gets total register count.
     *
     * @return count of general purpose registers
     */
    public int size() {
        return registers.size();
    }


    /**
     * Resets general purpose register as well as status register.
     */
    public void resetRegisters() {
        for (Register r :
                registers) {
            r.set(0);
        }
    }

    @Override
    public int read(int address) {
        return byId(address).value();
    }

    @Override
    public boolean canReadAt(int address) {
        return isInRegistersBounds(address);
    }

    @Override
    public void write(int address, int data) {
        byId(address).set(data);
    }

    @Override
    public boolean canWriteAt(int address) {
        return isInRegistersBounds(address);
    }

    private List<Register> createRegisters(int count) {
        List<Register> registers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            registers.add(new Register());
        }
        return registers;
    }

    private boolean isInRegistersBounds(int address) {
        return address >= 0 && address < registers.size();
    }
}
