package emulated_cpu.cpu.memory;

import java.util.Collections;

public class Stack {
    private Memory memory = Memory.getInstance();

    private int stackPointer;

    Stack(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Stack's size can't be negative");
        memory.getMemory()
              .addAll(Collections.nCopies(size, 0));

        stackPointer = memory.getMemory()
                             .size();
    }

    int pop() {
        return memory.getMemory()
                     .get(stackPointer++);
    }

    void push(int value) {
        memory.getMemory()
              .set(stackPointer, value);
        stackPointer--;
    }

    public int getStackPointer() {
        return stackPointer;
    }
}
