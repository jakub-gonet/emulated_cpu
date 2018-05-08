package emulated_cpu.data_storage;

public class Immediate implements Addressable {
    @Override
    public int read(int address) {
        return address;
    }

    @Override
    public void write(int address, int data) {
        throw new IllegalArgumentException("Cannot write to literal!");
    }

    @Override
    public int size() {
        try {
            throw new IllegalAccessException("Size doesn't exists in literals context!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
