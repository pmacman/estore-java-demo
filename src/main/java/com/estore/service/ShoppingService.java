package com.estore.service;

import com.estore.controller.api.OrdersController;
import com.estore.controller.api.ProductsController;
import com.estore.controller.api.exception.*;
import com.estore.dao.*;
import com.estore.dto.CartDto;
import com.estore.dto.OrderDto;
import com.estore.dto.ProductDto;
import com.estore.entity.*;
import com.estore.infrastructure.EstoreConstants;
import com.estore.model.CartItem;
import com.estore.model.CollectionModelEx;
import com.estore.model.OrderSearch;
import com.estore.model.ProductSearch;
import com.estore.utility.PaymentGateway;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShoppingService {
    private final CartDao cartDao;
    private final CartDetailDao cartDetailDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    @Inject
    public ShoppingService(CartDao cartDao, CartDetailDao cartDetailDao, CustomerDao customerDao, OrderDao orderDao,
                           ProductDao productDao) {
        this.cartDao = cartDao;
        this.cartDetailDao = cartDetailDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    public CollectionModelEx<ProductDto> searchProducts(ProductSearch filter) throws Exception {
        List<Product> products = productDao.search(filter);
        Long recordsTotal = productDao.count(filter);

        List<ProductDto> resources = new ArrayList<>();
        for (Product product : products) {
            resources.add(new ProductDto(product));
        }

        Link selfLink = linkTo(methodOn(ProductsController.class).search(filter)).withSelfRel();

        return new CollectionModelEx<>(resources, recordsTotal, recordsTotal, selfLink);
    }

    public ProductDto getProduct(int id) throws Exception {
        Product product = productDao.get(Product.class, id);

        if (product == null)
            throw ProductNotFoundException.createWith(id);

        return new ProductDto(product);
    }

    public CartDto getCart(int id) throws Exception {
        Cart cart = cartDao.get(Cart.class, id);

        if (cart == null)
            throw CartNotFoundException.createWith(id);

        return new CartDto(cart);
    }

    public CartDto getCartByCustomer(int customerId) throws Exception {
        Cart cart = cartDao.getByCustomer(customerId);

//        if (cart == null)
//            throw CartNotFoundException.createWith(customerId);

        return new CartDto(cart);
    }

    public CartDto upsertItemToCart(CartItem item) throws Exception {
        final int customerId = item.getCustomerId();
        final int productId = item.getProductId();
        int quantity = item.getQuantity();
        Customer customer = customerDao.get(Customer.class, customerId);
        Product product = productDao.get(Product.class, productId);
        CartDetail cartDetail = new CartDetail(product, quantity);
        Cart cart = cartDao.getByCustomer(customerId);

        if (quantity > product.getQuantity()) {
            throw ProductOutOfStockException.createWith(productId);
        }

        if (cart == null) {
            List<CartDetail> cartDetails = new ArrayList<>();
            cartDetails.add(cartDetail);
            cart = new Cart(customer, cartDetails);
            cart = cartDao.create(cart);
            return new CartDto(cart);
        } else {
            List<CartDetail> existingCartDetails = cart.getCartDetails(); //current cartdetail

            List<Integer> productIds = existingCartDetails.stream().map(x -> x.getProduct().getId()).collect(Collectors.toList());
            boolean isItemAlreadyInCart = productIds.contains(productId);

            if (isItemAlreadyInCart) {
                for (CartDetail detail : existingCartDetails) {  //compares productId of added product to each cartdetail
                    if (productId == detail.getProduct().getId()) {  //if it is already included in existingcartdetails
                        int availableQuant = detail.getProduct().getQuantity();  //get quantity of product available
                        if ((quantity += detail.getQuantity()) <= availableQuant) { //if quantity to add plus quantity already in cart is <= quantity available
                            detail.setQuantity(quantity); //set the new quantity
                        }
                    }
                }
            } else {
                existingCartDetails.add(cartDetail);
            }

            cart = cartDao.update(cart);

            return new CartDto(cart, productId);
        }
    }

    public CartDto deleteCart(int customerId) throws Exception {
        Cart cart = cartDao.getByCustomer(customerId);

        if (cart == null) {
            throw CartNotFoundException.createWith(customerId);
        }

        cartDao.delete(cart);

        return new CartDto(null);
    }

    public CartDto removeItemFromCart(int customerId, int productId) throws Exception {
        Cart cart = cartDao.getByCustomer(customerId);

        if (cart == null) {
            throw CartNotFoundException.createWith(customerId);
        }

        List<CartDetail> cartDetails = cart.getCartDetails();

        for (CartDetail cartDetail : cartDetails) {
            Product product = cartDetail.getProduct();
            if (product.getId() == productId) {
                cartDetailDao.delete(cartDetail);
                if (cartDetails.size() == 1) {
                    cart.setCartDetails(new ArrayList<>());
                    cartDao.delete(cart);
                    cart = null;
                }
                break;
            }
        }

        return new CartDto(cart, productId);
    }

    public OrderDto getOrder(int orderId) throws Exception {
        Order order = orderDao.get(Order.class, orderId);

        if (order == null)
            throw OrderNotFoundException.createWith(orderId);

        return new OrderDto(order);
    }

    public CollectionModel<OrderDto> searchOrders(OrderSearch filter) throws Exception {
        List<Order> orders = orderDao.search(filter);

        List<OrderDto> resources = new ArrayList<>();
        for (Order order : orders) {
            resources.add(new OrderDto(order));
        }

        Link selfLink = linkTo(methodOn(OrdersController.class).search(filter)).withSelfRel();

        return new CollectionModel<>(resources, selfLink);
    }

    public OrderDto placeOrder(Cart cart) throws Exception {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartDetail cartDetail : cart.getCartDetails()) {
            Product product = productDao.get(Product.class, cartDetail.getProduct().getId());

            if (product.getQuantity() < 1) {
                throw ProductOutOfStockException.createWith(product.getId());
            }

            OrderDetail orderDetail = new OrderDetail(product, cartDetail.getQuantity());
            orderDetails.add(orderDetail);
        }

        Order order = new Order(cart.getCustomer(), orderDetails, cart.getShippingMethod(), EstoreConstants.OrderStatus.NEW.name());

        boolean isPaymentSuccessful = PaymentGateway.processPayment(cart.getPayment());

        if (!isPaymentSuccessful) {
            throw PaymentUnsuccessfulException.createWith(cart.getCustomer().getId());
        }

        order = orderDao.create(order);
        order = orderDao.get(Order.class, order.getId());

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            subtractAvailableProductQuantity(orderDetail.getProduct().getId(), orderDetail.getQuantity());
        }

        return new OrderDto(order);
    }

    public OrderDto updateOrderStatus(int orderId, String status) throws Exception {
        Order order = orderDao.get(Order.class, orderId);

        if (order == null)
            throw OrderNotFoundException.createWith(orderId);

        order.setStatus(status);
        order = orderDao.update(order);

        return new OrderDto(order);
    }

    public OrderDto cancelOrder(int orderId) throws Exception {
        Order order = orderDao.get(Order.class, orderId);

        if (order == null) {
            throw OrderNotFoundException.createWith(orderId);
        }

        order.setStatus(EstoreConstants.OrderStatus.CANCELLED.name());
        order = orderDao.update(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            addAvailableProductQuantity(orderDetail.getProduct().getId(), orderDetail.getQuantity());
        }

        return new OrderDto(order);
    }

    private void subtractAvailableProductQuantity(int productId, int quantityPurchased) throws Exception {
        Product product = productDao.get(Product.class, productId);

        if (product == null) {
            throw ProductNotFoundException.createWith(productId);
        }

        int newQuantity = product.getQuantity() - quantityPurchased;

        product.setQuantity(newQuantity);
        productDao.update(product);
    }

    private void addAvailableProductQuantity(int productId, int quantityReturned) throws Exception {
        Product product = productDao.get(Product.class, productId);

        if (product == null) {
            throw ProductNotFoundException.createWith(productId);
        }

        int newQuantity = product.getQuantity() + quantityReturned;

        product.setQuantity(newQuantity);
        productDao.update(product);
    }
}