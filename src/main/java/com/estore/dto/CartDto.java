package com.estore.dto;

import com.estore.controller.api.CartsController;
import com.estore.entity.Cart;
import com.estore.model.CartItem;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CartDto extends RepresentationModel<CartDto> {
    private final Cart cart;

    public CartDto() throws Exception {
        this(new Cart());
    }

    public CartDto(Cart cart) throws Exception {
        this(cart, 0);
    }

    public CartDto(Cart cart, int productId) throws Exception {
        this.cart = cart;

        if (cart == null || cart.getId() == 0) {
            add(linkTo(methodOn(CartsController.class).upsert(new CartItem())).withRel("upsert"));
        } else {
            final int cartId = cart.getId();
            final int customerId = cart.getCustomer().getId();

            List<Link> links = new ArrayList<>();
            links.add(linkTo(methodOn(CartsController.class).get(cartId)).withRel("get"));
            links.add(linkTo(methodOn(CartsController.class).upsert(new CartItem())).withRel("upsert"));
            links.add(linkTo(methodOn(CartsController.class).deleteCart(cartId)).withRel("delete"));

            if (productId > 0) {
                links.add(linkTo(methodOn(CartsController.class).deleteItemFromCart(customerId, productId)).withRel("deleteItem"));
            }

            add(links);
        }
    }

    public Cart getCart() {
        return cart;
    }
}