package emulated_cpu.cpu;

public final class OperatingUnitUtils {
    private OperatingUnitUtils() {
    }

    /**
     * Gets OperatingUnit based on OP code value offset.
     * @param opCode general OP code
     * @param cu control unit
     * @param alu ALU
     * @return proper OperatingUnit (ALU or CU)
     */
    public static OperatingUnit getProperOperatingUnitFromOpCode(int opCode, OperatingUnit cu, OperatingUnit alu) {
        return opCode < cu.getOpCodes()
                          .size() ? cu : alu;
    }

    /**
     * Adjust general OP code index to match ALU's or CU's OP code.
     *
     * @return OP code index
     */
    public static int adaptOpCodeIndexToOperatingUnit(int opCode, OperatingUnit cu, OperatingUnit alu) {
        return opCode < cu.getOpCodes()
                          .size() ?
            opCode :
            opCode - cu.getOpCodes()
                       .size();
    }
}
