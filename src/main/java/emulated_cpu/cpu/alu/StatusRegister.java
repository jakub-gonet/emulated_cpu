package emulated_cpu.cpu.alu;

/**
 * Represents status register of a ALU registers. Used to store result of mathematical and logic operations.
 * <p>
 * Structure of a Status register:
 * <p>
 * bit 0 - zero flag (indicates zero value of last operation) - Z <p>
 * bit 1 - carry flag (indicates more than zero value of last operation) - C <p>
 * bit 2 - negative flag (indicates less than zero value of last operation) - N <p>
 * bit 3-31 - not used <p>
 */
public class StatusRegister extends Register {
    /**
     * Creates new Status Register object with specified initial value.
     *
     * @param value value to be stored in register
     */
    public StatusRegister(int value) {
        super(value);
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

        this.setValueAt(flagNumber, state);
    }
}
