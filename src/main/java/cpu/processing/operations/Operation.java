package cpu.processing.operations;

import cpu.memory.MemoryManager;
import cpu.memory.Readable;
import cpu.memory.Writable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public Operation(MemoryManager manager) {
        this.manager = manager;
        this.memory = manager.readableDevice(1);
    }

    public int fetch(int currentAddress) throws IllegalStateException {
        List<Integer> args = new ArrayList<>();

        int opCodeAndAddresses = memory.read(currentAddress++);

        this.opCodeNum = InstructionDecoder.decodeOpCode(opCodeAndAddresses);
        int argNum = InstructionDecoder.decodeArgNumber(opCodeAndAddresses);

        if (argNum > InstructionDecoder.maxArgNum()) {
            throw new IllegalStateException("Exceeded max arg number: " + argNum);
        }

        for (int i = 0; i < argNum; i++) {
            int nextValue = memory.read(currentAddress++);
            int deviceIdContainingValue = InstructionDecoder.decodeAddrMode(i, opCodeAndAddresses);

            if (i == 0) {
                updateDestinationDevice(deviceIdContainingValue, nextValue);
            }

            args.add(valueFromDevice(deviceIdContainingValue, nextValue));
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

    private int valueFromDevice(int deviceId, int address) {
        Readable device = manager.readableDevice(deviceId);
        return device.read(address);
    }

    private void updateDestinationDevice(int deviceId, int address) {
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