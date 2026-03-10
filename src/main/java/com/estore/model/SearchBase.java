package com.estore.model;

import org.springframework.lang.Nullable;

public abstract class SearchBase  {
    /***
     * Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side processing requests
     * are drawn in sequence by DataTables (Ajax requests are asynchronous and thus can return out of sequence).
     */
    private Integer draw;

    /***
     * Paging first record indicator.
     * This is the start point in the current data set (0 index based - i.e. 0 is the first record).
     */
    private Integer start;

    /***
     * Number of records that the table can display in the current draw.
     * It is expected that the number of records returned will be equal to this number,
     * unless the server has fewer records to return. Note that this can be -1 to indicate
     * that all records should be returned (although that negates any benefits of server-side processing!)
     */
    private Integer length;

    public SearchBase(@Nullable Integer draw, @Nullable Integer start, @Nullable Integer length) {
        if (draw == null || draw == 0) {
            this.draw = 1;
        } else {
            this.draw = draw;
        }

        if (start == null || start == 0) {
            this.start = 0;
        } else {
            this.start = start;
        }

        if (length==null || length == 0) {
            this.length = 10;
        } else {
            this.length = length;
        }
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}