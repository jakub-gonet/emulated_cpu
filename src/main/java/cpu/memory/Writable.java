package cpu.memory;

public interface Writable {
    boolean write(int data, int address);

    boolean canWriteAt(int address);
}
