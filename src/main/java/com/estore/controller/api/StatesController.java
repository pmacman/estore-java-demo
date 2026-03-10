package com.estore.controller.api;

import com.estore.entity.State;
import com.estore.service.LocaleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/states")
public class StatesController {
    private final LocaleService service;

    @Inject
    public StatesController(LocaleService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Cacheable("states")
    public ResponseEntity<CollectionModel<State>> getByCountry(@RequestParam("country") String country) throws Exception {
        CollectionModel<State> resource =  service.getStatesByCountry(country);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}