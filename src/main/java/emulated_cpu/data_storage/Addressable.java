package emulated_cpu.cpu.data_storage;

public interface Addressable {
    /**
     * Reads data from a Addressable at the specified index.
     *
     * @param address address of data to be read
     * @return read data
     * @throws NullPointerException if read value is null
     */
    int read(int address);

    /**
     * Writes data to a Addressable at the specified index.
     *
     * @param address address of data
     * @param data    data to be written
     * @throws NullPointerException if data is null
     */
    void write(int address, int data);

    /**
     * Gets size of a Addressable
     *
     * @return size
     */
    int size();
}
