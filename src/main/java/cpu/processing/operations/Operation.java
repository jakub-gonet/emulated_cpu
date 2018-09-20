package cpu.processing.operations;

import cpu.memory.MemoryManager;
import cpu.memory.Readable;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class Operation {
    private final int addrModeLength = 3;
    private final int addrModeBitFieldLength = (int) Math.pow(2, addrModeLength) - 1;
    private final int argNumLength = 2;
    private final int argNumBitFieldLength = (int) Math.pow(2, argNumLength) - 1;
    private Readable memory;
    private MemoryManager manager;

    public Operation(Readable memory, MemoryManager manager) {
        this.manager = manager;
        this.memory = memory;
    }

    public void fetchOpCode(OpCodes opCodes){
        opCode = opCodes.byId(opCodeId);
    }

    public int execute(){
        return IP;
    }

}
