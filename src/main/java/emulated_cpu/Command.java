package emulated_cpu;

import emulated_cpu.cpu.alu.ALU;
import emulated_cpu.cpu.cu.CU;
import emulated_cpu.cpu.memory.IOInterface;
import emulated_cpu.cpu.memory.Memory;

import java.util.Optional;

public class Command {
    private ALU alu;
    private CU cu;
    private Memory memory = Memory.getInstance();
    private int opCodeAddress;
    private int firstAddressTypeIndex, secondAddressTypeIndex;
    private IOInterface firstAddressType, secondAddressType;

    private Integer firstValueAddress, secondValueAddress;
    private Integer firstValue, secondValue;

    public Command(ALU alu, CU cu) {
        this.alu = alu;
        this.cu = cu;

        int opCodeAndAddressTypes = getNextValueFromMemory();
        this.opCodeAddress = opCodeAndAddressTypes >> 4;
        this.firstAddressTypeIndex = (opCodeAndAddressTypes >> 2) & 0x3;
        this.secondAddressTypeIndex = opCodeAndAddressTypes & 0x3;

        firstAddressType = getProperIOInterfaceFromAddressType(firstAddressTypeIndex);
        secondAddressType = getProperIOInterfaceFromAddressType(secondAddressTypeIndex);
    }

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

    public IOInterface getFirstAddressType() {
        return firstAddressType;
    }

    public IOInterface getSecondAddressType() {
        return secondAddressType;
    }

    public Integer getFirstValue() {
        return firstValue;
    }

    public Integer getSecondValue() {
        return secondValue;
    }

    public Integer getFirstValueAddress() {
        return firstValueAddress;
    }

    private int getNextValueFromMemory() {
        return memory
            .read(cu.instructionPointer++);
    }

    private IOInterface getProperIOInterfaceFromAddressType(int addressType) {
        switch (addressType) {
            case 0:
                return null;
            case 1:
                return alu.getRegisters();
            case 2:
                return Memory.getInstance();
            default:
                throw new IllegalArgumentException("Addressing mode doesn't exist");
        }
    }

    private int adaptOpCodeIndexToOperatingUnit() {
        return opCodeAddress < cu.getOpCodes()
                                 .size() ?
            opCodeAddress :
            opCodeAddress - cu.getOpCodes()
                              .size();
    }

    private OperatingUnit getOperatingUnitFromCurrentOpCode() {
        return opCodeAddress < cu.getOpCodes()
                                 .size() ? cu : alu;
    }
}