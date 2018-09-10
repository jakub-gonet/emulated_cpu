package cpu.memory.registers;

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
        value = 0;
    }

    /**
     * Gets value stored in register.
     *
     * @return register value
     */
    int value() {
        return value;
    }

    /**
     * Gets specific bit from register value.
     *
     * @param bitIndex index of read bit
     * @return bit value at index
     */
    int valueAt(int bitIndex) {
        return (value >> bitIndex) & 1;
    }

    /**
     * Sets value in register.
     *
     * @param value value to be stored
     */
    void set(int value) {
        this.value = value;
    }

    /**
     * Sets or clears bit in specified position in register value.
     *
     * @param bitIndex index of a changed bit
     * @param value    new value of bit
     */
    void setValueAt(int bitIndex, boolean value) {
        this.value = value ?
                this.value | (1 << bitIndex) :
                this.value & ~(1 << bitIndex);
    }
}
