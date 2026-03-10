package com.estore.dto;

import com.estore.controller.api.OrdersController;
import com.estore.entity.Cart;
import com.estore.entity.Order;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OrderDto extends RepresentationModel<OrderDto> {
    private final Order order;

    public OrderDto() throws Exception {
        this(new Order());
    }

    public OrderDto(Order order) throws Exception {
        this.order = order;
        final int id = order.getId();

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(OrdersController.class).get(id)).withRel("get"));
        links.add(linkTo(methodOn(OrdersController.class).create(new Cart())).withRel("create"));
        links.add(linkTo(methodOn(OrdersController.class).update(id, "STATUS")).withRel("update"));
        links.add(linkTo(methodOn(OrdersController.class).delete(id)).withRel("delete"));
        links.add(linkTo(methodOn(OrdersController.class).search(null)).withRel("search"));
        add(links);
    }

    public Order getOrder() {
        return order;
    }
}