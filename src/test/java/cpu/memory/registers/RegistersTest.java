package cpu.memory.registers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegistersTest {
    @Test
    void canCreateNRegistersAndStatusRegister() {
        Registers registers = new Registers(5);
        Assertions.assertEquals(6, registers.size());

        registers = new Registers(0);
        Assertions.assertEquals(1, registers.size());
    }

    @Test
    void creatingNegativeAmountOfRegistersThrowsAnException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Registers(-1));
    }

    @Test
    void firstRegisterIsStatusRegister() {
        Registers registers = new Registers(1);
        Assertions.assertTrue(registers.statusRegister() instanceof StatusRegister);
        Assertions.assertEquals(registers.byId(0), registers.statusRegister());
    }

    @Test
    void canResetRegisters(){
        Registers registers = new Registers(2);
        registers.write(0, 15);
        registers.write(1, 5);
        registers.write(2, 10);

        Assertions.assertEquals(15, registers.read(0));
        Assertions.assertEquals(5, registers.read(1));
        Assertions.assertEquals(10, registers.read(2));

        registers.resetRegisters();
        Assertions.assertEquals(0, registers.read(0));
        Assertions.assertEquals(0, registers.read(1));
        Assertions.assertEquals(0, registers.read(2));
    }

    @Test
    void canReadWriteInsideRegistersBounds(){
        Registers registers = new Registers(2);

        Assertions.assertTrue(registers.canReadAt(0));
        Assertions.assertTrue(registers.canWriteAt(2));
    }

    @Test
    void cantReadOutsideRegistersBounds(){
        Registers registers = new Registers(2);

        Assertions.assertFalse(registers.canReadAt(-1));
        Assertions.assertFalse(registers.canWriteAt(3));
    }


}
