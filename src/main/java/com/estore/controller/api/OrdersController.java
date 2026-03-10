package com.estore.controller.api;

import com.estore.dto.OrderDto;
import com.estore.entity.Cart;
import com.estore.model.OrderSearch;
import com.estore.service.ShoppingService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/orders")
public class OrdersController {
    private final ShoppingService shoppingService;

    @Inject
    public OrdersController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderDto> get(@PathVariable("id") int id) throws Exception {
        OrderDto dto = shoppingService.getOrder(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<OrderDto>> search(OrderSearch filter) throws Exception {
        CollectionModel<OrderDto> dto = shoppingService.searchOrders(filter);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<OrderDto> create(@RequestBody Cart cart) throws Exception {
        OrderDto dto = shoppingService.placeOrder(cart);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<OrderDto> update(@PathVariable int id, @RequestParam String status) throws Exception {
        OrderDto dto = shoppingService.updateOrderStatus(id, status);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<OrderDto> delete(@PathVariable int id) throws Exception {
        OrderDto dto = shoppingService.cancelOrder(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}