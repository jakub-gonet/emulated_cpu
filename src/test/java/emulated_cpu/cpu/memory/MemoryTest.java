package emulated_cpu.cpu.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

class MemoryTest {
    @BeforeEach
    void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Memory.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }


    @Test
    void read_validRead() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();
        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);
        field.set(m, new ArrayList<Integer>(Arrays.asList(5, 1, 23)));
        Assertions.assertEquals(5, m.read(0));
    }

    @Test
    void read_validReadButMemoryUninitialized() {
        Memory m = Memory.getInstance();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.read(0));
    }

    @Test
    void read_indexIsOutOfBounds() {
        Memory m = Memory.getInstance();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.read(-1));
    }

    @Test
    void read_validReadButReadValueIsNull() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();
        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);
        field.set(m, new ArrayList<Integer>(Arrays.asList(null, 1, 23)));
        Assertions.assertThrows(NullPointerException.class, () ->
            m.read(0));
    }

    @Test
    void write() {
    }

    @Test
    void toStringTest() {
    }

    @Test
    void getInstance_validWayOfCalling() {
        Memory instance1 = Memory.getInstance();
        Memory instance2 = Memory.getInstance();
        Assertions.assertEquals(instance1, instance2);
    }

    @Test
    void setMemory() {
    }

    @Test
    void getMemory() {
    }

    @Test
    void getNumberFromMemoryAddress() {
    }

}