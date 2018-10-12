package cpu.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

class StackTest {

    @Test
    void canCreateStackWithSpecifiedCapacity() {
        Stack stack = new Stack(2);

        Assertions.assertEquals(2, stack.size());
    }

    @Test
    void canPushToAndPopFromStack() {
        Stack stack = new Stack(2);

        stack.push(2);
        Assertions.assertEquals(2, stack.pop());
    }

    @Test
    void canResetStack() {
        Stack stack = new Stack(2);
        stack.push(5);

        Assertions.assertFalse(stack.isEmpty());
        stack.reset();
        Assertions.assertTrue(stack.isEmpty());
    }

    @Test
    void stackOverflowThrowsaAnException() {
        Stack stack = new Stack(2);

        stack.push(2);
        stack.push(2);

        Assertions.assertThrows(IllegalStateException.class, () -> stack.push(-1));
    }

    @Test
    void poppingFromEmptyStackThrowAnException() {
        Stack stack = new Stack(2);

        Assertions.assertThrows(EmptyStackException.class, stack::pop);
    }

}