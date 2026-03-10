package com.estore.service;

import com.estore.controller.api.ReviewsController;
import com.estore.controller.api.exception.CustomerNotFoundException;
import com.estore.controller.api.exception.ReviewNotFoundException;
import com.estore.controller.api.exception.UserNotFoundException;
import com.estore.dao.CustomerDao;
import com.estore.dao.ReviewDao;
import com.estore.dto.CustomerDto;
import com.estore.dto.ReviewDto;
import com.estore.entity.Customer;
import com.estore.entity.Review;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerService {
    private final CustomerDao customerDao;
    private final ReviewDao reviewDao;

    @Inject
    public CustomerService(CustomerDao customerDao, ReviewDao reviewDao) {
        this.customerDao = customerDao;
        this.reviewDao = reviewDao;
    }

    public CustomerDto get(int id) throws Exception {
        Customer customer = customerDao.get(Customer.class, id);

        if (customer == null)
            throw CustomerNotFoundException.createWith(id);

        return new CustomerDto(customer);
    }

    public CustomerDto getByUserId(String userId) throws Exception {
        Customer customer = customerDao.getByUserId(userId);

        if (customer == null)
            throw UserNotFoundException.createWith(userId);

        return new CustomerDto(customer);
    }

    public CustomerDto register(Customer customer) throws Exception {
        customer = customerDao.create(customer);
        return new CustomerDto(customer);
    }

    public CustomerDto updateProfile(Customer customer, int customerId) throws Exception {
        Customer existingCustomer = customerDao.get(Customer.class, customerId);

        if (existingCustomer == null)
            throw CustomerNotFoundException.createWith(customerId);

        customer.setId(customerId);
        customer = customerDao.update(customer);

        return new CustomerDto(customer);
    }

    public CustomerDto deactivate(int customerId) throws Exception {
        Customer customer = customerDao.get(Customer.class, customerId);

        if (customer == null)
            throw CustomerNotFoundException.createWith(customerId);

        customer = customerDao.deactivate(customer);

        return new CustomerDto(customer);
    }

    public ReviewDto getReview(int id) throws Exception {
        Review review = reviewDao.get(Review.class, id);

        if (review == null)
            throw ReviewNotFoundException.createWith(id);

        return new ReviewDto(review);
    }

    public CollectionModel<ReviewDto> getReviewsByCustomer(int customerId) throws Exception {
        List<Review> reviews = reviewDao.getReviewsByCustomer(customerId);

        List<ReviewDto> resources = new ArrayList<>();
        for (Review review : reviews) {
            resources.add(new ReviewDto(review));
        }

        Link selfLink = linkTo(methodOn(ReviewsController.class).getReviewsByCustomer(customerId)).withSelfRel();

        return new CollectionModel<>(resources, selfLink);
    }

    public CollectionModel<ReviewDto> getReviewsByProduct(int productId) throws Exception {
        List<Review> reviews = reviewDao.getReviewsByProduct(productId);

        List<ReviewDto> resources = new ArrayList<>();
        for (Review review : reviews) {
            resources.add(new ReviewDto(review));
        }

        Link selfLink = linkTo(methodOn(ReviewsController.class).getReviewsByProduct(productId)).withSelfRel();

        return new CollectionModel<>(resources, selfLink);
    }

    public ReviewDto addReview(Review review) throws Exception {
        review = reviewDao.create(review);
        return new ReviewDto(review);
    }

    public ReviewDto updateReview(Review review, int reviewId) throws Exception {
        Review existingReview = reviewDao.get(Review.class, reviewId);

        if (existingReview == null)
            throw ReviewNotFoundException.createWith(reviewId);

        review.setId(reviewId);
        review = reviewDao.update(review);

        return new ReviewDto(review);
    }

    public ReviewDto deleteReview(int reviewId) throws Exception {
        Review review = reviewDao.get(Review.class, reviewId);

        if (review == null)
            throw ReviewNotFoundException.createWith(reviewId);

        reviewDao.delete(review);

        return new ReviewDto(null);
    }
}