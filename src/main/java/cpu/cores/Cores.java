package cpu.cores;

import cpu.memory.Memory;

import java.util.ArrayList;
import java.util.List;

public class Cores {
    private List<Core> cores;

    public Cores(int count, Memory memory){
        cores = createCores(count, memory);
    }

    private List<Core> createCores(int count, Memory memory){
        List<Core> cores = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cores.add(new Core(memory));
        }
        return cores;
    }
}
