package emulated_cpu.cpu.alu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ALUTest {
    @Test
    void compute_validArguments() throws NoSuchFieldException, IllegalAccessException {
        ALU alu = new ALU();
        Assertions.assertEquals(new Integer(2), alu.compute(0, 1));
        Assertions.assertEquals(new Integer(-3), alu.compute(3, 1, 4));
        alu.compute(12, 1, 4);
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getNegativeFlagState());
        alu.compute(12, -1, 4);
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getNegativeFlagState());
        alu.compute(12, -1, -4);
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getCarryFlagState());
        alu.compute(12, 1, 1);
        Assertions.assertTrue(alu.getRegisters()
                                 .getStatusRegister()
                                 .getZeroFlagState());

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