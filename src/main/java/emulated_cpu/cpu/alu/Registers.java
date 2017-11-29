package emulated_cpu.cpu.alu;

import emulated_cpu.cpu.memory.IOInterface;

import java.util.ArrayList;

/**
 * This class encapsulates register set mainly for ALU.
 * Every register has 32 bits (Java int implementation).
 * First register is used as status register and shouldn't be used as general purpose register.
 */
public class Registers implements IOInterface {
    private ArrayList<Register> registers;

    public static final int defaultRegistersNumber = 7;

    /**
     * Creates new Register object. This object is guarantied to have at least one register - status register.
     *
     * @param size count of registers. There's no need to take into account status register
     * @throws IllegalArgumentException if size is negative value
     */
    public Registers(int size) {
        if (size < 0) throw new IllegalArgumentException("Size cannot be negative.");

        registers = new ArrayList<>();
        registers.add(new StatusRegister(0));
        for (int i = 0; i < size; i++) {
            registers.add(new Register(0));
        }
    }

    /**
     * Reads value from register set and returns it.
     *
     * @param address Index of a register.
     * @return Value at a address
     * @throws ArrayIndexOutOfBoundsException if reading from nonexistent register
     * @throws NullPointerException           if read value is null
     */
    @Override
    public int read(int address) {
        return registers.get(address)
                        .getValue();
    }

    /**
     * Writes value to a specific register.
     *
     * @param address Index of a register
     * @param data    Value to be written
     * @throws ArrayIndexOutOfBoundsException if writing to nonexistent register
     * @throws NullPointerException           if data is null
     */
    @Override
    public void write(int address, int data) {
        if (address == 0)
            System.err.println("Did you mean to overwrite status register?");
        registers.get(address)
                 .setValue(data);
    }

    /**
     * Sets or clears specified bit in status register.
     *
     * @param flagName flag shortcut to be changed, single letter
     * @param state next state of a bit (cleared, set)
     */
    public void changeStateOfStatusRegisterFlag(String flagName, boolean state){
        ((StatusRegister)this.registers.get(0)).changeStateOfStatusRegisterFlag(flagName, state);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Status register: ")
           .append(Integer.toBinaryString(registers.get(0)
                                                   .getValue()));
        for (int i = 1; i < registers.size(); i++) {
            str.append("Register ")
               .append(i)
               .append(": ")
               .append(registers.get(i));
        }
        return str.toString();
    }
}
