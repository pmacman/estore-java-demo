package com.estore.entity;

import javax.persistence.*;

@Entity
@Table(name = "state")
public class State implements Comparable<State>  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "name")
    private String name;

    @Column(name = "country_iso2")
    private String countryIso2;

    public State()
    {
    }

    public State(int id, String abbreviation, String name, String countryIso2) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
        this.countryIso2 = countryIso2;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCountryIso2() {
        return countryIso2;
    }

    public void setCountryIso2(String countryIso2) {
        this.countryIso2 = countryIso2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(State s) {
        if (getName() == null || s.getName() == null) {
            return 0;
        }
        return getName().compareTo(s.getName());
    }
}