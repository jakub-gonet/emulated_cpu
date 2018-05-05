package emulated_cpu.data_storage;

import emulated_cpu.data_storage.program_storage.ProgramHolder;
import emulated_cpu.data_storage.program_storage.ProgramHolderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stack {
    private Logger logger = LogManager.getLogger(Stack.class);

    private ProgramHolder holder = ProgramHolderManager.getCurrentProgramHolder();
    private int size;
    private static final int defaultStackSize = 16;

    private int stackPointer;

    public Stack(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Stack's size can't be negative");

        this.size = size;
        this.stackPointer = holder.size() - 1;
    }


    public Stack() {
        this(defaultStackSize);
    }

    public int pop() {
        checkValidityOfStackPointerPosition(stackPointer + 1);
        return holder.read(++stackPointer);
    }

    public void push(int value) {
        checkValidityOfStackPointerPosition(stackPointer - 1);
        holder.write(stackPointer--, value);
    }

    public int getStackPointer() {
        return stackPointer;
    }

    private void checkValidityOfStackPointerPosition(int stackPointer) {
        if (stackPointer < holder.size() - size - 1) {

            logger.error("Stack overflow");
            throw new StackOverflowError("Stack overflow");
        } else if (stackPointer >= holder.size()) {

            logger.error("Stack underflow");
            throw new StackOverflowError("Stack underflow");
        }
    }
}
