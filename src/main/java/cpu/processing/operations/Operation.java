package cpu.processing.operations;

public class Operation {
    private OpCode opCode;
    private int opCodeId;
    private int IP;

    private Operation() {
    }

    public void fetchOpCode(OpCodes opCodes){
        opCode = opCodes.byId(opCodeId);
    }

    public int execute(){
        return IP;
    }

}
