package cpu.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

class MemoryManagerTest {
    @Test
    void canCreateManagerWithDefaultMapping() throws InvalidKeyException {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager(mem);
        Assertions.assertEquals(mem, manager.readableWritableDevice(0));
    }

    @Test
    void canInsertSameDeviceWithDifferentIdsAndTypes() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager();
        Assertions.assertTrue(manager.addWritableDevice(0, mem));
        Assertions.assertTrue(manager.addReadableDevice(1, mem));
        Assertions.assertTrue(manager.addReadableWritableDevice(2, mem));
    }

    @Test
    void cantInsertDeviceWithExistingId() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager();
        Assertions.assertTrue(manager.addWritableDevice(0, mem));
        Assertions.assertFalse(manager.addWritableDevice(0, mem));
    }

    @Test
    void gettingDeviceTypeByDifferentTypeThrowsAnException() {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager();
        manager.addReadableDevice(0, mem);
        manager.addWritableDevice(1, mem);

        Assertions.assertThrows(InvalidKeyException.class, () -> manager.writableDevice(0));
        Assertions.assertThrows(InvalidKeyException.class, () -> manager.readableWritableDevice(1));
    }

    @Test
    void canGetDeviceWithSubtype() throws InvalidKeyException {
        Memory mem = new Memory(1);
        MemoryManager manager = new MemoryManager();
        manager.addReadableWritableDevice(0, mem);

            Assertions.assertEquals(mem, manager.readableDevice(0));
        Assertions.assertEquals(mem, manager.writableDevice(0));

    }
}