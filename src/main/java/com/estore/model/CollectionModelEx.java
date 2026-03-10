package com.estore.model;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

public class CollectionModelEx<T> extends CollectionModel<T> {
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

    public CollectionModelEx() {
    }

    public CollectionModelEx(Long recordsTotal, Long recordsFiltered) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }

    public CollectionModelEx(Iterable<T> content, Long recordsTotal, Long recordsFiltered, Link... links) {
        super(content, links);
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }

    public CollectionModelEx(Iterable<T> content, Iterable<Link> links, Long recordsTotal, Long recordsFiltered) {
        super(content, links);
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
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
}