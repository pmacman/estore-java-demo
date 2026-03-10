package com.estore.controller.api;

import com.estore.dto.ReviewDto;
import com.estore.entity.Review;
import com.estore.service.CustomerService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/reviews")
public class ReviewsController {
    private final CustomerService customerService;

    @Inject
    public ReviewsController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ReviewDto> get(@PathVariable("id") int id) throws Exception {
        ReviewDto dto = customerService.getReview(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<ReviewDto>> getReviewsByCustomer(@PathVariable("customerId") int customerId) throws Exception {
        CollectionModel<ReviewDto> dto = customerService.getReviewsByCustomer(customerId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<ReviewDto>> getReviewsByProduct(@PathVariable("productId") int productId) throws Exception {
        CollectionModel<ReviewDto> dto = customerService.getReviewsByProduct(productId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<ReviewDto> create(@RequestBody Review review) throws Exception {
        ReviewDto dto = customerService.addReview(review);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<ReviewDto> update(@PathVariable int id, @RequestBody Review review) throws Exception {
        ReviewDto dto = customerService.updateReview(review, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ReviewDto> delete(@PathVariable int id) throws Exception {
        ReviewDto dto = customerService.deleteReview(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}