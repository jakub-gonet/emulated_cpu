package cpu.memory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Memory implements Readable, Writable {
    private Logger logger = LogManager.getLogger(Memory.class);
    private List<Integer> memory;

    public Memory(int size) {
        logger.info("Creating Memory object having {} capacity", size);
        memory = new ArrayList<>(Collections.nCopies(size, 0));
    }

    public Memory(List<Integer> initial){
        logger.info("Creating Memory object with initial value from {}", initial);
        memory = new ArrayList<>(initial);
    }

    @Override
    public int read(int address) {
        if (canReadAt(address)) {
            int value = memory.get(address);
            logger.trace("Reading from {} memory object in address {}: {}", this, address, value);
            return value;
        }
        throw new IndexOutOfBoundsException("Can't read at address " + address);
    }

    @Override
    public boolean canReadAt(int address) {
        return isInMemoryBounds(address);
    }

    @Override
    public void write(int address, int data) {
        if (canWriteAt(address)) {
            memory.set(address, data);
            return;
        }
        throw new IndexOutOfBoundsException("Can't write at address " + address);
    }

    @Override
    public boolean canWriteAt(int address) {
        return isInMemoryBounds(address);
    }

    public List<Integer> rawContent() {
        return new ArrayList<>(memory);
    }

    public int size() {
        return memory.size();
    }

    private boolean isInMemoryBounds(int address) {
        return address >= 0 && address < memory.size();
    }
}
