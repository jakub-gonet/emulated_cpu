package cpu.memory;

public interface Readable {
    int read(int address);
    boolean canReadAt(int address);
}
