package emulated_cpu.cpu;

import emulated_cpu.Command;
import emulated_cpu.cpu.alu.ALU;
import emulated_cpu.cpu.alu.Registers;
import emulated_cpu.cpu.cu.CU;
import emulated_cpu.cpu.memory.IOInterface;
import emulated_cpu.cpu.memory.Memory;

import java.util.ArrayList;
import java.util.Optional;

public class CPU {
    private ALU alu = new ALU();
    private CU cu = new CU(alu.getRegisters()
                              .getStatusRegister());
    private Memory memory = Memory.getInstance();

    public CPU(ArrayList<Integer> program) {
        memory.setMemory(program);
    }

    void executeNextOperation() {
        Command command = new Command(alu, cu);
        Integer result = command.fetchAndExecuteNextInstruction();
        if (result != null) {
            writeToIOInterface(result, command);
        }
    }

    public void restart() {
        for (int i = 0; i < alu.getRegisters()
                               .size(); i++) {
            alu.getRegisters()
               .write(i, 0);
        }

        cu.instructionPointer = 0;
    }

    public void loadNewProgram(ArrayList<Integer> program) {
        memory.setMemory(program);
    }

    private void writeToIOInterface(Integer value, Command command) {
        IOInterface writingAddressType = Optional.ofNullable(command.getFirstAddressType())
                                                 .orElseThrow(() -> new IllegalArgumentException("Address type can't be const value"));
        writingAddressType.write(command.getFirstValueAddress(), value);
    }

    public boolean isStopped() {
        return cu.isStopped();
    }

    public int getInstructionPointer() {
        return cu.instructionPointer;
    }

    public Memory getMemory() {
        return memory;
    }

    public Registers getAluRegisters() {
        return alu.getRegisters();
    }
}
