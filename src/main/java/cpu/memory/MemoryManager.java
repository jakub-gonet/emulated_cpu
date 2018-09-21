package cpu.memory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryManager {
    private Map<Integer, Readable> readableDeviceMapping = new HashMap<>();
    private Map<Integer, Writable> writableDeviceMapping = new HashMap<>();

    public boolean addDevice(int id, Readable device) {
        if (readableDeviceMapping.get(id) != null) return false;

        readableDeviceMapping.put(id, device);
        return true;
    }

    public boolean addDevice(int id, Writable device) {
        if (writableDeviceMapping.get(id) != null) return false;

        writableDeviceMapping.put(id, device);
        return true;
    }

    public Readable readableDevice(int id) throws InvalidKeyException {
        return (Readable) mappingOrThrow(readableDeviceMapping, id);
    }

    public Writable writableDevice(int id) throws InvalidKeyException {
        return (Writable) mappingOrThrow(writableDeviceMapping, id);
    }

    private Object mappingOrThrow(Map mapping, int id) throws InvalidKeyException {
        return Optional.ofNullable(mapping.get(id)).orElseThrow(() -> new InvalidKeyException("Object with id " + id + " not found in mapping: " + mapping.toString()));
    }
}
