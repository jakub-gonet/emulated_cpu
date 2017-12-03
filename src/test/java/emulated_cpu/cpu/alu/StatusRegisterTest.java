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

    @Test
    void setters() {
        StatusRegister statusRegister = new StatusRegister();
        statusRegister.setZeroFlagState(true);
        Assertions.assertTrue((statusRegister.getValueAt(0) == 1));

        statusRegister = new StatusRegister();
        statusRegister.setCarryFlagState(true);
        Assertions.assertTrue((statusRegister.getValueAt(1) == 1));

        statusRegister = new StatusRegister();
        statusRegister.setNegativeFlagState(true);
        Assertions.assertTrue((statusRegister.getValueAt(2) == 1));
    }
}