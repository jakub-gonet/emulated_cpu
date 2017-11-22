package emulated_cpu.cpu.alu;

public class Register {
    private int value;

    public Register(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public int getValueAt(int bitIndex) {
        return (value >> bitIndex) & 1;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
