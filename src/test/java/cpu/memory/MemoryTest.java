package cpu.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MemoryTest {
    @Test
    void canCreateMemoryWithSpecifiedSize() {
        Memory mem = new Memory(8);

        Assertions.assertEquals(8, mem.size());
    }

    @Test
    void cantCreateMemoryWithNegativeSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Memory(-1));
    }

    @Test
    void canCreateMemoryFromGivenList() {
        Memory mem = new Memory(List.of(1, 2, 3));

        Assertions.assertEquals(List.of(1, 2, 3), mem.rawContent());
    }

    @Test
    void canCheckIfGivenAddressIsInMemoryBounds() {
        Memory mem = new Memory(3);

        Assertions.assertTrue(mem.canReadAt(2));
        Assertions.assertTrue(mem.canWriteAt(2));

        Assertions.assertFalse(mem.canReadAt(-1));
        Assertions.assertFalse(mem.canReadAt(3));
        Assertions.assertFalse(mem.canWriteAt(-1));
        Assertions.assertFalse(mem.canWriteAt(3));
    }

    @Test
    void cantReadAndWriteBeyondMemoryBounds() {
        Memory mem = new Memory(3);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> mem.read(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> mem.read(5));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> mem.write(-1, 0xcafe));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> mem.write(5, 0xcafe));
    }

    @Test
    void canReadAndWriteInsideMemoryBounds() {
        Memory mem = new Memory(3);
        
        mem.write(0, 1);
        mem.write(2, 2);
        Assertions.assertEquals(1, mem.read(0));
        Assertions.assertEquals(2, mem.read(2));
    }
}