package com.estore.entity;

import com.estore.utility.ShippingCalculator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_cost")
    private double shippingCost;

    @Column(name = "status")
    private String status;

    public Order() {
    }

    public Order(Customer customer, List<OrderDetail> orderDetails, String shippingMethod, String status) {
        this.customer = customer;
        this.orderDetails = orderDetails;
        this.shippingMethod = shippingMethod;
        this.shippingCost = calculateShippingCost();
        this.status = status;
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    @JsonProperty("baseTotal")
    public double calculateBaseTotal() {
        return orderDetails.stream().mapToDouble(OrderDetail::getCost).sum();
    }

    @Transient
    @JsonProperty("tax")
    public double calculateTax() {
        return orderDetails.stream().mapToDouble(OrderDetail::getTax).sum();
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