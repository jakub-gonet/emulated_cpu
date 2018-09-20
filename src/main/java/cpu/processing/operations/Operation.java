package cpu.processing.operations;

import cpu.memory.MemoryManager;
import cpu.memory.Readable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class Operation {
    private Logger logger = LogManager.getLogger(Operation.class);
    private final int addrModeLength = 3;
    private final int addrModeBitFieldLength = (int) Math.pow(2, addrModeLength) - 1;
    private final int argNumLength = 2;
    private final int argNumBitFieldLength = (int) Math.pow(2, argNumLength) - 1;
    private Readable memory;
    private MemoryManager manager;

    public Operation(Readable memory, MemoryManager manager) {
        this.manager = manager;
        this.memory = memory;
    }

    public int fetch(int currentAddress) throws IllegalStateException {
        List<Integer> args = new ArrayList<>();

        int opCodeAndAddresses = memory.read(currentAddress++);
        int argNum = (opCodeAndAddresses >> 2 * addrModeLength) & argNumBitFieldLength;

        for (int i = argNum - 1; i >= 0; i--) {
            int nextValue = memory.read(currentAddress++);
            int deviceIdContainingValue = (opCodeAndAddresses >> i * addrModeLength) & addrModeBitFieldLength;

            try {
                args.add(valueFromDevice(deviceIdContainingValue, nextValue));
            } catch (InvalidKeyException e) {
                logger.error("Invalid device id: " + deviceIdContainingValue);
                throw new IllegalStateException("Invalid device id in argument");
            } catch (IllegalAccessException e) {
                logger.error("Device with " + deviceIdContainingValue + " id is not Readable");
                throw new IllegalStateException("Device not Readable");
            }
        }

        return currentAddress;
    }

    private int valueFromDevice(int deviceId, int address) throws InvalidKeyException, IllegalAccessException {
        Readable device = manager.ReadableDevice(deviceId);
        return device.read(address);
    }
}