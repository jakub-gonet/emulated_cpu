package emulated_cpu;

import emulated_cpu.cpu.alu.ALU;
import emulated_cpu.cpu.cu.CU;
import emulated_cpu.cpu.memory.IOInterface;
import emulated_cpu.cpu.memory.Memory;
import emulated_cpu.cpu.memory.MemoryAddressInRegister;

import java.util.Optional;

/**
 * Command is a class which is used to contain single command (OP code, addressing modes, operands).
 */
public class Command {
    private ALU alu;
    private CU cu;
    private MemoryAddressInRegister memoryAddressInRegister;

    private Memory memory = Memory.getInstance();
    private int opCodeAddress;
    private IOInterface firstAddressType, secondAddressType;

    private Integer firstValueAddress, secondValueAddress;
    private Integer firstValue, secondValue;

    /**
     * Creates new Command with specified CU and ALU.
     *
     * @param alu ALU used in CPU
     * @param cu  CU used in CPU
     */
    public Command(ALU alu, CU cu) {
        this.alu = alu;
        this.cu = cu;
        this.memoryAddressInRegister = new MemoryAddressInRegister(alu.getRegisters());

        int opCodeAndAddressTypes = getNextValueFromMemory();
        this.opCodeAddress = opCodeAndAddressTypes >> 6;
        int firstAddressTypeIndex = (opCodeAndAddressTypes >> 3) & 0x7;
        int secondAddressTypeIndex = opCodeAndAddressTypes & 0x7;

        firstAddressType = getProperIOInterfaceFromAddressType(firstAddressTypeIndex);
        secondAddressType = getProperIOInterfaceFromAddressType(secondAddressTypeIndex);
    }

    /**
     * Gets next OP code, fetches its arguments, then it applies them in appropriate unit (CU or ALU).
     *
     * @return value of arguments modified by OP code.
     */
    public Integer fetchAndExecuteNextInstruction() {
        OperatingUnit unit = getOperatingUnitFromCurrentOpCode();
        opCodeAddress = adaptOpCodeIndexToOperatingUnit();

        switch (unit.getOpCodes()
                    .get(opCodeAddress)
                    .getRequiredArguments()
            ) {
            case 0:
                firstValueAddress = null;
                secondValueAddress = null;
                break;
            case 2:
                firstValueAddress = getNextValueFromMemory();
                secondValueAddress = getNextValueFromMemory();
                secondValue = (secondAddressType != null ?
                    secondAddressType.read(secondValueAddress) :
                    secondValueAddress);
            case 1:

                firstValueAddress = Optional.ofNullable(firstValueAddress)
                                            .orElseGet(this::getNextValueFromMemory);
                firstValue = (firstAddressType != null) ?
                    firstAddressType.read(firstValueAddress) :
                    firstValueAddress;
        }
        return unit.execute(opCodeAddress, new Arguments(firstValue, secondValue));
    }

    /**
     * Gets address type of first argument.
     *
     * @return IOInterface as address type
     */
    public IOInterface getFirstAddressType() {
        return firstAddressType;
    }

    /**
     * Gets address of first argument.
     *
     * @return int which shows index of argument
     */
    public Integer getFirstValueAddress() {
        return firstValueAddress;
    }

    /**
     * Reads next value from memory and increments IP.
     *
     * @return value read from memory
     */
    private int getNextValueFromMemory() {
        return memory
            .read(cu.instructionPointer++);
    }

    /**
     * Gets appropriate IOInterface depending on address type passed.
     *
     * @param addressType index of each type
     * @return IOInterface which is memory type of given address
     */
    private IOInterface getProperIOInterfaceFromAddressType(int addressType) {
        switch (addressType) {
            case 0:
                return null;
            case 1:
                return alu.getRegisters();
            case 2:
                return Memory.getInstance();
            case 3:
                return memoryAddressInRegister;
            default:
                throw new IllegalArgumentException("Addressing mode " + addressType + " doesn't exist");
        }
    }

    /**
     * Adjust OP code index to match ALU's or CU's OP code.
     *
     * @return OP code index
     */
    private int adaptOpCodeIndexToOperatingUnit() {
        return opCodeAddress < cu.getOpCodes()
                                 .size() ?
            opCodeAddress :
            opCodeAddress - cu.getOpCodes()
                              .size();
    }

    /**
     * Gets CU or ALU depending on which OP code we want to use.
     *
     * @return Operating Unit - ALU or CU
     */
    private OperatingUnit getOperatingUnitFromCurrentOpCode() {
        return opCodeAddress < cu.getOpCodes()
                                 .size() ? cu : alu;
    }
}