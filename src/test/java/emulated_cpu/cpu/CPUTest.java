package emulated_cpu.cpu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
            0x40
            //HLT
        )));

        cpu.executeNextOperation();
        Assertions.assertTrue(cpu.isStopped());
    }

    @Test
    void executeNextOperation_jumpConstArg() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xc0, 0
            //JMP 0
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0, cpu.getInstructionPointer());
    }

    @Test
    void executeNextOperation_jumpLessEqualsRegArg() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 3, 0x1c8, 1
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
        Assertions.assertEquals(3, cpu.getInstructionPointer());
    }

    @Test
    void executeNextOperation_butJumpConstOutOfMemoryBounds() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xc0, -1
            //JMP -1
        )));

        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);

        cpu.restart();
        cpu.loadNewProgram(new ArrayList<>(Arrays.asList(
            0xc0, 3
            //JMP 3
        )));
        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);
    }

    @Test
    void executeNextOperation_moveValueToRegister() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 5
            //MOV [1] 5
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(5, cpu.getAluRegisters()
                                      .read(1));
    }

    @Test
    void executeNextOperation_moveValueToMemory() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x90, 1, 81
            //MOV &1 5
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(81, cpu.getMemory()
                                       .read(1));
    }

    @Test
    void executeNextOperation_moveRegisterToMemoryAddressStoredInRegister() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 50, 0x88, 2, 3, 0x99, 2, 1, 0x40
            //MOV [1] 50
            //MOV [2] 3
            //MOV &[2] [1]
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();
        Assertions.assertEquals(50, cpu.getMemory()
                                       .read(3));
    }

    @Test
    void executeNextOperation_butMoveValueToConst() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x80, 1, 16
            //MOV 1 16
        )));

        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);
    }

    @Test
    void executeNextOperation_compareRegisterAndConst() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 8, 0x588, 1, 5
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
            0x88, 1, -3, 0x588, 1, 0
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
            0x88, 1, 7, 0x588, 1, 7
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
            0x288, 1
            //INC [1]
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(1, cpu.getAluRegisters()
                                      .read(1));
    }

    @Test
    void executeNextOperation_butIncrementConst() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x280, 1
            //INC 1
        )));
        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);

    }

    @Test
    void executeNextOperation_incrementMemory() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x290, 0
            //INC &0
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0x291, cpu.getMemory()
                                          .read(0));
    }

    @Test
    void executeNextOperation_butAndWithMissingOneArgument() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x408, 1
            //AND [1]
        )));
        //reading outside of memory
        Assertions.assertThrows(IndexOutOfBoundsException.class, cpu::executeNextOperation);
    }


    @Test
    void executeNextOperation_calculateFactorialOf5() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 5, //MOV [1] 5
            0x88, 2, 1, //MOV [2] 1
            0x588, 1, 0,  //loop: CMP [1] 0
            0x1c0, 18,       //JLE hlt
            0x389, 0x2, 0x1, //MUL [2] [1]
            0x2c8, 0x1,      //DEC [1]
            0xc0, 6,        //JMP loop
            0x40            //hlt: HLT
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();

        Assertions.assertEquals(120, cpu.getAluRegisters()
                                        .read(2));
    }

    @Test
    void executeNextOperation_calculate20thFibonacciNumber() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 2, 0, //MOV [2] 0
            0x88, 3, 1, //MOV [3] 1
            0x88, 4, 20, //MOV [4] 5
            0x588, 4, 0,//loop(9): CMP [4] 0
            0x100, 27,   //JE hlt
            0x89, 1, 3, //MOV [1] [3]
            0x309, 3, 2, //ADD [3] [2]
            0x89, 2, 1, //MOV [2] [1]
            0x2c8, 4,    //DEC [4]
            0xc0, 9,    //JMP loop
            0x40        //hlt(27): HLT
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();

        Assertions.assertEquals(6765, cpu.getAluRegisters()
                                         .read(2));
    }

    @Disabled
    @Test
    void executeNextOperation_indirectAddressingMode() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x24, 1, 16,
            0x24, 2, 17,
            0x27, 3, 1,
            0x2f, 1, 2,
            0x2d, 2, 3,
            0x10,
            1,
            2
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();

        System.out.println(cpu.getAluRegisters());
        System.out.println(cpu.getMemory());
    }

    @Test
    void executeNextOperation_pushAndPop() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            136, 1, 17, 648, 1, 136, 1, 1, 648, 1, 136, 1, 3, 648, 1, 192, 18, 64, 712, 1, 712, 2, 905, 1, 2, 712, 3, 200, 3
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();

        System.out.println(cpu.getAluRegisters());
        System.out.println(cpu.getMemory());
    }


    @Test
    void executeNextOperation_callAddFunction() {
        /*
        MOV [2], 8
        MOV [3], 6

        PUSH [2]
        PUSH [3]

        CALL add

        HLT

        add:
            POP [1]
            POP [2]
            POP [3]

            ADD [2], [3]

            JMP [1]
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            136, 2, 8, 136, 3, 6, 648, 2, 648, 3, 768, 13, 64, 712, 1, 712, 2, 712, 3, 969, 2, 3, 200, 1
        )));

        while (!cpu.isStopped()) cpu.executeNextOperation();

        System.out.println(cpu.getAluRegisters());
        System.out.println(cpu.getMemory());
        Assertions.assertEquals(14, cpu.getAluRegisters()
                                       .read(2));

    }

    @Disabled
    @Test
    void executeNextOperation_bubbleSort() {
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 144, 0, 5, 144,
            1, 3, 144, 2, 7, 144, 3, 9, 144, 4, 1, 146, 5,
            0, 146, 6, 1, 146, 7, 2, 146, 8, 3, 146, 9, 4,
            136, 1, 5, 136, 2, 5, 137, 3, 1, 777, 3, 2, 137,
            4, 1, 137, 5, 1, 137, 6, 5, 648, 5, 1435, 6, 5, 512,
            85, 648, 5, 648, 6, 1417, 5, 3, 384, 63, 648, 4, 1417,
            4, 3, 384, 55, 64, 139, 7, 6, 155, 6, 5, 153, 5, 7, 192, 68
        )), 7, 0);

        while (!cpu.isStopped()) cpu.executeNextOperation();
        System.out.println(cpu.getAluRegisters());
        System.out.println(cpu.getMemory());
    }
}