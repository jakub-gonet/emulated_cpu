package emulated_cpu.cpu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;


class CPUTest {
    @Test
    void executeNextOperation_noOperation() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x0
            //NOP
        )));

        cpu.executeNextOperation();
    }


    @Test
    void executeNextOperation_halt() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x10
            //HLT
        )));

        cpu.executeNextOperation();
        Assertions.assertTrue(cpu.isStopped());
    }

    @Test
    void executeNextOperation_jumpConstArg() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x30, 0x0
            //JMP 0
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0x0, cpu.getInstructionPointer());
    }

    @Test
    void executeNextOperation_jumpLessEqualsRegArg() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x24, 0x1, 0x3, 0x74, 0x1
            //JMP [1]
        )));

        Method setZeroFlagStateMethod = cpu.getAluRegisters()
                                           .getStatusRegister()
                                           .getClass()
                                           .getDeclaredMethod("setZeroFlagState", boolean.class);
        setZeroFlagStateMethod.setAccessible(true);
        setZeroFlagStateMethod.invoke(cpu.getAluRegisters()
                                         .getStatusRegister(), true);


        Method setNegativeFlagStateMethod = cpu.getAluRegisters()
                                               .getStatusRegister()
                                               .getClass()
                                               .getDeclaredMethod("setNegativeFlagState", boolean.class);
        setZeroFlagStateMethod.setAccessible(true);
        setZeroFlagStateMethod.invoke(cpu.getAluRegisters()
                                         .getStatusRegister(), true);


        cpu.executeNextOperation();
        Assertions.assertEquals(0x3, cpu.getInstructionPointer());
    }

    @Test
    void executeNextOperation_butJumpConstOutOfMemoryBounds() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x30, -0x1
            //JMP -1
        )));

        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);

        cpu.restart();
        cpu.loadNewProgram(new ArrayList<>(Arrays.asList(
            0x30, 3
            //JMP 3
        )));
        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);
    }

    @Test
    void executeNextOperation_moveValueToRegister() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x24, 0x1, 0x5
            //MOV [1] 5
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0x5, cpu.getAluRegisters()
                                        .read(1));
    }

    @Test
    void executeNextOperation_moveValueToMemory() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x28, 0x1, 0x81
            //MOV &1 5
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0x81, cpu.getMemory()
                                         .read(1));
    }

    @Test
    void executeNextOperation_butMoveValueToConst() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x20, 0x1, 0x16
            //MOV 1 5
        )));

        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);
    }

    @Test
    void executeNextOperation_compareRegisterAndConst() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x24, 0x1, 0x8, 0x164, 0x1, 0x5
            //MOV [1] 8
            //CMP [1] 5
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();

        Assertions.assertTrue(cpu.getAluRegisters()
                                 .getStatusRegister()
                                 .getCarryFlagState());

        cpu.restart();
        cpu.loadNewProgram(new ArrayList<>(Arrays.asList(
            0x24, 0x1, -0x3, 0x164, 0x1, 0x0
            //MOV [1] -3
            //CMP [1] 0
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();

        Assertions.assertTrue(cpu.getAluRegisters()
                                 .getStatusRegister()
                                 .getNegativeFlagState());

        cpu.restart();
        cpu.loadNewProgram(new ArrayList<>(Arrays.asList(
            0x24, 0x1, 0x7, 0x164, 0x1, 0x7
            //MOV [1] 7
            //CMP [1] 7
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();

        Assertions.assertTrue(cpu.getAluRegisters()
                                 .getStatusRegister()
                                 .getZeroFlagState());

    }

    @Test
    void executeNextOperation_incrementRegister() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xa4, 0x1
            //INC [1]
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0x1, cpu.getAluRegisters()
                                        .read(1));
    }

    @Test
    void executeNextOperation_butIncrementConst() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xa0, 0x1
            //INC 1
        )));
        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);

    }

    @Test
    void executeNextOperation_incrementMemory() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xa8, 0x0
            //INC &0
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0xa9, cpu.getMemory()
                                         .read(0));
    }

    @Test
    void executeNextOperation_butAndWithMissingOneArgument() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x104, 0x1
            //AND [1]
        )));
        //reading outside of memory
        Assertions.assertThrows(IndexOutOfBoundsException.class, cpu::executeNextOperation);
    }


    @Test
    void executeNextOperation_calculateFactorialOf5() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x24, 0x1, 0x5, //MOV [1] 5
            0x24, 0x2, 0x1, //MOV [2] 1
            0x164, 0x1, 0,  //loop: CMP [1] 0
            0x70, 18,       //JLE hlt
            0xe5, 0x2, 0x1, //MUL [2] [1]
            0xb4, 0x1,      //DEC [1]
            0x30, 6,        //JMP loop
            0x10            //hlt: HLT
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();

        Assertions.assertEquals(120, cpu.getAluRegisters()
                                        .read(2));
    }
}