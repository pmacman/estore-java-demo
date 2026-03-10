package com.estore.dto;

import com.estore.controller.api.ProductsController;
import com.estore.entity.Product;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ProductDto extends RepresentationModel<ProductDto> {
    private final Product product;

    public ProductDto() throws Exception {
        this(new Product());
    }

    public ProductDto(Product product) throws Exception {
        this.product = product;
        final int id = product.getId();

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(ProductsController.class).get(id)).withRel("get"));
        links.add(linkTo(methodOn(ProductsController.class).create(new Product())).withRel("create"));
        links.add(linkTo(methodOn(ProductsController.class).update(id, new Product())).withRel("update"));
        links.add(linkTo(methodOn(ProductsController.class).delete(id)).withRel("delete"));
        links.add(linkTo(methodOn(ProductsController.class).search(null)).withRel("search"));
        add(links);
    }

    public Product getProduct() {
        return product;
    }
}