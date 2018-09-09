package cpu.cu;

import cpu.memory.Readable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleReadableDevice implements Readable {
    private List<Integer> content;

    public SimpleReadableDevice(int programSize) {
        content = new ArrayList<>(Collections.nCopies(programSize, 0));
    }

    @Override
    public int read(int address) {
        return content.get(address);
    }

    @Override
    public boolean canReadAt(int address) {
        return address >= 0 && address < content.size();
    }

    public List<Integer> rawContent() {
        return content;
    }
}
