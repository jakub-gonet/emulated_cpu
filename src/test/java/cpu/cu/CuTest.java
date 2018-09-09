package cpu.cu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class CuTest {
    @Test
    void canLoadProgramIntoMemory() {
        Cu cu = new Cu(50);
        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, 0);

        Assertions.assertTrue(cu.loadIntoMemory(programHolder, 0, 5, 0));
    }

    @Test
    void cantLoadProgramWithReadWriteErrors() {
        Cu cu = new Cu(10);
        SimpleReadableDevice programHolder = new SimpleReadableDevice(10, 0);

        Assertions.assertFalse(cu.loadIntoMemory(programHolder, 0, 50, 0));
        Assertions.assertFalse(cu.loadIntoMemory(programHolder, 0, 10, 50));
    }

    @Test
    void loadedProgramIsDeviceMemorySublist() {
        Cu cu = new Cu(50);
        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, 0);

        Assertions.assertTrue(cu.loadIntoMemory(programHolder, 0, 5, 0));
        Assertions.assertTrue(isSublist(programHolder.rawContent(), cu.memory().rawContent()));
    }

    <E> boolean isSublist(List<E> sublist, List<E> list) {
        return Collections.indexOfSubList(list, sublist) != -1;
    }
}

