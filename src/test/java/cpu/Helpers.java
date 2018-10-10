package cpu;

import cpu.processing.operations.InstructionDecoder;

public class Helpers {
    public static int opCode(int num, int argNum, int firstAddrMode, int secondAddrMode) {
        int addrModeLength = InstructionDecoder.addrModeLength();
        int argNumLength = InstructionDecoder.addrNumLength();
        return num << (addrModeLength * 2 + argNumLength) | argNum << (addrModeLength * 2) | firstAddrMode << addrModeLength | secondAddrMode;
    }
}
