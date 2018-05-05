package emulated_cpu.data_storage.program_storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ProgramHolderManager {
    private static Logger logger = LogManager.getLogger(ProgramHolderManager.class);
    private static ProgramHolder currentProgramHolder = null;
    private static Map<Integer, ProgramHolder> programHolders = new HashMap<>();
    private static int lastId = 0;

    private ProgramHolderManager() {
    }

    /**
     * Adds a ProgramHolder to ProgramHolders table
     *
     * @param programHolder ProgramHolder to add
     * @return id of a added ProgramHolder
     */
    public static Integer addProgramHolder(ProgramHolder programHolder) {
        if (!programHolders.containsValue(programHolder)) {
            logger.info(MessageFormat.format("Adding new program holder '{0}', with id={1}", programHolder, lastId + 1));
            programHolders.put(lastId++, programHolder);
            return lastId;
        }
        logger.error(MessageFormat.format("Program holder '{0}' exists", programHolder));
        return null;
    }

    /**
     * Removes a ProgramHolder from ProgramHolders table
     *
     * @param id id of a ProgramHolder to remove
     * @return true if succeeded, false otherwise
     */
    public static boolean removeProgramHolder(int id) {
        if (programHolders.containsKey(id)) {
            logger.info(MessageFormat.format("Removing program holder '{0}', with id={1}", programHolders.get(id), lastId + 1));
            programHolders.remove(id);
            return true;
        }
        logger.error(MessageFormat.format("Program holder with id={0} doesn't exist", id));
        return false;
    }

    /**
     * Sets ProgramHolder with given id as current
     *
     * @param id id of a ProgramHolder to set as current
     * @return true if ProgramHolder exists and set, false otherwise
     */
    public static boolean setCurrentProgramHolder(int id) {
        if (programHolders.containsKey(id)) {
            ProgramHolder programholder = programHolders.get(id);
            logger.debug(MessageFormat.format("Setting program holder '{0}', with id={1} as current", programholder, lastId + 1));
            currentProgramHolder = programholder;
            return true;
        }
        logger.error(MessageFormat.format("Program holder with id={0} doesn't exist", id));
        return false;
    }

    /**
     * Gets current ProgramHolder
     *
     * @return ProgramHolder
     */
    public static ProgramHolder getCurrentProgramHolder() {
        if (currentProgramHolder == null) {
            logger.error("Current Program holder is null");
        }
        return currentProgramHolder;
    }
}
