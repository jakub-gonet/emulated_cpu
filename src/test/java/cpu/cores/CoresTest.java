package cpu.cores;

import cpu.memory.Memory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CoresTest {
    @Test
    void canCreateNCores() {
        Cores cores = new Cores(5, new Memory(5));

        Assertions.assertEquals(5, cores.count());
    }

    @Test
    void canProvideOwnCores() {
        Core core = new Core(new Memory(5));
        Cores cores = new Cores(List.of(core), new Memory(5));

        Assertions.assertEquals(core, cores.byId(0));
    }
}
