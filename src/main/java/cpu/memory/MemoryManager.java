package cpu.memory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryManager {
    private enum Type {
        READABLE,
        WRITABLE,
        READABLE_WRITABLE
    }

    private Map<Integer, Map<Type, Object>> deviceMapping = new HashMap<>();

    public MemoryManager() {}

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
        return (Readable) deviceByTypes(deviceWithTypeById(id), List.of(Type.READABLE, Type.READABLE_WRITABLE));
    }

    public Writable writableDevice(int id) throws InvalidKeyException {
        return (Writable) deviceByTypes(deviceWithTypeById(id), List.of(Type.WRITABLE, Type.READABLE_WRITABLE));
    }

    public <T extends Writable & Readable> T readableWritableDevice(int id) throws InvalidKeyException {
        return (T) deviceByTypes(deviceWithTypeById(id), List.of(Type.READABLE_WRITABLE));
    }

    private Object deviceByTypes(Map<Type, Object> deviceWithType, List<Type> types) throws InvalidKeyException {
        for (Type type : types) {
            if (deviceWithType.containsKey(type)) {
                return deviceWithType.get(type);
            }
        }
        throw new InvalidKeyException("Device " + deviceWithType + " is not of " + types);
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
