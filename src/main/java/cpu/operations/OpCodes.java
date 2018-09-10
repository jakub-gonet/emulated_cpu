package cpu;

import cpu.operations.OpCode;

import java.util.List;

public class OpCodes {
    List<OpCode> opCodes;

    public OpCodes(List<OpCode> opCodes) {
        this.opCodes = opCodes;
    }

    public OpCode byId(int id) {
        return opCodes.get(id);
    }
}
