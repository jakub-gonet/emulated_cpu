package cpu.cu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class CuTest {
    @Test
    void canLoadProgramIntoMemory() {
        Cu cu = new Cu();
        SimpleReadableDevice programHolder = new SimpleReadableDevice(5);

        Assertions.assertTrue(cu.loadIntoMemory(programHolder, 0, 5, 0));
    }
}

