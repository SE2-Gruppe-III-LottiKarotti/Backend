package at.aau.serg.websocketdemoserver.model.logic;

import at.aau.serg.websocketdemoserver.logic.RandomCardGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RandomCardGeneratorTest {

    //test strategy: lower/nominal/upper - boundary value for every valid input and four error cases

    @Test
    public void testCalcVal_0_THREE () {
        assertEquals("THREE", RandomCardGenerator.calculate(0));
    }

    @Test
    public void testCalcVal_2_THREE () {
        assertEquals("THREE", RandomCardGenerator.calculate(2));
    }

    @Test
    public void testCalcVal_3_THREE () {
        assertEquals("THREE", RandomCardGenerator.calculate(3));
    }

    @Test
    public void testCalcVal_4_TWO () {
        assertEquals("TWO", RandomCardGenerator.calculate(4));
    }

    @Test
    public void testCalcVal_7_TWO () {
        assertEquals("TWO", RandomCardGenerator.calculate(7));
    }

    @Test
    public void testCalcVal_10_TWO () {
        assertEquals("TWO", RandomCardGenerator.calculate(10));
    }

    @Test
    public void testCalcVal_11_CARROT () {
        assertEquals("CARROT", RandomCardGenerator.calculate(11));
    }

    @Test
    public void testCalcVal_15_CARROT () {
        assertEquals("CARROT", RandomCardGenerator.calculate(15));
    }

    @Test
    public void testCalcVal_21_CARROT () {
        assertEquals("CARROT", RandomCardGenerator.calculate(21));
    }

    @Test
    public void testCalcVal_22_ONE () {
        assertEquals("ONE", RandomCardGenerator.calculate(22));
    }

    @Test
    public void testCalcVal_30_ONE () {
        assertEquals("ONE", RandomCardGenerator.calculate(30));
    }

    @Test
    public void testCalcVal_45_ONE () {
        assertEquals("ONE", RandomCardGenerator.calculate(45));
    }

    @Test
    public void testCalculateInvalidValueNegative_ERROR() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RandomCardGenerator.calculate(-1));
        assertEquals("error - number has to be between 0 and 45", exception.getMessage());
    }

    @Test
    public void testCalculateInvalidValueNegative_ERROR_defitiveMin() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RandomCardGenerator.calculate(Integer.MIN_VALUE));
        assertEquals("error - number has to be between 0 and 45", exception.getMessage());
    }

    @Test
    public void testCalculateInvalidValueTooHigh_ERROR() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RandomCardGenerator.calculate(46));
        assertEquals("error - number has to be between 0 and 45", exception.getMessage());
    }

    @Test
    public void testCalculateInvalidValueNegative_ERROR_defitiveMax() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RandomCardGenerator.calculate(Integer.MAX_VALUE));
        assertEquals("error - number has to be between 0 and 45", exception.getMessage());
    }


}
