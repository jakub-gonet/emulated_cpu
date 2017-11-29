package emulated_cpu.cpu.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Memory is a class containing program memory. This memory is used to store program, variables and stack.
 */
public class Memory implements IOInterface {
    private static Memory instance;

    private List<Integer> memory;

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
     * NOTE: new memory containing null values leads to undefined behaviour.
     *
     * @param memory new memory to be loaded
     * @throws IllegalArgumentException if new memory is null
     */
    public void setMemory(List<Integer> memory) {
        if (memory == null)
            throw new NullPointerException("Memory object can't be null.");

        this.memory = new ArrayList<>(memory);
    }

    /**
     * Gets memory content.
     *
     * @return memory content
     */
    public List<Integer> getMemory() {
        return memory;
    }

    /**
     * Reads data from memory.
     *
     * @param address address of data to be read
     * @return read data from memory
     * @throws IndexOutOfBoundsException if reading from nonexistent memory address
     * @throws NullPointerException      if read value is null
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
     * @throws NullPointerException if data is null
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
