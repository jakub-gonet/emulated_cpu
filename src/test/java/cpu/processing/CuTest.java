package cpu.processing;

import cpu.Helpers;
import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.processing.operations.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;
import java.util.List;

class CuTest {
    private Memory mem;
    private MemoryManager manager;
    private Cu cu;
    private Operation operation;
    private Registers registers;
    private Stack stack;

    @Test
    void NOP() {
        mem = new Memory(List.of(
                Helpers.opCode(0, 0, 0, 0)
        ));
        init(mem);

        int PC = operation.fetch(0);
        Assertions.assertDoesNotThrow(() -> cu.execute(PC, operation));
    }

    @Test
    void HLT() {
        mem = new Memory(List.of(
                Helpers.opCode(1, 0, 0, 0)
        ));
        init(mem);

        runNCommandsFrom(0, 1);

        Assertions.assertTrue(registers.statusRegister()
                                       .state(StatusRegister.StatusFlags.STOPPED));
    }

    @Test
    void MOV() {
        mem = new Memory(List.of(
                5,
                Helpers.opCode(2, 2, 1, 0), 0, 50
        ));
        init(mem);

        runNCommandsFrom(1, 1);

        Assertions.assertEquals(50, mem.read(0));
    }

    @Test
    void MOV_withImmediateWriteAddress() {
        mem = new Memory(List.of(
                Helpers.opCode(2, 2, 0, 0), 1, 42
        ));
        init(mem);

        int PC = operation.fetch(0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cu.execute(PC, operation));
    }

    @Test
    void JMP() {
        mem = new Memory(List.of(
                Helpers.opCode(3, 1, 0, 0), 0
        ));
        init(mem);

        int PC = runNCommandsFrom(0, 1);

        Assertions.assertEquals(0, PC);
    }

    @Test
    void JMP_intoIllegalAddress() {
        mem = new Memory(List.of(
                Helpers.opCode(3, 1, 0, 0), -1
        ));
        init(mem);

        int PC = operation.fetch(0);
        Assertions.assertThrows(IllegalStateException.class, () -> cu.execute(PC, operation));
    }

