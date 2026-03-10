package com.estore.entity;

import javax.persistence.*;

@Entity
@Table(name = "country")
public class Country implements Comparable<Country> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iso2")
    private String iso2;

    @Column(name = "name")
    private String name;

    public Country()
    {
    }

    public Country(String iso2, String name) {
        this.iso2 = iso2;
        this.name = name;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Country c) {
        if (getName() == null || c.getName() == null) {
            return 0;
        }
        return getName().compareTo(c.getName());
    }
}