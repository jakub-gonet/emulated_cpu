package cpu;

import cpu.cu.SimpleReadableDevice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class CpuTest {
    @Test
    void canLoadProgramFromDeviceIntoMemory() {
        Cpu cpu = new Cpu(5, 1);

        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, 0);

        Assertions.assertTrue(cpu.loadIntoMemory(programHolder, 0, 5, 0));
    }

    @Test
    void cantLoadProgramWithReadWriteErrors() {
        Cpu cpu = new Cpu(10, 1);
        SimpleReadableDevice programHolder = new SimpleReadableDevice(10, 0);

        Assertions.assertFalse(cpu.loadIntoMemory(programHolder, 0, 50, 0));
        Assertions.assertFalse(cpu.loadIntoMemory(programHolder, 5, 10, 0));
        Assertions.assertFalse(cpu.loadIntoMemory(programHolder, 0, 10, 50));
    }

    @Test
    void loadedProgramIsDeviceMemorySublist() {
        Cpu cpu = new Cpu(50, 1);
        SimpleReadableDevice programHolder = new SimpleReadableDevice(5, 0);

        Assertions.assertTrue(cpu.loadIntoMemory(programHolder, 0, 5, 0));
        Assertions.assertTrue(isSublist(programHolder.rawContent(), cpu.memory().rawContent()));
    }

    private <E> boolean isSublist(List<E> sublist, List<E> list) {
        return Collections.indexOfSubList(list, sublist) != -1;
    }

}
