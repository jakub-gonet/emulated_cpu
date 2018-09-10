package cpu.cores;

import cpu.processing.Alu;
import cpu.processing.Cu;
import cpu.memory.Memory;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;

public class Core {
    private int PC;
    private Memory memory;
    private Registers registers;
    private Stack stack;
    private Cu cu;
    private Alu alu;

    Core(Memory memory){
        this.memory = memory;
    }

}
