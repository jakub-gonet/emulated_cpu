package cpu.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemoryManagerTest {
    @Test
    void canCreateManagerWithDefaultMapping() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager(mem);

        Assertions.assertEquals(mem, manager.readableWritableDevice(1));
    }

    @Test
    void canInsertSameDeviceWithDifferentIdsAndTypes() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager(mem);
        Assertions.assertTrue(manager.addWritableDevice(16, mem));
        Assertions.assertTrue(manager.addReadableDevice(17, mem));
        Assertions.assertTrue(manager.addReadableWritableDevice(18, mem));
    }

    @Test
    void cantInsertDeviceWithExistingId() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager(mem);
        Assertions.assertTrue(manager.addWritableDevice(16, mem));
        Assertions.assertFalse(manager.addWritableDevice(16, mem));
    }

    @Test
    void gettingDeviceTypeByDifferentTypeThrowsAnException() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager(mem);
        manager.addReadableDevice(16, mem);
        manager.addWritableDevice(17, mem);

        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.writableDevice(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.readableWritableDevice(17));
    }

    @Test
    void canGetDeviceWithSubtype() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager(mem);
        manager.addReadableWritableDevice(16, mem);

        Assertions.assertEquals(mem, manager.readableDevice(16));
        Assertions.assertEquals(mem, manager.writableDevice(16));

    }
}