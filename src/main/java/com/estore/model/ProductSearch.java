package com.estore.model;

import org.springframework.lang.Nullable;

public class ProductSearch extends SearchBase {
    /***
     * Search parameters
     */
    private String productName;
    private Integer partnerId;
    private String status;

    public ProductSearch() {
        super(0, 0, 0);
    }

    public ProductSearch(@Nullable Integer draw, @Nullable Integer start, @Nullable Integer length,
                         @Nullable String productName, @Nullable Integer partnerId, @Nullable String status) {
        super(draw, start, length);
        this.productName = productName;
        this.partnerId = partnerId;
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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