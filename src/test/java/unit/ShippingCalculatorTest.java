package unit;

import com.estore.infrastructure.EstoreConstants;
import com.estore.utility.ShippingCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShippingCalculatorTest {
    @Test
    public void calculateShippingCost_Standard() {
        double result = ShippingCalculator.calculateShippingCost(EstoreConstants.ShippingMethod.STANDARD.name());
        assertEquals(5, result, 0);
    }

    @Test
    public void calculateShippingCost_Priority() {
        double result = ShippingCalculator.calculateShippingCost(EstoreConstants.ShippingMethod.PRIORITY.name());
        assertEquals(10, result, 0);
    }
}