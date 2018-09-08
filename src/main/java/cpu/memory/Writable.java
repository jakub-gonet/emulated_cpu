package cpu.memory;

public interface Writable {
    boolean write(int address, int data);

    boolean canWriteAt(int address);
}
