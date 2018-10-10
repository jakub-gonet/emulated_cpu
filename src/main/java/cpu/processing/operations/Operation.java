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

    private Readable memory;
    private MemoryManager manager;

    private int opCodeNum;
    private List<Integer> args;

    public Operation(MemoryManager manager) throws InvalidKeyException {
        this.manager = manager;
        this.memory = manager.readableDevice(0);
    }

    public int fetch(int currentAddress) throws IllegalStateException {
        final int addrModeLength = 3;
        final int addrModeBitFieldLength = (int) Math.pow(2, addrModeLength) - 1;
        final int argNumLength = 2;
        final int argNumBitFieldLength = (int) Math.pow(2, argNumLength) - 1;
        final int maxArgNum = 2;

        List<Integer> args = new ArrayList<>();

        int opCodeAndAddresses = memory.read(currentAddress++);
        int argNum = (opCodeAndAddresses >> maxArgNum * addrModeLength) & argNumBitFieldLength;
        if (argNum > maxArgNum)
            throw new IllegalStateException("Exceeded max arg number: " + argNum);

        for (int i = 0; i < argNum; i++) {
            int nextValue = memory.read(currentAddress++);
            int deviceIdContainingValue = (opCodeAndAddresses >> (maxArgNum - i - 1) * addrModeLength) & addrModeBitFieldLength;

            try {
                args.add(valueFromDevice(deviceIdContainingValue, nextValue));
            } catch (InvalidKeyException e) {
                logger.error("Invalid device id: " + deviceIdContainingValue);
                throw new IllegalStateException("Invalid device id in argument");
            }
        }

        this.opCodeNum = opCodeAndAddresses >> (maxArgNum * addrModeLength + argNumLength);
        this.args = args;
        return currentAddress;
    }

    private int valueFromDevice(int deviceId, int address) throws InvalidKeyException {
        Readable device = manager.readableDevice(deviceId);
        return device.read(address);
    }

    public int opCodeNum() {
        return opCodeNum;
    }

    public List<Integer> args() {
        return new ArrayList<>(args);
    }
}