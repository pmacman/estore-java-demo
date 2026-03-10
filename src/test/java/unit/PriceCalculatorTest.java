package unit;

import com.estore.utility.PriceCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PriceCalculatorTest {
    @Test
    public void calculateTax() {
        double result = PriceCalculator.calculateTax(30);
        assertEquals(3, result, 0);
    }
}