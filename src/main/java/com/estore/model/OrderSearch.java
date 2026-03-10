package com.estore.model;

import org.springframework.lang.Nullable;

public class OrderSearch extends SearchBase {
    private Integer customerId;
    private Integer partnerId;
    private String status;

    public OrderSearch(@Nullable Integer draw, @Nullable Integer start, @Nullable Integer length,
                       @Nullable Integer customerId, @Nullable Integer partnerId, String status) {
        super(draw, start, length);
        this.customerId = customerId;
        this.partnerId = partnerId;
        this.status = status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}