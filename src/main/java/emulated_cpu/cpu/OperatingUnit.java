package emulated_cpu.cpu;

import emulated_cpu.cpu.command.Arguments;
import emulated_cpu.cpu.opcode.OpCode;

import java.util.ArrayList;

/**
 * Represents Unit object to unify some properties of ALU and CU.
 */
public interface OperatingUnit {
    /**
     * Gets OP codes for this unit.
     *
     * @return ArrayList of OP codes
     */
    ArrayList<OpCode> getOpCodes();

    /**
     * Executes proper OP code with specified arguments.
     *
     * @param opCode OP code to select operation
     * @param args   arguments passed as operation arguments
     * @return result of the operation
     */
    Integer execute(int opCode, Arguments args);
}
