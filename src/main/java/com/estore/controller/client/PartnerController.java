package com.estore.controller.client;

import com.estore.dto.CountryDto;
import com.estore.dto.OrderDto;
import com.estore.dto.PartnerDto;
import com.estore.dto.ProductDto;
import com.estore.entity.Order;
import com.estore.entity.Partner;
import com.estore.entity.PartnerContact;
import com.estore.entity.Product;
import com.estore.infrastructure.Client;
import com.estore.infrastructure.TokenAuthentication;
import com.estore.model.CollectionModelEx;
import com.estore.model.ProductSearch;
import com.estore.model.ProductSearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/partner")
public class PartnerController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest request, final Authentication authentication,
                        @CookieValue(value = "UserId", defaultValue = "/") String userId,
                        final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        String uri = Client.getApiUri(request, "partners?userId=" + userId);
        WebClient client = Client.getWebClient();

        ResponseEntity<PartnerDto> result =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();

        PartnerDto partnerDto;

        if (result == null || result.getStatusCode() == HttpStatus.NOT_FOUND) {
            partnerDto = new PartnerDto();
        } else {
            partnerDto = result.getBody();
        }

        model.addAttribute("partnerDto", partnerDto);

        LocaleController localeController = new LocaleController();
        CollectionModel<CountryDto> countries = localeController.getCountries(request, authentication);
        model.addAttribute("countryDtos", countries.getContent());

        return "partner/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String indexPost(HttpServletRequest request, final Authentication authentication,
                            @CookieValue(value = "UserId", defaultValue = "/") String userId,
                            @ModelAttribute PartnerDto savePartner) throws Exception {

        String uri = Client.getApiUri(request, "partners");
        WebClient client = Client.getWebClient();

        // Get existing partner
        ResponseEntity<PartnerDto> existingPartner =
                client.get().uri(uri + "?userId=" + userId)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();

        if (existingPartner == null || existingPartner.getStatusCode() == HttpStatus.NOT_FOUND) {
            List<PartnerContact> partnerContacts = new ArrayList<>();
            partnerContacts.add(new PartnerContact(userId));
            savePartner.getPartner().setPartnerContacts(partnerContacts);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(savePartner.getPartner());

            ResponseEntity<PartnerDto> newPartner =
                client.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(savePartner.getPartner()))
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();
        } else {
            savePartner.getPartner().setId(existingPartner.getBody().getPartner().getId());
            savePartner.getPartner().setPartnerContacts(existingPartner.getBody().getPartner().getPartnerContacts());

            ResponseEntity<PartnerDto> newPartner =
                client.put().uri(uri + "/" + existingPartner.getBody().getPartner().getId()).contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(savePartner.getPartner()))
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();
        }

        return "redirect:partner";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(HttpServletRequest request, final Authentication authentication,
                        @CookieValue(value = "UserId", defaultValue = "/") String userId,
                        final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        model.addAttribute("productSearch", new ProductSearch());

        return "partner/search";
    }

    @RequestMapping(value = "/search-submit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductSearchResult> searchSubmit(HttpServletRequest request, final Authentication authentication,
                                                            @CookieValue(value = "UserId", defaultValue = "/") String userId,
                                                            final Model model,
                                                            @ModelAttribute ProductSearch filter) throws Exception {

        // Gets partner ID
        String uri = Client.getApiUri(request, "partners?userId=" + userId);
        WebClient client = Client.getWebClient();

        ResponseEntity<PartnerDto> partnerResult =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();

        int partnerId = partnerResult.getBody().getPartner().getId();

        uri = Client.getApiUri(request, "products/search?start=" + filter.getStart() + "&length=" + filter.getLength() + "&productName=" + filter.getProductName() + "&partnerId=" + partnerId);

        ParameterizedTypeReference<CollectionModelEx<ProductDto>> type = new ParameterizedTypeReference<CollectionModelEx<ProductDto>>() {};

        ResponseEntity<CollectionModelEx<ProductDto>> result =
                client.get().uri(uri)
                        .exchange().flatMap(response -> response.toEntity(type)).block();

        List<Product> products = new ArrayList<>();
        Collection<ProductDto> dtos = result.getBody().getContent();
        for (ProductDto dto : dtos) {
            products.add(dto.getProduct());
        }

        ProductSearchResult productSearchResult = new ProductSearchResult(result.getBody().getDraw(),
                result.getBody().getRecordsTotal(), result.getBody().getRecordsTotal(), products);

        model.addAttribute("product", new Product());

        return new ResponseEntity<>(productSearchResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String product(HttpServletRequest request, final Authentication authentication,
                          @CookieValue(value = "UserId", defaultValue = "/") String userId,
                          final Model model) throws Exception {

        model.addAttribute("product", new Product());

        return "partner/_productModal";
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String product(HttpServletRequest request, final Authentication authentication,
                          @CookieValue(value = "UserId", defaultValue = "/") String userId,
                          final Model model, @PathVariable("id") int id) throws Exception {

        String uri = Client.getApiUri(request, "products/" + id);
        WebClient client = Client.getWebClient();

        ResponseEntity<ProductDto> result =
                client.get().uri(uri)
                        .exchange().flatMap(response -> response.toEntity(ProductDto.class)).block();

        Product product;

        if (result == null || result.getStatusCode() == HttpStatus.NOT_FOUND) {
            product = new Product();
        } else {
            product = result.getBody().getProduct();
        }

        model.addAttribute("product", product);

        return "partner/_productModal";
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity productPost(HttpServletRequest request, final Authentication authentication,
                            @CookieValue(value = "UserId", defaultValue = "/") String userId,
                            @ModelAttribute Product product) throws Exception {

        // Gets partner ID
        String uri = Client.getApiUri(request, "partners?userId=" + userId);
        WebClient client = Client.getWebClient();

        ResponseEntity<PartnerDto> partnerResult =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();

        int partnerId = partnerResult.getBody().getPartner().getId();
        Partner partner = new Partner();
        partner.setId(partnerId);
        product.setPartner(partner);

        if (product.getId() > 0) {
            uri = Client.getApiUri(request, "products/" + product.getId());

            ResponseEntity<ProductDto> newProduct =
                    client.put().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(product))
                            .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                            .exchange().flatMap(response -> response.toEntity(ProductDto.class)).block();
        } else {
            uri = Client.getApiUri(request, "products");

            ResponseEntity<ProductDto> newProduct =
                    client.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(product))
                            .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                            .exchange().flatMap(response -> response.toEntity(ProductDto.class)).block();
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/order-history", method = RequestMethod.GET)
    public String orderHistory(HttpServletRequest request, final Authentication authentication,
                               @CookieValue(value = "UserId", defaultValue = "/") String userId,
                               final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        // Gets partner
        String uri = Client.getApiUri(request, "partners?userId=" + userId);
        WebClient client = Client.getWebClient();

        ResponseEntity<PartnerDto> partnerResult =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(PartnerDto.class)).block();

        // Gets orders
        uri = Client.getApiUri(request, "orders/search?partnerId=" + partnerResult.getBody().getPartner().getId());
        ParameterizedTypeReference<CollectionModel<OrderDto>> type = new ParameterizedTypeReference<CollectionModel<OrderDto>>() {};
        ResponseEntity<CollectionModel<OrderDto>> result =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(type)).block();

        List<Order> orders = new ArrayList<>();
        for (OrderDto dto : result.getBody().getContent()) {
            orders.add(dto.getOrder());
        }

        model.addAttribute("orders", orders);

        return "partner/orders";
    }

}