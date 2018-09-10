package cpu.cores;

import cpu.alu.Alu;
import cpu.cu.Cu;
import cpu.memory.Memory;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;

class Core {
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
