package cpu.memory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryManager {
    private Map<Integer, Readable> readableDeviceMapping = new HashMap<>();
    private Map<Integer, Writable> writableDeviceMapping = new HashMap<>();

    public boolean addReadableDevice(int id, Readable device) {
        if (readableDeviceMapping.get(id) != null) return false;

    public boolean addReadableDevice(int id, Readable device) {
        if (isIdNotUsed(readableDeviceMapping, id)) {
            readableDeviceMapping.put(id, device);
            return true;
        }
        return false;
    }

    public boolean addWritableDevice(int id, Writable device) {
        if (isIdNotUsed(writableDeviceMapping, id)) {
            writableDeviceMapping.put(id, device);
            return true;
        }
        return false;
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

    private boolean isIdNotUsed(Map mapping, int id) {
        return !mapping.containsKey(id);
    }
}
