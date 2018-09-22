package cpu;

import cpu.cores.Cores;
import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import cpu.memory.Readable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Cpu {
    private Logger logger = LogManager.getLogger(Cpu.class);

    private Memory memory;
    private Cores cores;

    Cpu(int memorySize, int coresCount) {
        memory = new Memory(memorySize);
        cores = new Cores(coresCount, new MemoryManager(memory));
    }

    /**
     * Loads data with given length to main memory, using offsets
     *
     * @param device           Readable device, source of data
     * @param deviceDataOffset data offset in device
     * @param dataLength       size of data to copy
     * @param memoryOffset     storing offset in memory
     * @return true if copied successfully, false otherwise
     */
    boolean loadIntoMemory(Readable device, int deviceDataOffset, int dataLength, int memoryOffset) {
        logger.info("Loading program from {} into memory", device);

        for (int i = 0; i < dataLength; i++) {
            if (!canReadFromDevice(device, deviceDataOffset + i) || !canWriteToMemory(memory, memoryOffset + i)) {
                return false;
            }

            memory.write(memoryOffset + i, device.read(deviceDataOffset + i));
        }
        return true;
    }


    /**
     * Returns memory reference
     *
     * @return memory object
     */
    public Memory memory() {
        return memory;
    }

    /**
     * Checks if read can be performed at given device address
     *
     * @param device        checked device
     * @param deviceAddress checked address
     * @return true when read can be done, false otherwise
     */
    private boolean canReadFromDevice(Readable device, int deviceAddress) {
        if (!device.canReadAt(deviceAddress)) {
            logger.error("Can't read data from device {} at address {}", device, deviceAddress);
            return false;
        }
        return true;
    }

    /**
     * Checks if write can be performed at given memory address
     *
     * @param memory     cu memory
     * @param memAddress checked address
     * @return true when write can be done, false otherwise
     */
    private boolean canWriteToMemory(Memory memory, int memAddress) {
        if (!memory.canWriteAt(memAddress)) {
            logger.error("Can't read data from device {} at address {}", memory, memAddress);
            return false;
        }
        return true;
    }

}
