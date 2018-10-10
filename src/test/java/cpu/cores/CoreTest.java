package cpu.cores;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.util.List;

class CoreTest {
    @Test
    void canExecuteNextOperation() throws InvalidKeyException {
        int hltOpCode = 5 << 8 | 0 << 6;
        Core core = new Core(new MemoryManager((new Memory(List.of(hltOpCode)))));

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