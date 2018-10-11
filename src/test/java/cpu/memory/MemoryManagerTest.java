package cpu.memory;

import cpu.memory.registers.Registers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryManagerTest {
    private MemoryManager manager;
    private Memory mem;

    @BeforeEach
    void setup() {
        mem = new Memory(1);
        manager = new MemoryManager(mem);
    }

    @Test
    void canCopyMemoryManager() {
        MemoryManager memoryManager = new MemoryManager(manager);
        Assertions.assertNotEquals(memoryManager, manager);
    }

    @Test
    void canCreateManagerWithDefaultMapping() {
        Assertions.assertEquals(mem, manager.readableWritableDevice(1));
    }

    @Test
    void canExtendManagerMappingWithRegisters() {
        Registers registers = new Registers(2);

        MemoryManager memoryManager = new MemoryManager(manager, registers);
        Assertions.assertDoesNotThrow(() -> memoryManager.readableDevice(2));
        Assertions.assertDoesNotThrow(() -> memoryManager.readableDevice(3));
    }

    @Test
    void canInsertSameDeviceWithDifferentIdsAndTypes() {
        Assertions.assertTrue(manager.addWritableDevice(16, mem));
        Assertions.assertTrue(manager.addReadableDevice(17, mem));
        Assertions.assertTrue(manager.addReadableWritableDevice(18, mem));
    }

    @Test
    void cantInsertDeviceWithExistingId() {
        Assertions.assertTrue(manager.addWritableDevice(16, mem));
        Assertions.assertFalse(manager.addWritableDevice(16, mem));
    }

    @Test
    void gettingDeviceTypeByDifferentTypeThrowsAnException() {
        manager.addReadableDevice(16, mem);
        manager.addWritableDevice(17, mem);

        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.writableDevice(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.readableWritableDevice(17));
    }

    @Test
    void canGetDeviceWithSubtype() {
        manager.addReadableWritableDevice(16, mem);

        Assertions.assertEquals(mem, manager.readableDevice(16));
        Assertions.assertEquals(mem, manager.writableDevice(16));
    }

    @Test
    void cantUseNotRegisteredDevice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.writableDevice(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.readableDevice(17));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.readableWritableDevice(18));

    }
}