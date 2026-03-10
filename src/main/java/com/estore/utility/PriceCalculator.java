package com.estore.utility;

/**
 * Additional logic could be added to this class to calculate based on product type, shipping location, etc.
 */
public class PriceCalculator {
    public static double calculateTax(double baseTotal) {
        // Logic for calculating tax could be entered here. Defaulting to 10%.
        return (int)(baseTotal * (10/100.0f));
    }
}