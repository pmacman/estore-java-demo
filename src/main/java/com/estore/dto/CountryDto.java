package com.estore.dto;

import com.estore.controller.api.CountriesController;
import com.estore.controller.api.StatesController;
import com.estore.entity.Country;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CountryDto extends RepresentationModel<CountryDto> {
    private final Country country;

    public CountryDto() throws Exception {
        this(new Country());
    }

    public CountryDto(Country country) throws Exception {
        this.country = country;
        final String iso2 = country.getIso2();
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CountriesController.class).get(iso2)).withSelfRel());
        links.add(linkTo(methodOn(StatesController.class).getByCountry(iso2)).withRel("states"));
        add(links);
    }

    public Country getCountry() {
        return country;
    }
}