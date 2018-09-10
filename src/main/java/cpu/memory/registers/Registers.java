package cpu.memory.registers;

import java.util.ArrayList;
import java.util.List;

public class Registers {
    private List<Register> generalPurpose;
    private StatusRegister statusRegister;

    /**
     * Creates new Register object with `count` number of general purpose `Register`s.
     *
     * @param count how many registers should be created
     */
    public Registers(int count) {
        generalPurpose = createRegisters(count);
        statusRegister = new StatusRegister();
    }


    /**
     * Returns StatusRegister.
     *
     * @return StatusRegister object
     */
    public StatusRegister statusRegister() {
        return statusRegister;
    }


    /**
     * Gets general purpose register by given id.
     *
     * @param id id of register
     * @return Register object
     */
    public Register byId(int id) {
        return generalPurpose.get(id);
    }

    /**
     * Gets total register count.
     *
     * @return count of general purpose registers
     */
    public int size() {
        return generalPurpose.size();
    }


    /**
     * Resets general purpose register as well as status register.
     */
    public void resetRegisters() {
        for (Register r :
                generalPurpose) {
            r.set(0);
        }

        statusRegister.set(0);
    }

    private List<Register> createRegisters(int count) {
        List<Register> registers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            registers.add(new Register());
        }
        return registers;
    }
}
