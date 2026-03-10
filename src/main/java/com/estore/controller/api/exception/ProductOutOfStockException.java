package com.estore.controller.api.exception;

public class ProductOutOfStockException extends Exception {
    private int productId;

    public static ProductOutOfStockException createWith(int productId) {
        return new ProductOutOfStockException(productId);
    }

    private ProductOutOfStockException(int productId) {
        this.productId = productId;
    }

    @Override
    public String getMessage() {
        return "Product '" + productId + "' out of stock";
    }
}