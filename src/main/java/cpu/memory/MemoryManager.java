package cpu.memory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryManager {
    private enum Type {
        READABLE,
        WRITABLE,
        READABLE_WRITABLE
    }

    private Map<Integer, Map<Type, Object>> deviceMapping = new HashMap<>();

    public MemoryManager(Memory mem) {
        addReadableWritableDevice(0, mem);
    }

    public <T extends Readable & Writable> boolean addReadableWritableDevice(int id, T device) {
        return addDevice(id, Type.READABLE_WRITABLE, device);
    }

    public boolean addReadableDevice(int id, Readable device) {
        return addDevice(id, Type.READABLE, device);
    }

    public boolean addWritableDevice(int id, Writable device) {
        return addDevice(id, Type.WRITABLE, device);
    }

    public Readable readableDevice(int id) throws InvalidKeyException {
        return (Readable) deviceByType(deviceWithTypeById(id), Type.READABLE);
    }

    public Writable writableDevice(int id) throws InvalidKeyException {
        return (Writable) deviceByType(deviceWithTypeById(id), Type.WRITABLE);
    }

    public <T extends Writable & Readable> T readableWritableDevice(int id) throws InvalidKeyException {
        return (T) deviceByType(deviceWithTypeById(id), Type.READABLE_WRITABLE);
    }

    private Object deviceByType(Map<Type, Object> deviceWithType, Type type) throws InvalidKeyException {
        return Optional.ofNullable(deviceWithType.get(type))
                       .orElseThrow(() -> new InvalidKeyException("Device with type" + deviceWithType + " is not " + type));
    }

    private Map<Type, Object> deviceWithTypeById(int id) throws InvalidKeyException {
        return Optional.ofNullable(deviceMapping.get(id))
                       .orElseThrow(() -> new InvalidKeyException("Object with id " + id + " not found"));
    }

    private boolean addDevice(int id, Type type, Object device) {
        if (isIdNotUsed(id)) {
            deviceMapping.put(id, createDeviceMapping(type, device));
            return true;
        }
        return false;
    }

    private boolean isIdNotUsed(int id) {
        return !deviceMapping.containsKey(id);
    }

    private Map<Type, Object> createDeviceMapping(Type type, Object device) {
        return new HashMap<>(Map.of(type, device));
    }
}
