package com.estore.model;

import com.estore.entity.Product;

import java.util.List;

public class ProductSearchResult {
    /**
     * 	The draw counter that this object is a response to - from the draw parameter sent as part of the data request.
     */
    private int draw;

    /**
     * Total records, before filtering (i.e. the total number of records in the database)
     */
    private Long recordsTotal;

    /**
     * Total records, after filtering (i.e. the total number of records after filtering has been applied -
     * not just the number of records being returned for this page of data).
     */
    private Long recordsFiltered;

    private List<Product> data;

    public ProductSearchResult(int draw, Long recordsTotal, Long recordsFiltered, List<Product> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}