package emulated_cpu.cpu.data_storage;

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
    StatusRegister() {
        super();
    }

    /**
     * Gets state of zero flag.
     *
     * @return state of zero flag
     */
    public boolean getZeroFlagState() {
        return this.getValueAt(0) == 1;
    }

    /**
     * Sets zero flag to new state.
     *
     * @param state new state
     */
    public void setZeroFlagState(boolean state) {
        this.setValueAt(0, state);
    }

    /**
     * Gets state of carry flag.
     *
     * @return state of cary flag
     */
    public boolean getCarryFlagState() {
        return this.getValueAt(1) == 1;
    }

    /**
     * Sets carry flag to new state.
     *
     * @param state new state
     */
    public void setCarryFlagState(boolean state) {
        this.setValueAt(1, state);
    }

    /**
     * Gets state of negative flag.
     *
     * @return state of negative flag
     */
    public boolean getNegativeFlagState() {
        return this.getValueAt(2) == 1;
    }

    /**
     * Sets negative flag to new state.
     *
     * @param state new state
     */
    public void setNegativeFlagState(boolean state) {
        this.setValueAt(2, state);
    }
}
