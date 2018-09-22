package cpu.memory;

public class Stack implements Readable, Writable {
    @Override
    public int read(int address) {
        return 0;
    }

    @Override
    public boolean canReadAt(int address) {
        return false;
    }

    @Override
    public void write(int address, int data) {

    }

    @Override
    public boolean canWriteAt(int address) {
        return false;
    }

    public void reset() {
    }
}
