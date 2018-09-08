package cpu.cu;

import cpu.memory.Readable;

import java.util.Collections;
import java.util.List;

public class SimpleReadableDevice implements Readable {
    private List<Integer> content = Collections.nCopies(5, 0);

    @Override
    public int read(int address) {
        return content.get(address);
    }

    @Override
    public boolean canReadAt(int address) {
        return 0 < address && address < content.size() - 1;
    }

    public List<Integer> rawContent() {
        return content;
    }
}
