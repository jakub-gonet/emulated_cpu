package cpu.memory.addressing_modes;

import cpu.memory.Memory;
import cpu.memory.registers.Registers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AddressFromRegisterTest {
    @Test
    void canReadAndWriteInRegisterBounds() {
        Memory mem = new Memory(List.of(5, 10, 15));
        Registers registers = new Registers(2);
        AddressFromRegister addressFromRegister = new AddressFromRegister(mem, registers);

        registers.write(1, 2);

        Assertions.assertEquals(15, addressFromRegister.read(1));
        Assertions.assertTrue(addressFromRegister.canReadAt(1));
        Assertions.assertTrue(addressFromRegister.canWriteAt(1));
    }

    @Test
    void cantReadOrWriteOutsideRegisterBounds() {
        Memory mem = new Memory(List.of(5, 10, 15));
        Registers registers = new Registers(2);
        AddressFromRegister addressFromRegister = new AddressFromRegister(mem, registers);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.read(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.write(2, 5));
        Assertions.assertFalse(addressFromRegister.canReadAt(-1));
        Assertions.assertFalse(addressFromRegister.canWriteAt(2));
    }

    @Test
    void canReadAndWriteInMemoryBounds() {
        Memory mem = new Memory(List.of(5, 10, 15));
        Registers registers = new Registers(2);
        AddressFromRegister addressFromRegister = new AddressFromRegister(mem, registers);

        registers.write(1, 2);

        Assertions.assertEquals(15, addressFromRegister.read(1));
        Assertions.assertTrue(addressFromRegister.canReadAt(1));
        Assertions.assertTrue(addressFromRegister.canWriteAt(1));
    }

    @Test
    void cantReadOrWriteOutsideMemoryBounds() {
        Memory mem = new Memory(List.of(5, 10, 15));
        Registers registers = new Registers(2);
        AddressFromRegister addressFromRegister = new AddressFromRegister(mem, registers);

        registers.write(1, 5);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.read(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> addressFromRegister.write(1, 5));
        Assertions.assertFalse(addressFromRegister.canReadAt(-1));
        Assertions.assertFalse(addressFromRegister.canWriteAt(4));
    }
}