package emulated_cpu.data_storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * AddressableManager is a class responsible for managing current addressable. Default addressable should be instance of Memory class.
 */
public final class AddressableManager {
    private static Logger logger = LogManager.getLogger(AddressableManager.class);

    private static Map<Integer, Addressable> addressables = new HashMap<>();
    private static int lastId = 0;

    private AddressableManager() {
    }

    /**
     * Adds a Addressable to Addressables table
     *
     * @param addressable Addressable to add
     * @return id of a added Addressable
     */
    public static Integer addAddressable(Addressable addressable) {
        if (!addressables.containsValue(addressable)) {
            logger.info(MessageFormat.format("Adding new addressable '{0}', with id={1}", addressable, lastId + 1));
            addressables.put(lastId++, addressable);
            return lastId;
        }
        logger.error(MessageFormat.format("Addressable '{0}' exists", addressable));
        return null;
    }

    /**
     * Removes a Addressable from Addressables table
     *
     * @param id id of a Addressable to remove
     * @return true if succeeded, false otherwise
     */
    public static boolean removeAddressable(int id) {
        if (addressables.containsKey(id)) {
            logger.info(MessageFormat.format("Removing addressable '{0}', with id={1}", addressables.get(id), lastId + 1));
            addressables.remove(id);
            return true;
        }
        logger.error(MessageFormat.format("Addressable with id={0} doesn't exist", id));
        return false;
    }

    /**
     * Gets Addressable by id
     *
     * @param id id of a Addressable in Addressales table
     * @return Addressable
     */
    public static Addressable getAddressableById(int id) {
        if (addressables.containsKey(id)) {
            logger.trace(MessageFormat.format("Taking Addressable with id {0}", id));
            return addressables.get(id);
        }
        throw new IllegalArgumentException(MessageFormat.format("Addressable with id {0} doesn't exists", id));
    }
}
