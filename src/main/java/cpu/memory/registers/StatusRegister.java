package cpu.memory.registers;

/**
 * Represents status register. Used to store result of mathematical and logic operations.
 * <p>
 * Structure of a Status register:
 * <p>
 * bit 0 - zero flag (indicates zero value of last operation) - Z <p>
 * bit 1 - positive flag (indicates more than zero value of last operation) - C <p>
 * bit 2 - negative flag (indicates less than zero value of last operation) - N <p>
 * bit 3-31 - not used <p>
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
                state = valueAt(0) == 1;
                break;

            case POSITIVE:
                state = valueAt(1) == 1;
                break;

            case NEGATIVE:
                state = valueAt(2) == 1;
                break;

            case STOPPED:
                state = valueAt(3) == 1;
                break;
        }
        return state;
    }

    public enum StatusFlags {
        ZERO,
        POSITIVE,
        NEGATIVE,
        STOPPED
    }
}
