package com.estore.dto;

import com.estore.controller.api.CountriesController;
import com.estore.controller.api.PartnersController;
import com.estore.entity.Partner;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PartnerDto extends RepresentationModel<PartnerDto> {
    private final Partner partner;

    public PartnerDto() throws Exception {
        this(new Partner());
    }

    public PartnerDto(Partner partner) throws Exception {
        this.partner = partner;
        final int id = partner.getId();

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(PartnersController.class).get(id)).withRel("get"));
        links.add(linkTo(methodOn(PartnersController.class).create(new Partner())).withRel("post"));
        links.add(linkTo(methodOn(PartnersController.class).update(id, new Partner())).withRel("put"));
        links.add(linkTo(methodOn(PartnersController.class).delete(id)).withRel("delete"));
        links.add(linkTo(methodOn(CountriesController.class).getAll()).withRel("countries"));
        add(links);
    }

    public Partner getPartner() {
        return partner;
    }
}