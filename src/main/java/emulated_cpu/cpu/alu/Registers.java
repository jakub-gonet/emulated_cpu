package emulated_cpu.cpu.alu;

import emulated_cpu.cpu.memory.IOInterface;

import java.util.ArrayList;

/**
 * This class encapsulates register set mainly for ALU.
 * Every register has 32 bits (Java int implementation).
 * First register is used as status register and shouldn't be used as general purpose register.
 * <p>
 * Structure of a Status register:
 * <p>
 * bit 0 - zero flag (indicates zero value of last operation) - Z <p>
 * bit 1 - carry flag (indicates more than zero value of last operation) - C <p>
 * bit 2 - negative flag (indicates less than zero value of last operation) - N <p>
 * bit 3-31 - not used <p>
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
        if (size >= 0) {
            registers = new ArrayList<>();
            for (int i = 0; i < size + 1; i++) {
                registers.add(new Register(0));
            }
        } else throw new IllegalArgumentException("Size cannot be negative.");
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
     * Sets specific flag in status register.
     *
     * @param flagName short name of flag, for example "E", "Z" etc.
     *                 Check class documentation for more information
     * @param state    new state for that flag
     * @throws IllegalArgumentException if flag doesn't exist
     * @throws NullPointerException     if flag is null
     */
    public void changeStateOfStatusRegisterFlag(String flagName, boolean state) {
        int flagNumber;

        switch (flagName) {
            case "Z":
                flagNumber = 0;
                break;
            case "C":
                flagNumber = 1;
                break;
            case "N":
                flagNumber = 2;
                break;
            default:
                throw new IllegalArgumentException("Flag doesn't exist.");
        }

        registers.get(0)
                 .setValueAt(flagNumber, state);
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
