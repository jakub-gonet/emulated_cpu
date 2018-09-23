package cpu.memory;

/**
 * Provides interface used to mark devices as read only.
 *
 * @see Writable
 */
public interface Readable {
    /**
     * Reads value at given address.
     *
     * @param address an address in device memory bounds
     * @return read value
     */
    int read(int address);

    /**
     * Checks if given address can be used for reading.
     *
     * @param address an address to check
     * @return true if can read at address, false otherwise
     */
    boolean canReadAt(int address);
}
