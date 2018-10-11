package cpu.processing.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpCodeTest {
    @Test
    void canCreateOpCodeWithChosenFunction() {
        OpCode opCode = new OpCode((x, y) -> 5, 2);
        Assertions.assertEquals(2, opCode.requiredArguments());
    }

    @Test
    void applyingWrongNumberOfArgsThrowsAnException() {
        OpCode opCode = new OpCode((x, y) -> 5, 2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> opCode.applyOperation(new ArrayList<>()));
    }

    @Test
    void applyingTwoArgsReturnsValue() {
        OpCode opCode = new OpCode((x, y) -> x * y, 2);
        Assertions.assertEquals(10, (int) opCode.applyOperation(List.of(2, 5)));
    }

    @Test
    void applyingOneArgReturnsValue() {
        OpCode opCode = new OpCode((x, _y) -> -x, 1);
        Assertions.assertEquals(-5, (int) opCode.applyOperation(List.of(5)));
    }

    @Test
    void applyingZeroArgsReturnsValue() {
        OpCode opCode = new OpCode((x, y) -> 42, 0);
        Assertions.assertEquals(42, (int) opCode.applyOperation(new ArrayList<>()));
    }
}