package emulated_cpu.cpu.alu;

import emulated_cpu.Arguments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ALUTest {
    @Test
    void compute_validArguments() throws NoSuchFieldException, IllegalAccessException {
        ALU alu = new ALU();
        Assertions.assertEquals(new Integer(2), alu.execute(0, new Arguments(1)));
        Assertions.assertEquals(new Integer(-3), alu.execute(3, new Arguments(1, 4)));
        alu.execute(12, new Arguments(1, 4));
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getNegativeFlagState());
        alu = new ALU();
        alu.execute(12, new Arguments(-1, 4));
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getNegativeFlagState());
        alu = new ALU();
        alu.execute(12, new Arguments(-1, -4));
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getCarryFlagState());
        alu = new ALU();
        alu.execute(12, new Arguments(1, 1));
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getZeroFlagState());
    }

    @Test
    void compute_butWithInvalidNumberOfArguments() {
        ALU alu = new ALU();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            alu.execute(0, new Arguments(1, 5)));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            alu.execute(2, new Arguments(1)));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            alu.execute(2, new Arguments(5)));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            alu.execute(9, new Arguments(1,1)));
    }

    @Test
    void setRegister_validValueAndIndex() {
        final ALU alu = new ALU(5);
        alu.setRegister(4, 20);
    }

    @Test
    void setRegister_butIndexOutOfBounds() {
        final ALU alu = new ALU(5);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            alu.setRegister(-1, 20));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            alu.setRegister(6, 20));
    }
}