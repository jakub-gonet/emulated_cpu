package emulated_cpu.cpu.alu;

import emulated_cpu.Arguments;
import emulated_cpu.OpCode;
import emulated_cpu.OperatingUnit;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ALU class is used with CU class to perform all mathematical operations.
 */
public final class ALU implements OperatingUnit {
    private Registers registers;

    /**
     * ALU_OP_CODES is used as lookup table for getting desired operation.
     */
    private final ArrayList<OpCode> ALU_OP_CODES = new ArrayList<>(Arrays.asList(
        new OpCode((x, y) -> ++x, 1),   //INC
        new OpCode((x, y) -> --x, 1),   //DEC
        new OpCode((x, y) -> x + y, 2), //ADD
        new OpCode((x, y) -> x - y, 2), //SUB
        new OpCode((x, y) -> x * y, 2), //MUL
        new OpCode((x, y) -> x / y, 2), //DIV
        new OpCode((x, y) -> x & y, 2), //AND
        new OpCode((x, y) -> x | y, 2), //OR
        new OpCode((x, y) -> x ^ y, 2), //XOR
        new OpCode((x, y) -> ~x, 1),    //NOT
        new OpCode((x, y) -> x >> y, 2),//RSHFT
        new OpCode((x, y) -> x << y, 2),//LSHFT
        new OpCode((x, y) -> {                          //CMP
            int diff = x - y;
            registers.getStatusRegister()
                     .setZeroFlagState(false); //reset flags
            registers.getStatusRegister()
                     .setCarryFlagState(false);
            registers.getStatusRegister()
                     .setNegativeFlagState(false);
            if (diff < 0)
                registers.getStatusRegister()
                         .setNegativeFlagState(true);
            else if (diff > 0)
                registers.getStatusRegister()
                         .setCarryFlagState(true);
            else
                registers.getStatusRegister()
                         .setZeroFlagState(true);
            return null;
        }, 2)
    ));

    /**
     * Creates new ALU object with specified number of registers inside.
     *
     * @param registerNumber number of registers, can't be negative, with 0 value only StatusRegister is created.
     */
    ALU(int registerNumber) {
        registers = new Registers(registerNumber);
    }

    /**
     * Creates new ALU object with default number of registers defined by variable in Registers class.
     */
    public ALU() {
        registers = new Registers();
    }

    /**
     * Calculates value specified by OP code.
     *
     * @param opCode OP code to select operation
     * @param args   arguments passed as operation arguments
     * @return calculated value
     */
    @Override
    public Integer execute(int opCode, Arguments args) {
        checkIfAluOPCodeExists(opCode);
        ALU_OP_CODES.get(opCode)
                    .checkIfArgumentsMatchRequiredCount(args);

        return ALU_OP_CODES.get(opCode)
                           .getOperation()
                           .apply(args.arg1, args.arg2);
    }

    /**
     * Checks if passed OP code exist in lookup table.
     *
     * @param opCode OP code to be checked
     */
    private void checkIfAluOPCodeExists(int opCode) {
        if (opCode >= ALU_OP_CODES.size())
            throw new IndexOutOfBoundsException("OP code " + opCode + " doesn't exist");
    }

    /**
     * Gets register set from ALU.
     *
     * @return registers contained in this ALU
     */
    public Registers getRegisters() {
        return registers;
    }

    /**
     * Sets specified registers value.
     *
     * @param address address of modified register
     * @param value   new value of register
     */
    public void setRegister(int address, int value) {
        this.registers.write(address, value);
    }

    /**
     * Gets AlU OP codes.
     *
     * @return ArrayList of OP codes
     */
    @Override
    public ArrayList<OpCode> getOpCodes() {
        return ALU_OP_CODES;
    }
}