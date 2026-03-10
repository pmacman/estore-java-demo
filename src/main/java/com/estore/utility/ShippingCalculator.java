package com.estore.utility;

/**
 * Additional logic for calculating shipping could be entered here.
 */
public class ShippingCalculator {
    public static double calculateShippingCost(String shippingMethod) {
        if (shippingMethod == null) {
            shippingMethod = "STANDARD";
        }

        double shippingCost = 0;

        switch (shippingMethod) {
            case "PRIORITY":
                shippingCost = 10;
                break;
            case "STANDARD":
                shippingCost = 5;
                break;
        }

        return shippingCost;
    }
}