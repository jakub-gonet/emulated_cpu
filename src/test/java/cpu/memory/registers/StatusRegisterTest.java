package cpu.memory.registers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static cpu.memory.registers.StatusRegister.StatusFlags;

class StatusRegisterTest {
    @Test
    void canCreateStatusRegisterWithInitialValue() {
        StatusRegister status = new StatusRegister(3);

        Assertions.assertTrue(status.state(StatusFlags.ZERO));
        Assertions.assertTrue(status.state(StatusFlags.POSITIVE));
        Assertions.assertFalse(status.state(StatusFlags.NEGATIVE));
        Assertions.assertFalse(status.state(StatusFlags.STOPPED));
    }

    @ParameterizedTest
    @EnumSource(StatusFlags.class)
    void canChangeStateOfFlags(StatusFlags flag) {
        StatusRegister status = new StatusRegister();

        Assertions.assertFalse(status.state(flag));
        status.set(flag, true);
        Assertions.assertTrue(status.state(flag));
        status.set(flag, false);
        Assertions.assertFalse(status.state(flag));
    }

    @Test
    void canResetArithmeticFlags(){
        StatusRegister status = new StatusRegister();

        status.set(StatusFlags.STOPPED, true);
        status.set(StatusFlags.POSITIVE, true);
        status.set(StatusFlags.ZERO, true);

        status.resetArithmeticFlags();

        Assertions.assertFalse(status.state(StatusFlags.POSITIVE));
        Assertions.assertFalse(status.state(StatusFlags.NEGATIVE));
        Assertions.assertFalse(status.state(StatusFlags.ZERO));
        Assertions.assertTrue(status.state(StatusFlags.STOPPED)); 
    }
}