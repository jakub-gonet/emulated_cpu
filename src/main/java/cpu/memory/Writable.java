package cpu.memory;

/**
 * Interface used to mark devices as write only.
 *
 * @see Readable
 */
public interface Writable {
    /**
     * Writes the data at given address.
     *
     * @param address an address in device memory bounds
     * @param data    data to write
     */
    void write(int address, int data);

    /**
     * Checks if given address can be used for writing.
     *
     * @param address an address to check
     * @return true if can write at address, false otherwise
     */
    boolean canWriteAt(int address);
}
