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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CuTest {
    private Memory mem;
    private MemoryManager manager;
    private Cu cu;
    private Operation operation;
    private Registers registers;
    private Logger logger = LogManager.getLogger(CuTest.class);

    @BeforeEach
    void setup() {
        mem = new Memory(List.of(
                -1,
                5,
                Helpers.opCode(0, 0, 0, 0),
                Helpers.opCode(1, 0, 0, 0),
                Helpers.opCode(2, 2, 1, 0), 1, 50,
                Helpers.opCode(2, 2, 0, 0), 1, 42,
                Helpers.opCode(3, 1, 0, 0), 0,
                Helpers.opCode(3, 1, 0, 0), -1,
                Helpers.opCode(26, 2, 0, 0), 1, 1,
                Helpers.opCode(4, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 1, 2,
                Helpers.opCode(4, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 3, 4,
                Helpers.opCode(5, 1, 0, 0), 0,
                Helpers.opCode(26, 2, 0, 0), 4, 4,
                Helpers.opCode(5, 1, 0, 0), 0
        ));
        registers = new Registers(8);
        manager = new MemoryManager(new MemoryManager(mem), registers);
        cu = new Cu(manager, new Stack());
        operation = new Operation(manager);
    }

    @Test
    void NOP() {
        int PC = operation.fetch(2);
        Assertions.assertDoesNotThrow(() -> cu.execute(PC, operation));
    }

    @Test
    void HLT() {
        int PC = operation.fetch(3);
        cu.execute(PC, operation);

        Assertions.assertTrue(registers.statusRegister()
                                       .state(StatusRegister.StatusFlags.STOPPED));
    }

    @Test
    void MOV() {
        int PC = operation.fetch(4);
        cu.execute(PC, operation);

        Assertions.assertEquals(50, mem.read(1));
    }

    @Test
    void MOV_withImmediateWriteAddress() {
        int PC = operation.fetch(7);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cu.execute(PC, operation));
    }

    @Test
    void JMP() {
        int PC = operation.fetch(10);
        PC = cu.execute(PC, operation);

        Assertions.assertEquals(0, PC);
    }

    @Test
    void JMP_intoIllegalAddress() {
        int PC = operation.fetch(12);
        Assertions.assertThrows(IllegalStateException.class, () -> cu.execute(PC, operation));
    }

    @Test
    void JE_JNE() {
        Assertions.assertEquals(0, runNCommandsFrom(14, 2));
        Assertions.assertEquals(24, runNCommandsFrom(19, 2));

        Assertions.assertEquals(0, runNCommandsFrom(24, 2));
        Assertions.assertEquals(34, runNCommandsFrom(29, 2));
    }

    @Test
    void JL_JLE() {

    }

    @Test
    void JG_JGE() {

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

    private int runNCommandsFrom(int startingPC, int n) {
        int PC = startingPC;
        for (int i = 0; i < n; i++) {
            PC = cu.execute(operation.fetch(PC), operation);
        }

        return PC;
    }
}

