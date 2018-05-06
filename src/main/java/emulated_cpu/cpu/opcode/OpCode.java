package emulated_cpu.cpu.opcode;


import emulated_cpu.cpu.command.Arguments;

import java.util.function.BinaryOperator;

/**
 * This class is used as single OP code wrapper.
 */
public class OpCode {
    private BinaryOperator<Integer> operation;
    private int requiredArguments;
    private int id;

    public OpCode(BinaryOperator<Integer> operation, int requiredArguments, int id) {
        this.operation = operation;
        this.requiredArguments = requiredArguments;
        this.id = id;
    }


    /**
     * Checks if passed arguments match required argument count for this OP code.
     *
     * @param args object of arguments
     */
    public void checkIfArgumentsMatchRequiredCount(Arguments args) {
        if (requiredArguments != args.getArgumentsCount())
            throw new IllegalArgumentException("Passed arguments don't match arguments count for OP code");
    }

    /**
     * Gets required arguments for OP code.
     *
     * @return amount of arguments required
     */
    public int getRequiredArguments() {
        return requiredArguments;
    }

    /**
     * Gets operation performed by this OP code.
     *
     * @return BinaryOperator as operation
     */
    public BinaryOperator<Integer> getOperation() {
        return operation;
    }

    /**
     * Gets OP code's id
     *
     * @return id
     */
    public int getId() {
        return id;
    }
}
