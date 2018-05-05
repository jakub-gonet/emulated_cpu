package emulated_cpu.data_storage;

/**
 * This class wraps single register behaviour. Mainly used by Registers class.
 */
public class Register {
    private int value;

    /**
     * Creates new Register object with specified value.
     *
     * @param value value to be stored in register
     */
    public Register(int value) {
        this.value = value;
    }

    /**
     * Creates new Register with initial 0 value.
     */
    public Register() {
        this(0);
    }


    /**
     * Gets value stored in register.
     *
     * @return register value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets specific bit from register value.
     *
     * @param bitIndex index of read bit
     * @return bit value at index
     */
    public int getValueAt(int bitIndex) {
        boundsCheck(bitIndex);
        return (value >> bitIndex) & 1;
    }

    /**
     * Sets value in register.
     *
     * @param value value to be stored
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Sets or clears bit in specified position in register value.
     *
     * @param bitIndex index of a changed bit
     * @param value    new value of bit
     */
    public void setValueAt(int bitIndex, boolean value) {
        boundsCheck(bitIndex);
        this.value = value ?
            this.value | (1 << bitIndex) :
            this.value & ~(1 << bitIndex);
    }

    /**
     * Checks if index is in range of a integer value.
     *
     * @param bitIndex bit index to be checked
     * @throws IndexOutOfBoundsException if out of range of integer bit range
     */
    private void boundsCheck(int bitIndex) {
        if (bitIndex > 31 || bitIndex < 0)
            throw new IndexOutOfBoundsException("Desired bit index cannot be less than 0 and more than 31.");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
