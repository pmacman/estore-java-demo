package com.estore.entity;

import com.estore.infrastructure.EstoreConstants;
import com.estore.model.Payment;
import com.estore.utility.PriceCalculator;
import com.estore.utility.ShippingCalculator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private List<CartDetail> cartDetails = new ArrayList<>();

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Transient
    private Payment payment;

    public Cart() {
    }

    public Cart(Customer customer, List<CartDetail> cartDetails) {
        this(customer, cartDetails, EstoreConstants.ShippingMethod.STANDARD.toString());
    }

    public Cart(Customer customer, List<CartDetail> cartDetails, String shippingMethod) {
        if (shippingMethod == null || shippingMethod.isEmpty()) {
            shippingMethod = EstoreConstants.ShippingMethod.STANDARD.toString();
        }

        this.customer = customer;
        this.cartDetails = cartDetails;
        this.shippingMethod = shippingMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Transient
    @JsonProperty("baseTotal")
    public double calculateBaseTotal() {
        double sum = 0;

        for (CartDetail cartDetail : cartDetails) {
            sum += cartDetail.getProduct().getPrice() * cartDetail.getQuantity();
        }

        return sum;
    }

    @Transient
    @JsonProperty("tax")
    public double calculateTax() {
        return PriceCalculator.calculateTax(calculateBaseTotal());
    }

    @Transient
    @JsonProperty("shippingCost")
    public double calculateShippingCost() {
        return ShippingCalculator.calculateShippingCost(getShippingMethod());
    }

    @Transient
    @JsonProperty("total")
    public double calculateTotal() {
        return calculateBaseTotal() + calculateTax() + calculateShippingCost();
    }
}