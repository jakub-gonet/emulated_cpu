package cpu.memory.registers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {
    @Test
    void canReadWholeRegister() {
        Register r = new Register(5);
        Assertions.assertEquals(5, r.value());
    }

    @Test
    void canReadSomeBitOfRegister() {
        Register r = new Register(5);
        Assertions.assertTrue(r.valueAt(2));
    }

    @Test
    void canWriteToWholeRegister() {
        Register r = new Register(5);
        r.set(8);

        Assertions.assertEquals(8, r.value());
    }

    @Test
    void canWriteToSomeBitOfRegister() {
        Register r = new Register();

        r.setValueAt(2, true);
        Assertions.assertTrue(r.valueAt(2));
        r.setValueAt(2, false);
        Assertions.assertFalse(r.valueAt(2));
    }
}