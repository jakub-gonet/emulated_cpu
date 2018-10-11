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
    void canReadAndWriteInRegisterBounds() {
        registers.write(1, 2);

        Assertions.assertEquals(15, addressFromRegister.read(1));
        Assertions.assertTrue(addressFromRegister.canReadAt(1));
        Assertions.assertTrue(addressFromRegister.canWriteAt(1));
    }

    @Test
    void cantReadOrWriteOutsideRegisterBounds() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.read(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.write(2, 5));
        Assertions.assertFalse(addressFromRegister.canReadAt(-1));
        Assertions.assertFalse(addressFromRegister.canWriteAt(2));
    }

    @Test
    void canReadAndWriteInMemoryBounds() {
        registers.write(1, 2);

        Assertions.assertEquals(15, addressFromRegister.read(1));
        Assertions.assertTrue(addressFromRegister.canReadAt(1));
        Assertions.assertTrue(addressFromRegister.canWriteAt(1));
    }

    @Test
    void cantReadOrWriteOutsideMemoryBounds() {
        registers.write(1, 5);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.read(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.write(1, 5));
        Assertions.assertFalse(addressFromRegister.canReadAt(-1));
        Assertions.assertFalse(addressFromRegister.canWriteAt(4));
    }
}