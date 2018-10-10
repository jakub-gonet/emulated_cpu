package cpu.memory.addressing_modes;

import cpu.memory.Readable;

public class Immediate implements Readable {
    @Override
    public int read(int address) {
        return address;
    }

    @Override
    public boolean canReadAt(int address) {
        return true;
    }
}
