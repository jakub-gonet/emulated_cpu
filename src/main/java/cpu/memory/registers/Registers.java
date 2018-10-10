package cpu.memory.registers;

import cpu.memory.Readable;
import cpu.memory.Writable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages collection of Registers.
 */
public class Registers implements Readable, Writable {
    private List<Register> registers = new ArrayList<>();

    /**
     * Creates new Register object with <code>count</code> number of Registers and one StatusRegister.
     *
     * @param count positive count of Registers to create
     * @throws IllegalArgumentException if count was negative
     * @see StatusRegister
     * @see Register
     */
    public Registers(int count) {
        if (count <= 0)
            throw new IllegalArgumentException();

        registers.add(new StatusRegister());
        registers.addAll(createRegisters(count-1));
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
     * Returns Register by given id.
     *
     * @param id
     * @return Register object
     */
    public Register byId(int id) {
        return registers.get(id);
    }

    /**
     * Returns total register count.
     *
     * @return count
     */
    public int size() {
        return registers.size();
    }


    /**
     * Resets registers to their default value.
     */
    public void resetRegisters() {
        for (Register r :
                registers) {
            r.set(0);
        }
    }

    /**
     * Reads a value from Register with given id.
     *
     * @param address an address mapped to Register
     * @return value read from Register
     */
    @Override
    public int read(int address) {
        return byId(address).value();
    }

    /**
     * Checks if Register with given id can be read.
     *
     * @param address an Register id to check
     * @return
     */
    @Override
    public boolean canReadAt(int address) {
        return isInRegistersBounds(address);
    }

    /**
     * Writes a value to Register with given id.
     *
     * @param address an address mapped to Register
     * @param data    data to write
     */
    @Override
    public void write(int address, int data) {
        byId(address).set(data);
    }

    /**
     * Checks if Register with given id can be written.
     *
     * @param address an Register id to check
     * @return
     */
    @Override
    public boolean canWriteAt(int address) {
        return isInRegistersBounds(address);
    }

    /**
     * Creates a List of Registers.
     *
     * @param count number of Registers to create
     * @return
     */
    private List<Register> createRegisters(int count) {
        List<Register> registers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            registers.add(new Register());
        }
        return registers;
    }

    /**
     * Checks if given address maps to any Register.
     *
     * @param address
     * @return
     */
    private boolean isInRegistersBounds(int address) {
        return address >= 0 && address < registers.size();
    }
}
