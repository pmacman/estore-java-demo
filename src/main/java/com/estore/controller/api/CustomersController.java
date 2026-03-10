package com.estore.controller.api;

import com.estore.dto.CustomerDto;
import com.estore.entity.Customer;
import com.estore.service.CustomerService;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomersController {
    private final CustomerService customerService;

    @Inject
    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CustomerDto> get(@PathVariable("id") int id) throws Exception {
        CustomerDto dto = customerService.get(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<CustomerDto> get(@RequestParam("userId") String userId) throws Exception {
        CustomerDto dto = customerService.getByUserId(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CustomerDto> create(@RequestBody Customer customer) throws Exception {
        CustomerDto dto = customerService.register(customer);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CustomerDto> update(@PathVariable int id, @RequestBody Customer customer) throws Exception {
        CustomerDto dto = customerService.updateProfile(customer, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RepresentationModel> delete(@PathVariable int id) throws Exception {
        CustomerDto dto = customerService.deactivate(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}