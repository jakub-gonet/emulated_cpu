package cpu.cores;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CoreTest {
    @Test
    void canExecuteNextOperation() {
        int hltOpCode = 5;
        Core core = new Core(createMemManagerWithMemory(new Memory(List.of(hltOpCode))));

        core.executeNext();
        Assertions.assertTrue(core.isStopped());
    }

    @Test
    void canRestart() {
        Core core = new Core(createMemManagerWithMemory(new Memory(5)));

        core.restart();
    }

    @Test
    void newlyCreatedCoreIsntStopped() {
        Core core = new Core(createMemManagerWithMemory(new Memory(5)));

        Assertions.assertFalse(core.isStopped());
    }

    private MemoryManager createMemManagerWithMemory(Memory mem) {
        MemoryManager memManager = new MemoryManager();
        memManager.addReadableWritableDevice(0, mem);
        return memManager;
    }
}