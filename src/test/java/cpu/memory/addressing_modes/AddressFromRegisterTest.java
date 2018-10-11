package cpu.memory.addressing_modes;

import cpu.memory.Memory;
import cpu.memory.registers.Registers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class AddressFromRegisterTest {
    private AddressFromRegister addressFromRegister;
    private Memory mem;
    private Registers registers;

    @BeforeEach
    void setup() {
        mem = new Memory(List.of(5, 10, 15));
        registers = new Registers(2);
        addressFromRegister = new AddressFromRegister(mem, registers);
    }

    @Test
    void canReadInBounds() {
        registers.write(1, 2);

        Assertions.assertTrue(addressFromRegister.canReadAt(1));
        Assertions.assertEquals(15, addressFromRegister.read(1));
    }

    @Test
    void canWriteInBounds() {
        registers.write(1, 0);

        Assertions.assertTrue(addressFromRegister.canWriteAt(1));
        addressFromRegister.write(1, 50);
        Assertions.assertEquals(50, mem.read(0));
    }

    @Test
    void cantReadOutsideRegisterBounds() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.read(2));
        Assertions.assertFalse(addressFromRegister.canReadAt(-1));
    }

    @Test
    void cantWriteOutsideRegisterBounds() {
        Assertions.assertFalse(addressFromRegister.canWriteAt(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.write(-1, 5));
    }

    @Test
    void cantReadOutsideMemoryBounds() {
        registers.write(1, 5);
        Assertions.assertFalse(addressFromRegister.canReadAt(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.read(1));
    }

    @Test
    void cantReadOrWriteOutsideMemoryBounds() {
        registers.write(1, 5);

        Assertions.assertFalse(addressFromRegister.canWriteAt(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.write(1, 5));
    }
}