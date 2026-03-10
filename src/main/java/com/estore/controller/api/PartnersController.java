package com.estore.controller.api;

import com.estore.dto.PartnerDto;
import com.estore.entity.Partner;
import com.estore.service.PartnerService;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/partners")
public class PartnersController {
    private final PartnerService service;

    @Inject
    public PartnersController(PartnerService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PartnerDto> get(@PathVariable("id") int id) throws Exception {
        PartnerDto dto = service.getPartner(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PartnerDto> get(@RequestParam("userId") String userId) throws Exception {
        PartnerDto dto = service.getPartnerByContactUserId(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PartnerDto> create(@RequestBody Partner partner) throws Exception {
        PartnerDto dto = service.registerPartner(partner);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PartnerDto> update(@PathVariable int id, @RequestBody Partner partner) throws Exception {
        PartnerDto dto = service.updateProfile(partner, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RepresentationModel> delete(@PathVariable int id) throws Exception {
        PartnerDto dto = service.deactivatePartner(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}