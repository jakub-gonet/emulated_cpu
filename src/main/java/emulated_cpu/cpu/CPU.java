package emulated_cpu.cpu;

import emulated_cpu.Command;
import emulated_cpu.cpu.alu.ALU;
import emulated_cpu.cpu.alu.Registers;
import emulated_cpu.cpu.cu.CU;
import emulated_cpu.cpu.memory.IOInterface;
import emulated_cpu.cpu.memory.Memory;

import java.util.ArrayList;
import java.util.Optional;

/**
 * CPU is main class for this project, represents a processor.
 */
public class CPU {
    private ALU alu = new ALU();
    private CU cu = new CU(alu.getRegisters()
                              .getStatusRegister());
    private Memory memory = Memory.getInstance();

    /**
     * Create new CPU object with specified program.
     *
     * @param program program which will be executed
     */
    public CPU(ArrayList<Integer> program) {
        memory.setMemory(program);
    }

    /**
     * Fetches and executes next instruction from program memory.
     */
    void executeNextOperation() {
        Command command = new Command(alu, cu);
        Integer result = command.fetchAndExecuteNextInstruction();
        if (result != null) {
            writeToIOInterface(result, command);
        }
    }

    /**
     * Restarts processor, clearing ALU's registers and setting IP to 0.
     */
    public void restart() {
        for (int i = 0; i < alu.getRegisters()
                               .size(); i++) {
            alu.getRegisters()
               .write(i, 0);
        }

        cu.instructionPointer = 0;
    }

    /**
     * Load new program
     *
     * @param program new program to be used
     */
    public void loadNewProgram(ArrayList<Integer> program) {
        memory.setMemory(program);
        restart();
    }

    /**
     * Write value to IOInterface contained in command.
     *
     * @param value   value to be updated
     * @param command command containing memory type and address
     */
    private void writeToIOInterface(Integer value, Command command) {
        IOInterface writingAddressType = Optional.ofNullable(command.getFirstAddressType())
                                                 .orElseThrow(() -> new IllegalArgumentException("Address type can't be const value"));
        writingAddressType.write(command.getFirstValueAddress(), value);
    }

    /**
     * Checks if CU ended operating.
     *
     * @return state of CU
     */
    public boolean isStopped() {
        return cu.isStopped();
    }

    /**
     * Gets IP.
     *
     * @return current address pointed by IP
     */
    public int getInstructionPointer() {
        return cu.instructionPointer;
    }

    /**
     * Gets CPU memory.
     *
     * @return memory
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     * Gets CPU registers
     *
     * @return registers from ALU
     */
    public Registers getAluRegisters() {
        return alu.getRegisters();
    }
}
