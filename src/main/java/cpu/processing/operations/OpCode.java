package cpu.processing.operations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

public class OpCode {
    private BinaryOperator<Integer> operation;
    private int requiredArguments;
    private Logger logger = LogManager.getLogger(OpCode.class);

    public OpCode(BinaryOperator<Integer> operation, int requiredArguments) {
        this.operation = operation;
        this.requiredArguments = requiredArguments;
    }

    public int requiredArguments() {
        return requiredArguments;
    }

    public Integer applyOperation(List<Integer> args) {
        if (args.size() != requiredArguments) {
            logger.error("OP code have {} arguments, when {} arguments are required", args.size(), requiredArguments);
            throw new IllegalArgumentException("Arguments number not matching");
        }

        ArrayList<Integer> argsCopy = new ArrayList<>(args);
        if (argsCopy.size() < 2) {
            argsCopy.addAll(Collections.nCopies(2 - argsCopy.size(), 0));
        }

        return operation.apply(argsCopy.get(0), argsCopy.get(1));
    }
}
