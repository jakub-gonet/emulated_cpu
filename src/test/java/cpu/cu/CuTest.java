package cpu.cu;

import cpu.Helpers;
import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import cpu.memory.Stack;
import cpu.memory.registers.Registers;
import cpu.processing.Cu;
import cpu.processing.operations.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CuTest {
    @Test
    void canExecuteJumpOpCodes() {
        Memory mem = new Memory(List.of(
                0,
                5,
                Helpers.opCode(3, 1, 0, 0),
                0
        ));
        MemoryManager manager = new MemoryManager(new MemoryManager(mem), new Registers(8));
        Cu cu = new Cu(manager, new Stack());

        Operation operation = new Operation(manager);
        int PC = operation.fetch(2);

        PC = cu.execute(PC, operation);

        Assertions.assertEquals(0, PC);
    }
}

