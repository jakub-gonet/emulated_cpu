package cpu.cores;

import cpu.memory.Memory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CoreTest {
    @Test
    void canExecuteNextOperation() {
        int hltOpCode = 5;
        Core core = new Core(new Memory(List.of(hltOpCode)));

        core.executeNext();
        Assertions.assertTrue(core.isStopped());
    }

    @Test
    void canRestart() {
        Core core = new Core(new Memory(5));

        core.restart();
    }

    @Test
    void newlyCreatedCoreIsntStopped() {
        Core core = new Core(new Memory(5));

        Assertions.assertFalse(core.isStopped());
    }
}