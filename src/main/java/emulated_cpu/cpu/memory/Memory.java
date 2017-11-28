package emulated_cpu.cpu.memory;

import java.util.ArrayList;

/**
 * Memory is a class containing program memory. This memory is used to store program, variables and stack.
 */
public class Memory implements IOInterface {
    private static Memory instance;

    private ArrayList<Integer> memory;

    private Memory() {
        memory = new ArrayList<>();
    }

    /**
     * Gets instance of Memory or creates it if nonexistent.
     *
     * @return instance of Memory class
     */
    public static Memory getInstance() {
        if (instance == null)
            instance = new Memory();
        return instance;
    }

    /**
     * Swaps content of memory.
     *
     * @param memory new memory to be loaded
     * @throws IllegalArgumentException when new memory is null
     */
    public void setMemory(ArrayList<Integer> memory) {
        if (memory == null)
            throw new IllegalArgumentException("ERROR: memory object can't be null.");
        if (memory.contains(null))
            throw new IllegalArgumentException("ERROR: memory object can't contain null value.");

        this.memory = memory;
    }

    /**
     * Gets memory content.
     *
     * @return memory content
     */
    public ArrayList<Integer> getMemory() {
        return memory;
    }

    /**
     * Reads data from memory.
     *
     * @param address address of data to be read
     * @return read data from memory
     * @throws IndexOutOfBoundsException when reading from nonexistent memory address
     * @throws NullPointerException      when read value is null
     */
    @Override
    public int read(int address) {
        return memory.get(address);
    }

    /**
     * Writes data to memory.
     *
     * @param address address of data.
     * @param data    data to be written in memory
     * @throws NullPointerException when data is null
     */
    @Override
    public void write(int address, int data) {
        memory.set(address, data);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        memory.forEach(x -> str.append(x)
                               .append(", "));
        str.delete(str.length() - 2, str.length());
        str.append("]");

        return str.toString();
    }
}
