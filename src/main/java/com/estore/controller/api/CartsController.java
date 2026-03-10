package com.estore.controller.api;

import com.estore.dto.CartDto;
import com.estore.model.CartItem;
import com.estore.service.ShoppingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/carts")
public class CartsController {
    private final ShoppingService shoppingService;

    @Inject
    public CartsController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<CartDto> get(@PathVariable int customerId) throws Exception {
        CartDto dto = shoppingService.getCartByCustomer(customerId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CartDto> upsert(@RequestBody CartItem cartItem) throws Exception {
        CartDto dto = shoppingService.upsertItemToCart(cartItem);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<CartDto> deleteCart(@PathVariable int customerId) throws Exception {
        CartDto dto = shoppingService.deleteCart(customerId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/customer/{customerId}/product/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<CartDto> deleteItemFromCart(@PathVariable int customerId, @PathVariable int productId) throws Exception {
        CartDto dto = shoppingService.removeItemFromCart(customerId, productId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}