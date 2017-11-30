package emulated_cpu.cpu.alu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class RegisterTest {
    @Test
    void getValueAt_validBitNumber() {
        Register r = new Register(6);
        Assertions.assertEquals(0, r.getValueAt(0));
        Assertions.assertEquals(1, r.getValueAt(1));
        Assertions.assertEquals(1, r.getValueAt(2));
        Assertions.assertEquals(0, r.getValueAt(3));

        Assertions.assertEquals(0, r.getValueAt(31));
    }

    @Test
    void getValueAt_butAddressOutOfRegisterBounds() {
        Register r = new Register(7);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            r.getValueAt(-1);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            r.getValueAt(32);
        });
    }

    @Test
    void setValue_validValue() throws NoSuchFieldException, IllegalAccessException {
        final Register r = new Register(0);
        final Field field = r.getClass()
                             .getDeclaredField("value");
        field.setAccessible(true);

        r.setValue(-5);
        Assertions.assertEquals(-5, field.get(r));
    }

    @Test
    void setValueAt_validBitNumber() throws NoSuchFieldException, IllegalAccessException {
        final Register r = new Register(0);
        final Field field = r.getClass()
                             .getDeclaredField("value");
        field.setAccessible(true);

        r.setValueAt(1, true);
        Assertions.assertEquals(2, field.get(r));
    }
}