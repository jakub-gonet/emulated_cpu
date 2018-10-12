package cpu.memory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stack {
    private Logger logger = LogManager.getLogger(Stack.class);
    private java.util.Stack<Integer> stack;
    private int size;

    public Stack(int size) {
        this.stack = new java.util.Stack<>();
        this.size = size;
    }

    public void reset() {
        logger.info("Reset stack");
        stack.clear();
    }

    public void push(Integer arg) {
        if (stack.size() < size) {
            logger.info("Pushing {} into a stack", arg);
            stack.push(arg);
        } else {
            logger.error("Stack overflow when pushing {} to the stack", arg);
            throw new IllegalStateException("Stack overflow");
        }
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int pop() {
        int value = stack.pop();
        logger.info("Popped {} from stack", value);
        return value;
    }

    public int size() {
        return size;
    }
}
