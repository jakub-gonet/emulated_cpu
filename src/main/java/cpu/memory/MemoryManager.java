package cpu.memory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Manages different devices implementing Readable or Writable interfaces (or both).
 *
 * <p>
 * Default mapping is assumed to be as follows:
 *
 * <table>
 * <tr>
 * <th>ID</th>
 * <th>Name</th>
 * </tr>
 *
 * <tr>
 * <td>
 * 0
 * </td>
 * <td>
 * Memory
 * </td>
 * </tr>
 *
 * <tr>
 * <td>
 * 1
 * </td>
 * <td>
 * Registers
 * </td>
 * </tr>
 *
 * <tr>
 * <td>
 * 2
 * </td>
 * <td>
 * Indirect Memory
 * </td>
 * </tr>
 *
 * <tr>
 * <td>
 * 2
 * </td>
 * <td>
 * Indirect Memory by Register
 * </td>
 * </tr>
 *
 * </table>
 * </p>
 */
public class MemoryManager {
    /**
     * Allowed types of device
     */
    private enum Type {
        READABLE,
        WRITABLE,
        READABLE_WRITABLE
    }

    private Map<Integer, Map<Type, Object>> deviceMapping = new HashMap<>();

    /**
     * Constructs a new MemoryManager without any mapping.
     */
    public MemoryManager() {}

    /**
     * Constructs a new MemoryManager with provided Memory mapped with <code>id = 0</code>
     *
     * @param mem a Memory object
     */
    public MemoryManager(Memory mem) {
        addReadableWritableDevice(0, mem);
    }

    /**
     * Adds given Readable and Writable device to mapping.
     *
     * @param id     an id mapping number to device
     * @param device a class implementing Readable and Writable interface
     * @return true if added successfully, false otherwise
     */
    public <T extends Readable & Writable> boolean addReadableWritableDevice(int id, T device) {
        return addDevice(id, Type.READABLE_WRITABLE, device);
    }

    /**
     * Adds given Readable device to mapping.
     *
     * @param id     an id mapping number to device
     * @param device a class implementing Readable interface
     * @return true if added successfully, false otherwise
     */
    public boolean addReadableDevice(int id, Readable device) {
        return addDevice(id, Type.READABLE, device);
    }

    /**
     * Adds given Writable device to mapping.
     *
     * @param id     an id mapping number to device
     * @param device a class implementing Writable interface
     * @return true if added successfully, false otherwise
     */
    public boolean addWritableDevice(int id, Writable device) {
        return addDevice(id, Type.WRITABLE, device);
    }

    /**
     * Returns a Readable device from mapping by id.
     *
     * @param id a number mapped to some device
     * @return device implementing Readable interface
     * @throws InvalidKeyException if given id wasn't mapped to any device or if device with given id didn't implement Readable
     */
    public Readable readableDevice(int id) throws InvalidKeyException {
        return (Readable) deviceByTypes(deviceWithTypeById(id), List.of(Type.READABLE, Type.READABLE_WRITABLE));
    }

    /**
     * Returns a Writable device from mapping by id.
     *
     * @param id a number mapped to some device
     * @return device implementing Writable interface
     * @throws InvalidKeyException if given id wasn't mapped to any device or if device with given id didn't implement Writable
     */
    public Writable writableDevice(int id) throws InvalidKeyException {
        return (Writable) deviceByTypes(deviceWithTypeById(id), List.of(Type.WRITABLE, Type.READABLE_WRITABLE));
    }

    /**
     * Returns a Readable and Writable device from mapping by id.
     *
     * @param id a number mapped to some device
     * @return device implementing Readable and Writable interface
     * @throws InvalidKeyException if given id wasn't mapped to any device or if device with given id didn't implement Readable and Writable
     */
    @SuppressWarnings("unchecked")
    public <T extends Writable & Readable> T readableWritableDevice(int id) throws InvalidKeyException {
        return (T) deviceByTypes(deviceWithTypeById(id), List.of(Type.READABLE_WRITABLE));
    }

    /**
     * Returns Object implementing Readable, Writable or both based on given types list.
     *
     * @param deviceWithType a map of type Map<Type, Object>
     * @param types          a list of types to check
     * @return Object implementing Readable, Writable or both
     * @throws InvalidKeyException if given map didn't contain any of given types
     */
    private Object deviceByTypes(Map<Type, Object> deviceWithType, List<Type> types) throws InvalidKeyException {
        for (Type type : types) {
            if (deviceWithType.containsKey(type)) {
                return deviceWithType.get(type);
            }
        }
        throw new InvalidKeyException("Device " + deviceWithType + " is not of " + types);
    }

    /**
     * Returns Map<Type, Object> from mapping by given id.
     *
     * @param id
     * @return map of Type mapped to Object implementing Readable, Writable or both
     * @throws InvalidKeyException if mapping didn't contain given device with given id
     */
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

    /**
     * Checks if id is already in use.
     *
     * @param id
     * @return true if not used, false otherwise
     */
    private boolean isIdNotUsed(int id) {
        return !deviceMapping.containsKey(id);
    }

    /**
     * Creates new Map with given type and device.
     *
     * @param type   a value from Type enum
     * @param device device implementing Readable, Writable or both
     * @return a newly created map
     */
    private Map<Type, Object> createDeviceMapping(Type type, Object device) {
        return new HashMap<>(Map.of(type, device));
    }
}
