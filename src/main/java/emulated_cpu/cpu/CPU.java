package emulated_cpu.cpu;

import emulated_cpu.Arguments;
import emulated_cpu.OperatingUnit;
import emulated_cpu.cpu.alu.ALU;
import emulated_cpu.cpu.cu.CU;
import emulated_cpu.cpu.memory.IOInterface;
import emulated_cpu.cpu.memory.Memory;

import java.util.ArrayList;

public class CPU {
    private ALU alu = new ALU();
    private CU cu = new CU();
    private Memory memory = Memory.getInstance();

    public CPU(ArrayList<Integer> program) {
        memory.setMemory(program);
    }

    private int fetchNextOperationOrValue(CU cu) {
        return memory.read(cu.instructionPointer++);
    }

    void executeNextOperation() {
        int opCodeIndex = fetchNextOperationOrValue(cu) >> 4;
        int addressingModeOfFirstArgument = (fetchNextOperationOrValue(cu) >> 2) & 0x3;
        int addressingModeOfSecondArgument = fetchNextOperationOrValue(cu) & 0x3;

        Arguments arguments = new Arguments();
        arguments.firstAddressType = getProperIOInterfaceFromAddressType(addressingModeOfFirstArgument);
        arguments.secondAddressType = getProperIOInterfaceFromAddressType(addressingModeOfSecondArgument);

        OperatingUnit operatingUnit;
        if (opCodeIndex < cu.getOpCodes()
                            .size()) operatingUnit = cu;
        else {
            operatingUnit = alu;
            opCodeIndex -= cu.getOpCodes()
                             .size();
        }

        arguments = getRequiredArguments(opCodeIndex, operatingUnit, arguments);
        Integer result = operatingUnit.execute(opCodeIndex, arguments);
        if (result != null) {
            if (arguments != null)
                arguments.firstAddressType.write(arguments.arg1, result);
            else
                throw new IllegalArgumentException("Address type can't be const value");
        }
    }

    private IOInterface getProperIOInterfaceFromAddressType(int addressType) {
        switch (addressType) {
            case 0:
                return null;
            case 1:
                return alu.getRegisters();
            case 2:
                return memory;
            default:
                throw new IllegalArgumentException("Addressing mode doesn't exist");
        }
    }

    private Arguments getRequiredArguments(int opCodeIndex, OperatingUnit operatingUnit, Arguments arguments) {
        switch (operatingUnit.getOpCodes()
                             .get(opCodeIndex)
                             .getRequiredArguments()
            ) {
            case 2:
                int firstValueOrAddress = fetchNextOperationOrValue(cu);
                int secondValueOrAddress = fetchNextOperationOrValue(cu);
                arguments.arg1 = (arguments.firstAddressType != null ?
                    arguments.firstAddressType.read(firstValueOrAddress) :
                    firstValueOrAddress);
                arguments.arg2 = (arguments.secondAddressType != null ?
                    arguments.secondAddressType.read(secondValueOrAddress) :
                    secondValueOrAddress);
                return arguments;
            case 1:
                firstValueOrAddress = fetchNextOperationOrValue(cu);
                arguments.arg1 = (arguments.firstAddressType != null) ?
                    arguments.firstAddressType.read(firstValueOrAddress) :
                    firstValueOrAddress;
                arguments.arg2 = null;
                return arguments;
            case 0:
                arguments.arg1 = null;
                arguments.arg2 = null;
                return arguments;
        }
        return null;
    }
}
