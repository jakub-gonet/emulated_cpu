package cpu.memory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryManager {
    private Map<Integer, Device> deviceMapping = new HashMap<>();

    public boolean addDevice(int id, Device device) {
        if (deviceMapping.get(id) != null) return false;

        deviceMapping.put(id, device);
        return true;
    }

    public Readable ReadableDevice(int id) throws InvalidKeyException, IllegalAccessException {
        Device device = deviceOrThrow(id);

        if (!device.isReadable()) throw new IllegalAccessException("Device with id " + id + " is not Readable");
        return (Readable) device.self();
    }

    private Device deviceOrThrow(int id) throws InvalidKeyException {
        return Optional.ofNullable(deviceMapping.get(id)).orElseThrow(() -> new InvalidKeyException("Device with id " + id + " not found"));
    }
}
