package com.estore.controller.api;

import com.estore.dto.ProductDto;
import com.estore.entity.Product;
import com.estore.model.ProductSearch;
import com.estore.service.PartnerService;
import com.estore.service.ShoppingService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/products")
public class ProductsController {
    private final PartnerService partnerService;
    private final ShoppingService shoppingService;

    @Inject
    public ProductsController(PartnerService partnerService, ShoppingService shoppingService) {
        this.partnerService = partnerService;
        this.shoppingService = shoppingService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductDto> get(@PathVariable("id") int id) throws Exception {
        ProductDto dto = shoppingService.getProduct(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<ProductDto>> search(ProductSearch filter) throws Exception {
        CollectionModel<ProductDto> dto = shoppingService.searchProducts(filter);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<ProductDto> create(@RequestBody Product product) throws Exception {
        ProductDto dto = partnerService.addProduct(product);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<ProductDto> update(@PathVariable int id, @RequestBody Product product) throws Exception {
        ProductDto dto = partnerService.updateProduct(product, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RepresentationModel> delete(@PathVariable int id) throws Exception {
        ProductDto dto = partnerService.deactivateProduct(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}