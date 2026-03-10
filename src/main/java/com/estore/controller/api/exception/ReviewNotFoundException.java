package com.estore.controller.api.exception;

public class ReviewNotFoundException extends Exception {
    private int reviewId;

    public static ReviewNotFoundException createWith(int reviewId) {
        return new ReviewNotFoundException(reviewId);
    }

    private ReviewNotFoundException(int reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String getMessage() {
        return "Review '" + reviewId + "' not found";
    }
}