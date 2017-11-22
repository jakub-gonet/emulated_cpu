package emulated_cpu.cpu.alu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

class RegistersTest {
    @Test
    void constructor_validSize() {
        Registers r = new Registers(0);
    }

    @Test
    void constructor_negativeRegistersNumber() {
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
    void read_addressIsOutOfRegistersBounds() {
        Registers r = new Registers(1);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.read(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.read(2));
    }

    @Test
    void write_writingToAddressRegister() {
        Registers r = new Registers(1);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        r.write(0, 5);

        Assertions.assertEquals("Did you mean to overwrite status register?", outContent.toString()
                                                                                        .trim());
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
    void write_addressIsOutOfRegistersBounds() {
        Registers r = new Registers(1);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.write(-1, 0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            r.write(2, 0));
    }


    @Test
    void changeStateOfStatusRegisterFlag_checkForExistingFlag() throws NoSuchFieldException, IllegalAccessException {
        final Registers r = new Registers(1);

        final Field field = r.getClass()
                             .getDeclaredField("registers");
        field.setAccessible(true);
        r.changeStateOfStatusRegisterFlag("Z", true);

        Assertions.assertEquals(1, ((ArrayList<Register>) field.get(r)).get(0)
                                                                       .getValue());
    }

    @Test
    void changeStateOfStatusRegisterFlag_useIllegalFlagName() {
        Registers r = new Registers(1);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            r.changeStateOfStatusRegisterFlag("P", false));
    }

    @Test
    void changeStateOfStatusRegisterFlag_useNullFlagName() {
        Registers r = new Registers(1);
        Assertions.assertThrows(NullPointerException.class, () ->
            r.changeStateOfStatusRegisterFlag(null, false));
    }


    @Test
    void changeStateOfStatusRegisterFlag_useLegalFlagNameButLowercase() {
        Registers r = new Registers(1);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            r.changeStateOfStatusRegisterFlag("z", false));
    }

}