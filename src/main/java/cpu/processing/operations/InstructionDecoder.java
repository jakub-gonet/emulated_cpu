package cpu.processing.operations;

public final class InstructionDecoder {
    private static final int addrModeLength = 8;
    private static final int addrModeBitFieldLength = (int) Math.pow(2, addrModeLength) - 1;
    private static final int argNumLength = 2;
    private static final int argNumBitFieldLength = (int) Math.pow(2, argNumLength) - 1;
    private static final int maxArgNum = 2;

    static int decodeOpCode(int opCodeAndAddresses) {
        return opCodeAndAddresses >> (maxArgNum * addrModeLength + argNumLength);
    }

    static int decodeArgNumber(int opCodeAndAddresses) {
        return (opCodeAndAddresses >> maxArgNum * addrModeLength) & argNumBitFieldLength;
    }

    static int decodeAddrMode(int num, int opCodeAndAddresses) {
        return (opCodeAndAddresses >> (maxArgNum - num - 1) * addrModeLength) & addrModeBitFieldLength;
    }

    public static int addrModeLength() {
        return addrModeLength;
    }

    public static int addrNumLength(){
        return argNumLength;
    }

    static int maxArgNum() {
        return maxArgNum;
    }
}
