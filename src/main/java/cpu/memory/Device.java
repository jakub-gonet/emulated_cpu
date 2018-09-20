package cpu.memory;

public interface Device<T> {
    boolean isReadable();

    boolean isWritable();

    T self();
}
