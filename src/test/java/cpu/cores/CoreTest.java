package cpu.cores;

import cpu.Helpers;
import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CoreTest {
    @Test
    void canExecuteNextOperation() {
        int hltOpCode = Helpers.opCode(1, 0, 0, 0);
        MemoryManager memoryManager = new MemoryManager(new Memory(List.of(hltOpCode)));
        Core core = new Core(memoryManager);

        core.executeNext();
        Assertions.assertTrue(core.isStopped());
    }

    @Test
    void canRestart() {
        Core core = new Core(new MemoryManager((new Memory(5))));

        core.restart();
    }

    @Test
    void newlyCreatedCoreIsntStopped() {
        Core core = new Core(new MemoryManager((new Memory(5))));

        Assertions.assertFalse(core.isStopped());
    }
}