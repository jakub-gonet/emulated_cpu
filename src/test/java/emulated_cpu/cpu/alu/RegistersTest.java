package emulated_cpu.cpu.alu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

class RegistersTest {
    @Test
    void constructor_validSize() {
        Registers r = new Registers(0);
    }

    @Test
    void constructor_butNegativeRegistersNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Registers r = new Registers(-1);
        });
    }

    @Test
    void read_validRead() {
        Registers r = new Registers(0);
        Assertions.assertEquals(0, r.read(0));
    }

    @Test
    void read_validReadButReadValueIsNull() throws NoSuchFieldException, IllegalAccessException {
        final Registers r = new Registers(2);
        final Field field = r.getClass()
                             .getDeclaredField("registers");
        field.setAccessible(true);
        ((ArrayList<Integer>) field.get(r)).set(0, null);

        Assertions.assertThrows(NullPointerException.class, () -> r.read(0));
    }

    @Test
    void read_butAddressIsOutOfRegistersBounds() {
        Registers r = new Registers(1);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.read(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.read(2));
    }

    // TODO enable this test when logger is added
    @Disabled
    @Test
    void write_butWritingToAddressRegister() {
        Registers r = new Registers(1);

        r.write(0, 5);

        Assertions.assertEquals("Did you mean to overwrite status register?", "");
    }


    @Test
    void write_validWrite() throws NoSuchFieldException, IllegalAccessException {
        Registers r = new Registers(1);
        r.write(1, 5);

        final Field field = r.getClass()
                             .getDeclaredField("registers");
        field.setAccessible(true);

        Assertions.assertEquals(5, ((ArrayList<Register>) field.get(r)).get(1)
                                                                       .getValue());
    }

    @Test
    void write_butAddressIsOutOfRegistersBounds() {
        Registers r = new Registers(1);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.write(-1, 0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.write(2, 0));
    }
}