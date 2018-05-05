package emulated_cpu.cpu;

import emulated_cpu.Command;
import emulated_cpu.cpu.command.Command2;
import emulated_cpu.data_storage.AddressableManager;
import emulated_cpu.data_storage.Registers;
import emulated_cpu.data_storage.program_storage.ProgramHolderManager;
import emulated_cpu.data_storage.program_storage.Memory;
import emulated_cpu.data_storage.program_storage.ProgramHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

/**
 * CPU is main class for this project, represents a processor.
 */
public class CPUCore {
    private Logger logger = LogManager.getLogger(CPUCore.class);
    private ALU alu;
    private CU cu;
    private ProgramHolder holder = ProgramHolderManager.getCurrentProgramHolder();

    /**
     * Creates new CPU core with specified program.
     *
     * @param program       program which will be executed
     * @param registerCount specifies how many registers should be available
     * @param stackSize     specifies how big stack should be
     */
    public CPUCore(ArrayList<Integer> program, int registerCount, int stackSize) {
        holder.setProgram(program);
        this.alu = new ALU(registerCount);
        this.cu = new CU(alu.getRegisters()
                            .getStatusRegister(), stackSize);
    }

    public CPUCore(ArrayList<Integer> program) {
        holder.setProgram(program);
        this.alu = new ALU();
        this.cu = new CU(alu.getRegisters()
                            .getStatusRegister());
    }

    /**
     * Executes operations until HLT opcode is spotted. Can lead to infinite loops.
     */
    void executeOperationsUntilHLT() {
        int count = 0;
        logger.info("Starting program...");
        while (!isStopped()) {
            executeNextOperation();
            count++;
        }
        logger.info("Performed {} CPU operations", count);
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
     * Restarts processor by clearing ALU's registers and setting IP to 0.
     */
    public void restart() {
        for (int i = 0; i < alu.getRegisters()
                               .size(); i++) {
            alu.getRegisters()
               .write(i, 0);
        }

        cu.setStopped(false);
        cu.instructionPointer = 0;
        logger.info("Restarted the CPU");
    }

    /**
     * Load new program
     *
     * @param program new program to be used
     */
    public void loadNewProgram(ArrayList<Integer> program) {
        holder.setProgram(program);
        restart();
        logger.info("Loaded new program into CPU");
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