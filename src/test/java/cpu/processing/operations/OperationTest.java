package cpu.processing.operations;

import cpu.memory.Memory;
import cpu.memory.MemoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.util.List;

class OperationTest {
    private Operation op;

    @BeforeEach
    void setup() throws InvalidKeyException {
        Memory mem = new Memory(List.of(
                0xcafe,
                0xbabe,
                9 << 8,
                8 << 8 | 1 << 6 | 0 << 3, 0,
                7 << 8 | 2 << 6 | 0 << 3 | 0 << 3, 0, 1,
                6 << 8 | 1 << 6 | 5 << 3, 0,
                5 << 8 | 3 << 6));
        MemoryManager manager = new MemoryManager();
        manager.addReadableDevice(0, mem);

        this.op = new Operation(manager);
    }

    @Test
    void fetchingOpCodeWithVariousArgNumsMovesPC() {
        Assertions.assertEquals(3, op.fetch(2));
        Assertions.assertEquals(5, op.fetch(3));
        Assertions.assertEquals(8, op.fetch(5));
    }

    @Test
    void fetchingOpCodesGivesVariousArgsListsAndOpCodesNum() {
        op.fetch(2);
        Assertions.assertEquals(List.of(), op.args());
        Assertions.assertEquals(9, op.opCodeNum());

        op.fetch(3);
        Assertions.assertEquals(List.of(0xcafe), op.args());
        Assertions.assertEquals(8, op.opCodeNum());

        op.fetch(5);
        Assertions.assertEquals(List.of(0xcafe, 0xbabe), op.args());
        Assertions.assertEquals(7, op.opCodeNum());
    }

    @Test
    void fetchingFromDeviceWithInvalidIdThrowsAnException() {
        Assertions.assertThrows(IllegalStateException.class, () -> op.fetch(8));
    }

    @Test
    void fetchingOpCodeWithInvalidArgNumberThrowAnException() {
        Assertions.assertThrows(IllegalStateException.class, () -> op.fetch(10));
    }

}