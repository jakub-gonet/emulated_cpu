package cpu.cores;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CoresTest {
    @Test
    void canCreateNCores() {
        Cores cores = new Cores(5, createMemManagerWithMemory(new Memory(5)));

        Assertions.assertEquals(5, cores.count());
    }

    @Test
    void canProvideOwnCores() {
        Core core = new Core(createMemManagerWithMemory(new Memory(5)));
        Cores cores = new Cores(List.of(core));

        Assertions.assertEquals(core, cores.byId(0));
    }

    private MemoryManager createMemManagerWithMemory(Memory mem) {
        MemoryManager memManager = new MemoryManager();
        memManager.addReadableWritableDevice(0, mem);
        return memManager;
    }
}
