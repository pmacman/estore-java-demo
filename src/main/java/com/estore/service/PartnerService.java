package com.estore.service;

import com.estore.controller.api.exception.PartnerNotFoundException;
import com.estore.controller.api.exception.ProductNotFoundException;
import com.estore.controller.api.exception.UserNotFoundException;
import com.estore.dao.PartnerDao;
import com.estore.dao.ProductDao;
import com.estore.dto.PartnerDto;
import com.estore.dto.ProductDto;
import com.estore.entity.Partner;
import com.estore.entity.Product;
import com.estore.infrastructure.EstoreConstants;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class PartnerService {
    private final PartnerDao partnerDao;
    private final ProductDao productDao;

    @Inject
    public PartnerService(PartnerDao partnerDao, ProductDao productDao) {
        this.partnerDao = partnerDao;
        this.productDao = productDao;
    }

    public PartnerDto getPartner(int id) throws Exception {
        Partner partner = partnerDao.get(Partner.class, id);

        if (partner == null)
            throw PartnerNotFoundException.createWith(id);

        return new PartnerDto(partner);
    }

    public PartnerDto getPartnerByContactUserId(String userId) throws Exception {
        Partner partner = partnerDao.getByContactUserId(userId);

        if (partner == null)
            throw UserNotFoundException.createWith(userId);

        return new PartnerDto(partner);
    }

    public PartnerDto registerPartner(Partner partner) throws Exception {
        partner = partnerDao.create(partner);
        return new PartnerDto(partner);
    }

    public PartnerDto updateProfile(Partner partner, int partnerId) throws Exception {
        Partner existingPartner = partnerDao.get(Partner.class, partnerId);

        if (existingPartner == null)
            throw PartnerNotFoundException.createWith(partnerId);

        partner.setId(partnerId);
        partner = partnerDao.update(partner);

        return new PartnerDto(partner);
    }

    public PartnerDto deactivatePartner(int partnerId) throws Exception {
        Partner partner = partnerDao.get(Partner.class, partnerId);

        if (partner == null)
            throw PartnerNotFoundException.createWith(partnerId);

        partner = partnerDao.deactivate(partner);

        return new PartnerDto(partner);
    }

    public ProductDto addProduct(Product product) throws Exception {
        product = productDao.create(product);
        return new ProductDto(product);
    }

    public ProductDto updateProduct(Product product, int productId) throws Exception {
        Product existingProduct = productDao.get(Product.class, productId);

        if (existingProduct == null)
            throw ProductNotFoundException.createWith(productId);

        product.setId(productId);
        product = productDao.update(product);

        return new ProductDto(product);
    }

    public ProductDto deactivateProduct(int productId) throws Exception {
        Product product = productDao.get(Product.class, productId);

        if (product == null)
            throw ProductNotFoundException.createWith(productId);

        product.setStatus(EstoreConstants.ProductStatus.INACTIVE.name());
        product = productDao.update(product);

        return new ProductDto(product);
    }
}