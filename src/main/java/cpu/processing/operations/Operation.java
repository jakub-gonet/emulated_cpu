package cpu.processing.operations;

import cpu.memory.MemoryManager;
import cpu.memory.Readable;
import cpu.memory.Writable;
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
    private Writable destinationDevice;
    private int destinationAddress;

    public Operation(MemoryManager manager) throws InvalidKeyException {
        this.manager = manager;
        this.memory = manager.readableDevice(0);
    }

    public int fetch(int currentAddress) throws IllegalStateException {
        List<Integer> args = new ArrayList<>();

        int opCodeAndAddresses = memory.read(currentAddress++);

        this.opCodeNum = InstructionDecoder.decodeOpCode(opCodeAndAddresses);
        int argNum = InstructionDecoder.decodeArgNumber(opCodeAndAddresses);

        if (argNum > InstructionDecoder.maxArgNum()) {
            throw new IllegalStateException("Exceeded max arg number: " + argNum);
        }

        for (int i = 0; i < argNum; i++, currentAddress++) {
            int nextValue = memory.read(currentAddress);
            int deviceIdContainingValue = InstructionDecoder.decodeAddrMode(i, opCodeAndAddresses);

            try {
                if (i == 0) {
                    updateDestinationDevice(deviceIdContainingValue, currentAddress);
                }

                args.add(valueFromDevice(deviceIdContainingValue, nextValue));
            } catch (InvalidKeyException e) {
                logger.error("Invalid device id: " + deviceIdContainingValue);
                throw new IllegalStateException("Invalid device id in argument");
            }
        }

        this.args = args;
        return currentAddress;
    }

    public Writable destinationDevice() {
        return destinationDevice;
    }

    public int destinationAddress() {
        return destinationAddress;
    }

    private int valueFromDevice(int deviceId, int address) throws InvalidKeyException {
        Readable device = manager.readableDevice(deviceId);
        return device.read(address);
    }

    private void updateDestinationDevice(int deviceId, int address) throws InvalidKeyException {
        destinationDevice = manager.writableDevice(deviceId);
        destinationAddress = address;
    }

    public int opCodeNum() {
        return opCodeNum;
    }

    public List<Integer> args() {
        return new ArrayList<>(args);
    }
}