package cpu.memory.registers;

/**
 * Represents status register. Used to store result of mathematical and logic operations.
 * <p>
 * Structure of a Status register:
 * <p>
 * bit 0 - zero flag (indicates zero value of last operation)<p>
 * bit 1 - positive flag (indicates more than zero value of last operation)<p>
 * bit 2 - negative flag (indicates less than zero value of last operation)<p>
 * bit 3 - stopped flag (indicates that Core is stopped)<p>
 * bit 4-31 - not used <p>
 */
public class StatusRegister extends Register {
    /**
     * Creates new StatusRegister object with specified initial value.
     *
     * @param value value to be stored in register
     */
    public StatusRegister(int value) {
        super(value);
    }

    /**
     * Creates new StatusRegister object with initial 0 value.
     */
    public StatusRegister() {
        super();
    }

    /**
     * Sets value for given flag.
     *
     * @param flag     a flag to be updated
     * @param newState new bool state
     */
    public void set(StatusFlags flag, boolean newState) {
        switch (flag) {
            case ZERO:
                setValueAt(0, newState);
                break;
            case POSITIVE:
                setValueAt(1, newState);
                break;
            case NEGATIVE:
                setValueAt(2, newState);
                break;
            case STOPPED:
                setValueAt(3, newState);
                break;
        }
    }

    /**
     * Gets state of given flag.
     *
     * @param flag a flag to be checked
     * @return state
     */
    public boolean state(StatusFlags flag) {
        boolean state = false;
        switch (flag) {
            case ZERO:
                state = valueAt(0);
                break;

            case POSITIVE:
                state = valueAt(1);
                break;

            case NEGATIVE:
                state = valueAt(2);
                break;

            case STOPPED:
                state = valueAt(3);
                break;
        }
        return state;
    }

    /**
     * Resets POSITIVE, NEGATIVE AND ZERO flags
     */
    public void resetArithmeticFlags() {
        this.set(this.value() & ~(0x7));
    }

    /**
     * Flags used to mark bits.
     */
    public enum StatusFlags {
        ZERO,
        POSITIVE,
        NEGATIVE,
        STOPPED
    }
}
