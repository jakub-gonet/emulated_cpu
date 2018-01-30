package emulated_cpu.cpu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class CPUTest {
    @Test
    void noOperation() {
        /*
        NOP
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x0
        )));

        cpu.executeNextOperation();
    }


    @Test
    void halt() {
        /*
        HLT
        */

        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x40
        )));

        cpu.executeNextOperation();
        Assertions.assertTrue(cpu.isStopped());
    }

    @Test
    void jumpToConstValue() {
        /*
        JMP 0
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xc0, 0
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0, cpu.getInstructionPointer());
    }

    @Test
    void jumpToAddressStoredInRegister() {
        /*
        MOV [1], 3
        JMP &[1]
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 3, 0xc8, 1
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();
        Assertions.assertEquals(3, cpu.getInstructionPointer());
    }

    @Test
    void jumpOutOfMemoryBounds() {
        /*
        JMP &-1
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xd0, -1
        )));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, cpu::executeNextOperation);

        //==============================================

        /*
        JMP &3
        */
        cpu = new CPU(new ArrayList<>(Arrays.asList(
            0xd0, 3
        )));
        cpu.executeNextOperation();
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, cpu::executeNextOperation);
    }

    @Test
    void moveValueToRegister() {
        /*
        MOV [1], 5
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 5
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(5, cpu.getAluRegisters()
                                      .read(1));
    }

    @Test
    void moveValueToMemory() {
        /*
        MOV &1, 81
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x90, 1, 81
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(81, cpu.getMemory()
                                       .read(1));
    }

    @Test
    void moveRegisterToMemoryAddressStoredInRegister() {
        /*
        MOV [1] 50
        MOV [2] 3
        MOV &[2] [1]
        HLT
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 50, 0x88, 2, 3, 0x99, 2, 1, 0x40
        )));

        cpu.executeOperationsUntilHLT();
        Assertions.assertEquals(50, cpu.getMemory()
                                       .read(3));
    }

    @Test
    void moveConstValueToConst() {
        /*
        MOV 1, 16
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x80, 1, 16
        )));

        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);
    }

    @Test
    void compareRegisterAndConst() {
        /*
        MOV [1], 8
        CMP [1], 5
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 8, 0x648, 1, 5
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();

        Assertions.assertTrue(cpu.getAluRegisters()
                                 .getStatusRegister()
                                 .getCarryFlagState());
        //==============================================

        /*
        MOV [1], -3
        CMP [1], 0
        */
        cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, -3, 0x648, 1, 0
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();

        Assertions.assertTrue(cpu.getAluRegisters()
                                 .getStatusRegister()
                                 .getNegativeFlagState());

        //==============================================

        /*
        MOV [1], 7
        CMP [1], 7
        */
        cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x88, 1, 7, 0x648, 1, 7
        )));
        cpu.executeNextOperation();
        cpu.executeNextOperation();

        Assertions.assertTrue(cpu.getAluRegisters()
                                 .getStatusRegister()
                                 .getZeroFlagState());

    }

    @Test
    void incrementRegister() {
        /*
        INC [1]
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x348, 1
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(1, cpu.getAluRegisters()
                                      .read(1));
    }

    @Test
    void IncrementConst() {
        /*
        INC 1
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x340, 1
        )));
        Assertions.assertThrows(IllegalArgumentException.class, cpu::executeNextOperation);

    }

    @Test
    void incrementMemory() {
        /*
        INC &0
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x350, 0
        )));

        cpu.executeNextOperation();
        Assertions.assertEquals(0x351, cpu.getMemory()
                                          .read(0));
    }

    @Test
    void AndWithMissingOneArgument() {
        /*
        AND [1]
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0x3c8, 1
        )));
        //reading outside of memory
        Assertions.assertThrows(IndexOutOfBoundsException.class, cpu::executeNextOperation);
    }


    @Test
    void calculateFactorialOf5() {
        /*
        MOV [1], 5
        MOV [2], 1
        loop:
          CMP [1], 0
          JLE hlt

          MUL [2], [1]
          DEC [1]

          JMP loop

         hlt:
           HLT
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            136, 1, 5, 136, 2, 1, 1608,
            1, 0, 448, 18, 1097, 2, 1,
            904, 1, 192, 6, 64
        )));

        cpu.executeOperationsUntilHLT();
        Assertions.assertEquals(120, cpu.getAluRegisters()
                                        .read(2));
    }

    @Test
    void calculate20thFibonacciNumber() {
        /*
        MOV [2], 0
        MOV [3], 1
        MOV [4], 20
        loop:
          CMP [4], 0
          JE hlt

          MOV [1], [3]
          ADD [3], [2]
          MOV [2], [1]

          DEC [4]
          JMP loop

        hlt:
          HLT
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            136, 2, 0, 136, 3, 1, 136, 4, 20, 1608,
            4, 0, 256, 27, 137, 1, 3, 969, 3, 2, 137,
            2, 1, 904, 4, 192, 9, 64
        )));

        cpu.executeOperationsUntilHLT();
        Assertions.assertEquals(6765, cpu.getAluRegisters()
                                         .read(2));
    }

    @Test
    void pushAndPop() {
        /*
        MOV [1], 8
        PUSH 6
        POP [2]
        ADD [1], [2]
        HLT
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            136, 1, 8, 640, 6, 712, 2, 969, 1, 2, 64
        )));

        cpu.executeOperationsUntilHLT();
        Assertions.assertEquals(14, cpu.getAluRegisters()
                                       .read(1));
    }


    @Test
    void callAddFunction() {
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

        cpu.executeOperationsUntilHLT();
        Assertions.assertEquals(14, cpu.getAluRegisters()
                                       .read(2));

    }

    @Test
    void bubbleSort() {
        /*
        NOP NOP NOP NOP NOP

        NOP NOP NOP NOP NOP

        # overwrite first 5 numbers in code and store values of an array there (indexes 0 -> 4)
        MOV &0, 5
        MOV &1, 3
        MOV &2, 7
        MOV &3, 9
        MOV &4, 1

        # store copy of the array next to it (indexes 5 -> 9)
        MOV &5, &0
        MOV &6, &1
        MOV &7, &2
        MOV &8, &3
        MOV &9, &4

        MOV [1], 5 # address of first value in the array
        MOV [2], 5 # size of the array

        # calculate last index
        MOV [3], [1]
        ADD [3], [2] # [3] <- [1] + [2]

        #####
        MOV [4], [1] # first loop index -> i = t[0]
        main_loop:

          MOV [5], [1]
          MOV [6], [5]  # second loop index j-1
          INC [5]       # second loop index j
          ###
          inner_loop:
            CMP &[6], &[5]
            JG swap

        swapped:

        INC [5]
        INC [6]
        CMP [5], [3]
        JL inner_loop # loop while j < size of the array
        ###

        INC [4]
        CMP [4], [3]
        JL main_loop # loop while i < size of the array
        #####
        HLT

        swap:
          MOV [7], &[6]    # save old j-1 value
          MOV &[6], &[5]   # swap j-1 with j
          MOV &[5], [7]    # swap j with old j-1
        JMP swapped
        */

        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 144, 0, 5, 144,
            1, 3, 144, 2, 7, 144, 3, 9, 144, 4, 1, 146, 5,
            0, 146, 6, 1, 146, 7, 2, 146, 8, 3, 146, 9, 4,
            136, 1, 5, 136, 2, 5, 137, 3, 1, 969, 3, 2, 137,
            4, 1, 137, 5, 1, 137, 6, 5, 840, 5, 1627, 6, 5, 512,
            85, 840, 5, 840, 6, 1609, 5, 3, 384, 63, 840, 4, 1609,
            4, 3, 384, 55, 64, 139, 7, 6, 155, 6, 5, 153, 5, 7, 192, 68
        )), 7, 0);

        cpu.executeOperationsUntilHLT();
        System.out.println(cpu.getAluRegisters());
        System.out.println(cpu.getMemory());
    }

    @Test
    void executeNextOperation_addEfficiency() {
        /*
        MOV [1], 0
        loop:
        CMP [1], 4096
        JE end
        INC [1]

        JMP loop
        end:
        HLT
        */
        CPU cpu = new CPU(new ArrayList<>(Arrays.asList(
            136, 1, 0, 1608, 1, 4096, 256, 12, 840, 1, 192, 3, 64
        )), 7, 0);
        long start = System.currentTimeMillis();
        while (!cpu.isStopped()) cpu.executeNextOperation();
        long end = System.currentTimeMillis();
        System.out.println("CPU was working " + (end - start) + "ms");

        Assertions.assertEquals(4096, cpu.getAluRegisters()
                                         .read(1));
    }
}