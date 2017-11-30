package emulated_cpu.cpu.alu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusRegisterTest {
    @Test
    void getters() {
        StatusRegister statusRegister = new StatusRegister(6);
        Assertions.assertFalse(statusRegister.getZeroFlagState());
        Assertions.assertTrue(statusRegister.getCarryFlagState());
        Assertions.assertTrue(statusRegister.getNegativeFlagState());
    }
}