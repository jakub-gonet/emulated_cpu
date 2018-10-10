package cpu.memory.addressing_modes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImmediateTest {

    @Test
    void canObtainImmediateValue() {
        Immediate immediate = new Immediate();

        Assertions.assertTrue(immediate.canReadAt(-5));
        Assertions.assertEquals(5, immediate.read(5));
    }

}