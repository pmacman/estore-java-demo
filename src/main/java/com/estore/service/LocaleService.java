package com.estore.service;

import com.estore.controller.api.CountriesController;
import com.estore.controller.api.StatesController;
import com.estore.controller.api.exception.CountryNotFoundException;
import com.estore.dao.CountryDao;
import com.estore.dao.StateDao;
import com.estore.dto.CountryDto;
import com.estore.entity.Country;
import com.estore.entity.State;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LocaleService {
    private CountryDao countryDao;
    private StateDao stateDao;

    @Inject
    public LocaleService(CountryDao countryDao, StateDao stateDao) {
        this.countryDao = countryDao;
        this.stateDao = stateDao;
    }

    public CollectionModel<CountryDto> getCountries() throws Exception {
        List<Country> countries = countryDao.getAll(Country.class);
        Collections.sort(countries);

        List<CountryDto> resources = new ArrayList<>();
        for (Country country : countries) {
            resources.add(new CountryDto(country));
        }

        Link selfLink = linkTo(methodOn(CountriesController.class).getAll()).withSelfRel();

        return new CollectionModel<>(resources, selfLink);
    }

    public CountryDto getCountryByIso2Code(String iso2) throws Exception {
        Country country = countryDao.getByIso2Code(iso2);

        if (country == null)
            throw CountryNotFoundException.createWith(iso2);

        return new CountryDto(country);
    }

    public CollectionModel<State> getStatesByCountry(String countryIso2) throws Exception {
        List<State> states = stateDao.getByCountry(countryIso2);
        Collections.sort(states);

        Link selfLink = linkTo(methodOn(StatesController.class).getByCountry(countryIso2)).withSelfRel();

        return new CollectionModel<>(states, selfLink);
    }
}