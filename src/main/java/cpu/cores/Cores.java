package cpu.cores;

import cpu.memory.MemoryManager;

import java.util.ArrayList;
import java.util.List;

public class Cores {
    private List<Core> cores;

    public Cores(int count, MemoryManager manager) {
        cores = createCores(count, manager);
    }

    public Cores(List<Core> cores) {
        this.cores = cores;
    }

    /**
     * Gets core by given id
     *
     * @param id describes core id
     * @return Core object
     */
    Core byId(int id) {
        return cores.get(id);
    }

    /**
     * Calculates number of cores
     *
     * @return core count
     */
    int count() {
        return cores.size();
    }

    private List<Core> createCores(int count, MemoryManager manager) {
        List<Core> cores = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cores.add(new Core(manager));
        }
        return cores;
    }

}
