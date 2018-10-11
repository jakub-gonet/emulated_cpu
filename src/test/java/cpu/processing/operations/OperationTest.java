package cpu.processing.operations;

import cpu.Helpers;
import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class OperationTest {
    private Operation op;

    @BeforeEach
    void setup() {
        Memory mem = new Memory(List.of(
                Helpers.opCode(9, 0, 0, 0),
                Helpers.opCode(8, 1, 0, 0), 0xcafe,
                Helpers.opCode(7, 2, 0, 0), 0xcafe, 0xbabe,
                Helpers.opCode(6, 1, 5, 0), 0,
                Helpers.opCode(5, 3, 0, 0)
        ));
        MemoryManager manager = new MemoryManager(mem);
        this.op = new Operation(manager);
    }

    @Test
    void fetchingOpCodeWithVariousArgNumsMovesPC() {
        Assertions.assertEquals(1, op.fetch(0));
        Assertions.assertEquals(3, op.fetch(1));
        Assertions.assertEquals(6, op.fetch(3));
    }

    @Test
    void fetchingOpCodesGivesVariousArgsListsAndOpCodesNum() {
        op.fetch(0);
        Assertions.assertEquals(List.of(), op.args());
        Assertions.assertEquals(9, op.opCodeNum());

        op.fetch(1);
        Assertions.assertEquals(List.of(0xcafe), op.args());
        Assertions.assertEquals(8, op.opCodeNum());

        op.fetch(3);
        Assertions.assertEquals(List.of(0xcafe, 0xbabe), op.args());
        Assertions.assertEquals(7, op.opCodeNum());
    }

    @Test
    void fetchingFromDeviceWithInvalidIdThrowsAnException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> op.fetch(6));
    }

    @Test
    void fetchingOpCodeWithInvalidArgNumberThrowAnException() {
        Assertions.assertThrows(IllegalStateException.class, () -> op.fetch(8));
    }
}