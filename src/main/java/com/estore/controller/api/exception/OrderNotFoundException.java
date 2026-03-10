package com.estore.controller.api.exception;

public class OrderNotFoundException extends Exception {
    private int orderId;

    public static OrderNotFoundException createWith(int orderId) {
        return new OrderNotFoundException(orderId);
    }

    private OrderNotFoundException(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getMessage() {
        return "Order '" + orderId + "' not found";
    }
}