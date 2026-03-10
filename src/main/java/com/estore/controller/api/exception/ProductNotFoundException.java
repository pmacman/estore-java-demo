package com.estore.controller.api.exception;

public class ProductNotFoundException extends Exception {
    private int productId;

    public static ProductNotFoundException createWith(int productId) {
        return new ProductNotFoundException(productId);
    }

    private ProductNotFoundException(int productId) {
        this.productId = productId;
    }

    @Override
    public String getMessage() {
        return "Product '" + productId + "' not found";
    }
}