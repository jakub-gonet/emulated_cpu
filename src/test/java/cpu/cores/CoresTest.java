package cpu.cores;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CoresTest {
    @Test
    void canCreateNCores() {
        Cores cores = new Cores(5, new MemoryManager(new Memory(5)));

        Assertions.assertEquals(5, cores.count());
    }

    @Test
    void canProvideOwnCores() {
        Core core = new Core(new MemoryManager(new Memory(5)));
        Cores cores = new Cores(List.of(core));

        Assertions.assertEquals(core, cores.byId(0));
    }
}
