package cpu.processing.operations;

import java.util.function.BinaryOperator;

public class OpCode {
    private BinaryOperator<Integer> operation;
    private int requiredArguments;

    public OpCode(BinaryOperator<Integer> operation, int requiredArguments) {
        this.operation = operation;
        this.requiredArguments = requiredArguments;
    }

    public int requiredArguments() {
        return requiredArguments;
    }

    public BinaryOperator<Integer> operation() {
        return operation;
    }
}
