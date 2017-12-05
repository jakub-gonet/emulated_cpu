package emulated_cpu.cpu.memory;

public interface IOInterface {
    /**
     * Reads data from IOInterface at specified index.
     *
     * @param address address of data to be read
     * @return read data
     * @throws NullPointerException if read value is null
     */
    int read(int address);

    /**
     * Writes data to IOInterface at specified index.
     *
     * @param address address of data
     * @param data    data to be written
     * @throws NullPointerException if data is null
     */
    void write(int address, int data);
}
