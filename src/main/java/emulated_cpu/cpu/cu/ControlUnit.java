package emulated_cpu.cpu.cu;

import emulated_cpu.OpCode;
import emulated_cpu.cpu.alu.ALU;
import emulated_cpu.cpu.memory.Memory;

import java.util.ArrayList;
import java.util.Arrays;

public class ControlUnit {
    private ALU alu = new ALU();
    private Memory memory = Memory.getInstance();
    private int instructionPointer = 0;
    private boolean stopped = false;

    private final ArrayList<OpCode> CU_OP_CODES = new ArrayList<>(Arrays.asList(
        new OpCode((x, y) -> null, 0),  //NOP
        new OpCode((x, y) -> null, 0)  //HLT
    ));

    void fetchNextOperation(){
        int opCode = memory.read(instructionPointer)>>4;
    }

}
