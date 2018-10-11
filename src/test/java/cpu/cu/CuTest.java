package cpu.cu;

import cpu.Helpers;
import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.memory.registers.StatusRegister;
import cpu.processing.Cu;
import cpu.processing.operations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CuTest {
    private Memory mem;
    private MemoryManager manager;
    private Cu cu;
    private Operation operation;
    private Registers registers;
    private Logger logger = LogManager.getLogger(CuTest.class);

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

        int PC = operation.fetch(0);
        cu.execute(PC, operation);

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

        int PC = operation.fetch(1);
        cu.execute(PC, operation);

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

        int PC = operation.fetch(0);
        PC = cu.execute(PC, operation);

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
    void PUSH_POP() {

    }

    @Test
    void CALL_RET() {

    }

    @Test
    void arithmeticOpCodes() {

    }

    @Test
    void logicOpCodes() {

    }

    @Test
    void CMP() {

    }

    private void init(Memory mem) {
        registers = new Registers(8);
        manager = new MemoryManager(new MemoryManager(mem), registers);
        cu = new Cu(manager, new Stack(2));
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

