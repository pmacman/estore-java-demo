package com.estore.dto;

import com.estore.controller.api.CustomersController;
import com.estore.entity.Customer;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CustomerDto extends RepresentationModel<CustomerDto> {
    private final Customer customer;

    public CustomerDto() throws Exception {
        this(new Customer());
    }

    public CustomerDto(Customer customer) throws Exception {
        this.customer = customer;
        final int id = customer.getId();

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CustomersController.class).get(id)).withRel("get"));
        links.add(linkTo(methodOn(CustomersController.class).create(new Customer())).withRel("create"));
        links.add(linkTo(methodOn(CustomersController.class).update(id, new Customer())).withRel("update"));
        links.add(linkTo(methodOn(CustomersController.class).delete(id)).withRel("delete"));
        add(links);
    }

    public Customer getCustomer() {
        return customer;
    }
}