package cpu.memory;

public interface Writable {
    void write(int address, int data);

    boolean canWriteAt(int address);
}
