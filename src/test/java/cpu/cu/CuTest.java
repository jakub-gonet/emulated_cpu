package cpu.cu;

import cpu.memory.Memory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class CuTest {
    @Test
    void canLoadProgramIntoMemory() {
        Cu cu = new Cu(new Memory(10));
        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, 0);

        Assertions.assertTrue(cu.loadIntoMemory(programHolder, 0, 5, 0));
    }

    @Test
    void cantLoadProgramWithReadWriteErrors() {
        Cu cu = new Cu(new Memory(10));
        SimpleReadableDevice programHolder = new SimpleReadableDevice(10, 0);

        Assertions.assertFalse(cu.loadIntoMemory(programHolder, 0, 50, 0));
        Assertions.assertFalse(cu.loadIntoMemory(programHolder, 0, 10, 50));
    }

    @Test
    void loadedProgramIsDeviceMemorySublist() {
        Cu cu = new Cu(new Memory(50));
        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, 0);

        Assertions.assertTrue(cu.loadIntoMemory(programHolder, 0, 5, 0));
        Assertions.assertTrue(isSublist(programHolder.rawContent(), cu.memory().rawContent()));
    }

    <E> boolean isSublist(List<E> sublist, List<E> list) {
        return Collections.indexOfSubList(list, sublist) != -1;
    }

    @Test
    void canExecuteCuOpcode() {
        Cu cu = new Cu(new Memory(20));
        int stackPushOpCode = 5;
        int stackPopOpCode = 6;
        int immediateMode = 0;
        int memoryMode = 1;

        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, List.of(
                stackPushOpCode << 6 | immediateMode << 3 | immediateMode,
                5,
                stackPopOpCode << 6 | memoryMode << 3,
                10
        ));

        cu.executeNext();
        cu.executeNext();

        Assertions.assertEquals(5, cu.memory().read(10));
    }
}

