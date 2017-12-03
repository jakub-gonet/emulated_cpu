package emulated_cpu;

import emulated_cpu.cpu.memory.IOInterface;

public class Arguments {
    public Integer arg1, arg2;
    public IOInterface firstAddressType, secondAddressType;

    public int getArgumentsCount() {
        int argumentCount = 0;
        if (arg1 != null) argumentCount++;
        if (arg2 != null) argumentCount++;

        return argumentCount;
    }
}
