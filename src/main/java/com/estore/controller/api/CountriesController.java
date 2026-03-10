package com.estore.controller.api;

import com.estore.dto.CountryDto;
import com.estore.entity.Country;
import com.estore.service.LocaleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/countries")
public class CountriesController {
    private final LocaleService service;

    @Inject
    public CountriesController(LocaleService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Cacheable("countries")
    public ResponseEntity<CollectionModel<CountryDto>> getAll() throws Exception {
        CollectionModel<CountryDto> dto = service.getCountries();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{iso2}", method = RequestMethod.GET)
    @Cacheable("countries")
    public ResponseEntity<CountryDto> get(@PathVariable("iso2") String iso2) throws Exception {
        CountryDto dto = service.getCountryByIso2Code(iso2);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}