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
    void getInstance() {
        Memory instance1 = Memory.getInstance();
        Memory instance2 = Memory.getInstance();
        Assertions.assertEquals(instance1, instance2);
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
    void read_butWithMemoryUninitialized() {
        Memory m = Memory.getInstance();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.read(0));
    }

    @Test
    void read_butIndexIsOutOfBounds() {
        Memory m = Memory.getInstance();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.read(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.read(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.read(1));
    }

    @Test
    void read_butReadValueIsNull() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();
        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);
        field.set(m, new ArrayList<Integer>(Arrays.asList(null, 1, 23)));
        Assertions.assertThrows(NullPointerException.class, () ->
            m.read(0));
    }

    @Test
    void setMemory_validArgument() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();

        ArrayList<Integer> p = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 0, 1));
        m.setMemory(p);

        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);

        Assertions.assertEquals(p, field.get(m));
    }

    @Test
    void setMemory_butMemoryArgumentIsNull() {
        Memory m = Memory.getInstance();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            m.setMemory(null));
    }

    @Test
    void setMemory_butMemoryArgumentContainsNullValue() {
        Memory m = Memory.getInstance();

        ArrayList<Integer> p = new ArrayList<>(Arrays.asList(1, null, 3, 4, 0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            m.setMemory(p));
    }

    @Test
    void getMemory_gotValidMemory() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();

        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);
        field.set(m, new ArrayList<>(Arrays.asList(1, 0, 3, 4, 0, 1)));

        Assertions.assertEquals(new ArrayList<>(Arrays.asList(1, 0, 3, 4, 0, 1)), m.getMemory());
    }

    @Test
    void getMemory_butMemoryIsntSet() throws NoSuchFieldException {
        Memory m = Memory.getInstance();
        Assertions.assertEquals(new ArrayList<Integer>(), m.getMemory());
    }

    @Test
    void write_validValue() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();
        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);
        field.set(m, new ArrayList<Integer>(Arrays.asList(0, 1, 23)));
        m.write(0, 5);
        m.write(1, 10);
        m.write(2, 15);

        Assertions.assertEquals(new ArrayList<Integer>(Arrays.asList(5, 10, 15)),
            field.get(m));

    }

    @Test
    void write_butOutOfMemoryBounds() throws NoSuchFieldException, IllegalAccessException {
        final Memory m = Memory.getInstance();

        final Field field = m.getClass()
                             .getDeclaredField("memory");
        field.setAccessible(true);
        field.set(m, new ArrayList<Integer>(Arrays.asList(0, 1)));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.write(-1, 5));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.write(2, 5));
    }

    @Test
    void write_butWithMemoryUninitialized() {
        Memory m = Memory.getInstance();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
            m.write(0, 5));
    }


}