package com.estore.entity;

import javax.persistence.*;

@Entity
@Table(name = "partner_contact")
public class PartnerContact {
    @Id
    @Column(name = "user_id")
    private String userId;

    public PartnerContact()
    {
    }

    public PartnerContact(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}