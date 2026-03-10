package com.estore.controller.api.exception;

public class UserNotFoundException extends Exception {
    private String userId;

    public static UserNotFoundException createWith(String userId) {
        return new UserNotFoundException(userId);
    }

    private UserNotFoundException(String userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "User '" + userId + "' not found";
    }
}