    @Test
    void JE_JNE() {
        mem = new Memory(List.of(
                Helpers.opCode(26, 2, 0, 0), 1, 1,
                Helpers.opCode(4, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 1, 2,
                Helpers.opCode(4, 1, 0, 0), 0,

                Helpers.opCode(26, 2, 0, 0), 3, 4,
                Helpers.opCode(5, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 4, 4,
                Helpers.opCode(5, 1, 0, 0), 0
        ));
        init(mem);

        Assertions.assertEquals(0, runNCommandsFrom(0, 2));
        Assertions.assertEquals(10, runNCommandsFrom(5, 2));

        Assertions.assertEquals(0, runNCommandsFrom(10, 2));
        Assertions.assertEquals(20, runNCommandsFrom(15, 2));
    }

    @Test
    void JL_JLE() {
        mem = new Memory(List.of(
                Helpers.opCode(26, 2, 0, 0), -1, 1,
                Helpers.opCode(6, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 1, 1,
                Helpers.opCode(6, 1, 0, 0), 0,

                Helpers.opCode(26, 2, 0, 0), 1, 1,
                Helpers.opCode(7, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 2, 1,
                Helpers.opCode(7, 1, 0, 0), 0
        ));
        init(mem);

        Assertions.assertEquals(0, runNCommandsFrom(0, 2));
        Assertions.assertEquals(10, runNCommandsFrom(5, 2));

        Assertions.assertEquals(0, runNCommandsFrom(10, 2));
        Assertions.assertEquals(20, runNCommandsFrom(15, 2));
    }

    @Test
    void JG_JGE() {
        mem = new Memory(List.of(
                Helpers.opCode(26, 2, 0, 0), 2, 1,
                Helpers.opCode(8, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 1, 1,
                Helpers.opCode(8, 1, 0, 0), 0,

                Helpers.opCode(26, 2, 0, 0), 1, 1,
                Helpers.opCode(9, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), -2, 1,
                Helpers.opCode(9, 1, 0, 0), 0
        ));
        init(mem);

        Assertions.assertEquals(0, runNCommandsFrom(0, 2));
        Assertions.assertEquals(10, runNCommandsFrom(5, 2));

        Assertions.assertEquals(0, runNCommandsFrom(10, 2));
        Assertions.assertEquals(20, runNCommandsFrom(15, 2));
    }

    @Test
    void PUSH_POP_withValidData() {
        mem = new Memory(List.of(
                Helpers.opCode(10, 1, 0, 0), 2,
                Helpers.opCode(11, 1, 2, 0), 0
        ));
        init(mem);

        runNCommandsFrom(0, 2);

        Assertions.assertEquals(2, registers.read(0));
    }

    @Test
    void PUSH_stackOverflow() {
        mem = new Memory(List.of(
                Helpers.opCode(10, 1, 0, 0), 2,
                Helpers.opCode(10, 1, 0, 0), 2,
                Helpers.opCode(10, 1, 0, 0), 2
        ));
        init(mem);

        int PC = runNCommandsFrom(0, 2);
        int PC2 = operation.fetch(PC);
        Assertions.assertThrows(IllegalStateException.class, () -> cu.execute(PC2, operation));
    }

    @Test
    void POP_onEmptyStack() {
        mem = new Memory(List.of(
                Helpers.opCode(11, 1, 1, 0), 0
        ));
        init(mem);

        int PC = operation.fetch(0);
        Assertions.assertThrows(EmptyStackException.class, () -> cu.execute(PC, operation));
    }

    @Test
    void CALL_RET() {
        mem = new Memory(List.of(
                Helpers.opCode(12, 1, 0, 0), 3,
                Helpers.opCode(0, 0, 0, 0),
                Helpers.opCode(13, 0, 0, 0)
        ));
        init(mem);


        int PC = runNCommandsFrom(0, 1);
        Assertions.assertFalse(stack.isEmpty());
        Assertions.assertEquals(3, PC);

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(2, PC);
    }

    @Test
    void INC_DEC() {
        mem = new Memory(List.of(
                0,
                0,
                Helpers.opCode(14, 1, 1, 0), 0,
                Helpers.opCode(15, 1, 1, 0), 1
        ));
        init(mem);

        int PC = runNCommandsFrom(2, 1);
        Assertions.assertEquals(1, mem.read(0));

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(-1, mem.read(1));
    }

    @Test
    void ADD_SUB() {
        mem = new Memory(List.of(
                5,
                3,
                Helpers.opCode(16, 2, 1, 1), 0, 0,
                Helpers.opCode(17, 2, 1, 0), 1, 8
        ));
        init(mem);

        int PC = runNCommandsFrom(2, 1);
        Assertions.assertEquals(10, mem.read(0));

        runNCommandsFrom(PC, 1);
        Assertions.assertEquals(-5, mem.read(1));
    }

    @Test
    void MUL_DIV() {
        mem = new Memory(List.of(
                5,
                24,
                Helpers.opCode(18, 2, 1, 1), 0, 0,
                Helpers.opCode(19, 2, 1, 0), 1, 8,
                Helpers.opCode(19, 2, 1, 0), 1, 0
        ));
        init(mem);

        int PC = runNCommandsFrom(2, 1);
        Assertions.assertEquals(25, mem.read(0));

        int PC2 = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(3, mem.read(1));

        Assertions.assertThrows(ArithmeticException.class, () -> runNCommandsFrom(PC2, 1));
    }

    @Test
    void logicOpCodes() {
        mem = new Memory(List.of(
                -1,
                5,
                1,
                4,
                2,
                -1,
                Helpers.opCode(20, 1, 1, 0), 0,
                Helpers.opCode(21, 2, 1, 0), 1, 4,
                Helpers.opCode(22, 2, 1, 0), 2, 8,
                Helpers.opCode(23, 2, 1, 0), 3, 1,
                Helpers.opCode(24, 2, 1, 0), 4, 2,
                Helpers.opCode(25, 2, 1, 0), 5, -2
        ));
        init(mem);

        int PC = runNCommandsFrom(6, 1);
        Assertions.assertEquals(0, mem.read(0));

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(4, mem.read(1));

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(9, mem.read(2));

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(2, mem.read(3));

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(8, mem.read(4));

        PC = runNCommandsFrom(PC, 1);
        Assertions.assertEquals(1, mem.read(5));
    }

    @Test
    void CMP() {

    }

    private void init(Memory mem) {
        registers = new Registers(8);
        manager = new MemoryManager(new MemoryManager(mem), registers);
        stack = new Stack(2);
        cu = new Cu(manager, stack);
        operation = new Operation(manager);
    }

    private int runNCommandsFrom(int startingPC, int n) {
        int PC = startingPC;
        for (int i = 0; i < n; i++) {
            PC = cu.execute(operation.fetch(PC), operation);
        }

        return PC;
    }
}

