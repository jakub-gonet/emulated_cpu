package emulated_cpu.cpu.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StackTest {
    @Test
    void pop() {
        Stack stack = new Stack();
        stack.push(15);

        Assertions.assertEquals(15, stack.pop());
    }

    @Test
    void pop_butNoDataIsOnStack() {
        Stack stack = new Stack();
        Assertions.assertThrows(StackOverflowError.class, stack::pop);
    }

    @Test
    void stack() {
        Stack stack = new Stack(1);
        stack.push(10);
    }

    @Test
    void push_butNoSpaceAvailable() {
        Stack stack = new Stack(0);
        Assertions.assertThrows(StackOverflowError.class, () -> stack.push(2));
    }
}