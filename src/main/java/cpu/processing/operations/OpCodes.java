package cpu.processing.operations;

import java.util.List;

public class OpCodes {
    private List<OpCode> opCodes;

    public OpCodes(List<OpCode> opCodes) {
        this.opCodes = opCodes;
    }

    public OpCode byId(int id) {
        return opCodes.get(id);
    }
}
