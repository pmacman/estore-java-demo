package com.estore.dto;

import com.estore.controller.api.ReviewsController;
import com.estore.entity.Review;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ReviewDto extends RepresentationModel<ReviewDto> {
    private final Review review;

    public ReviewDto() throws Exception {
        this(new Review());
    }

    public ReviewDto(Review review) throws Exception {
        this.review = review;

        if (review == null) {
            add(linkTo(methodOn(ReviewsController.class).create(new Review())).withRel("create"));
        } else {
            final int id = review.getId();
            final int customerId = review.getCustomer().getId();
            final int productId = review.getProduct().getId();

            List<Link> links = new ArrayList<>();
            links.add(linkTo(methodOn(ReviewsController.class).get(id)).withRel("get"));
            links.add(linkTo(methodOn(ReviewsController.class).create(new Review())).withRel("create"));
            links.add(linkTo(methodOn(ReviewsController.class).update(id, new Review())).withRel("update"));
            links.add(linkTo(methodOn(ReviewsController.class).delete(id)).withRel("delete"));
            links.add(linkTo(methodOn(ReviewsController.class).getReviewsByCustomer(customerId)).withRel("getByCustomer"));
            links.add(linkTo(methodOn(ReviewsController.class).getReviewsByProduct(productId)).withRel("getByProduct"));
            add(links);
        }
    }

    public Review getReview() {
        return review;
    }
}