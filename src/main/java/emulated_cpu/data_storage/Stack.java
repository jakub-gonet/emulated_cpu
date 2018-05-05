package emulated_cpu.cpu.data_storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

public class Stack {
    private Logger logger = LogManager.getLogger(Memory.class);

    private Memory memory = Memory.getInstance();
    private int size;
    private static final int defaultStackSize = 16;

    private int stackPointer;

    public Stack(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Stack's size can't be negative");

        this.size = size;
        this.memory.getMemory()
                   .addAll(Collections.nCopies(size, 0));
        this.stackPointer = memory.getMemory()
                                  .size() - 1;
    }


    public Stack() {
        this(defaultStackSize);
    }

    public int pop() {
        checkValidityOfStackPointerPosition(stackPointer + 1);
        return memory.getMemory()
                     .get(++stackPointer);
    }

    public void push(int value) {
        checkValidityOfStackPointerPosition(stackPointer - 1);
        memory.getMemory()
              .set(stackPointer--, value);
    }

    public int getStackPointer(){
        return stackPointer;
    }

    private void checkValidityOfStackPointerPosition(int stackPointer) {
        if (stackPointer < memory.getMemory()
                                 .size() - size - 1) {

            logger.error("Stack overflow");
            throw new StackOverflowError("Stack overflow");
        } else if (stackPointer >= memory.getMemory()
                                         .size()) {

            logger.error("Stack underflow");
            throw new StackOverflowError("Stack underflow");
        }
    }
}
