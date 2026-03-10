package com.estore.controller.client;

import com.estore.dto.CartDto;
import com.estore.dto.CountryDto;
import com.estore.dto.CustomerDto;
import com.estore.dto.OrderDto;
import com.estore.entity.Cart;
import com.estore.entity.Customer;
import com.estore.entity.Order;
import com.estore.infrastructure.Client;
import com.estore.infrastructure.TokenAuthentication;
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
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest request, final Authentication authentication,
                        @CookieValue(value = "UserId", defaultValue = "/") String userId,
                        final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        String uri = Client.getApiUri(request, "customers?userId=" + userId);
        WebClient client = Client.getWebClient();

        ResponseEntity<CustomerDto> result =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        CustomerDto customerDto;

        if (result == null || result.getStatusCode() == HttpStatus.NOT_FOUND) {
            customerDto = new CustomerDto();
        } else {
            customerDto = result.getBody();
        }

        model.addAttribute("customerDto", customerDto);

        LocaleController localeController = new LocaleController();
        CollectionModel<CountryDto> countries = localeController.getCountries(request, authentication);
        model.addAttribute("countryDtos", countries.getContent());

        return "customer/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String indexPost(HttpServletRequest request, final Authentication authentication,
                            @CookieValue(value = "UserId", defaultValue = "/") String userId,
                            @ModelAttribute CustomerDto saveCustomer) throws Exception {

        String uri = Client.getApiUri(request, "customers");
        WebClient client = Client.getWebClient();

        saveCustomer.getCustomer().setUserId(userId);

        // Get existing customer
        ResponseEntity<CustomerDto> existingCustomer =
                client.get().uri(uri + "?userId=" + userId)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        if (existingCustomer == null || existingCustomer.getStatusCode() == HttpStatus.NOT_FOUND) {
            ObjectMapper mapper = new ObjectMapper();
            saveCustomer.getCustomer().setActive(true);
            String json = mapper.writeValueAsString(saveCustomer.getCustomer());

            ResponseEntity<CustomerDto> newCustomer =
                    client.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(saveCustomer.getCustomer()))
                            .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                            .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();
        } else {
            saveCustomer.getCustomer().setId(existingCustomer.getBody().getCustomer().getId());
            saveCustomer.getCustomer().setUserId(userId);

            ResponseEntity<CustomerDto> newCustomer =
                    client.put().uri(uri + "/" + existingCustomer.getBody().getCustomer().getId()).contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(saveCustomer.getCustomer()))
                            .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                            .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();
        }

        return "redirect:customer";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String cart(HttpServletRequest request, final Authentication authentication,
                        @CookieValue(value = "UserId", defaultValue = "/") String userId,
                        final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        WebClient client = Client.getWebClient();

        // Gets customer
        String uri = Client.getApiUri(request, "customers?userId=" + userId);
        ResponseEntity<CustomerDto> customer =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        // Gets cart
        uri = Client.getApiUri(request, "carts/customer/" + customer.getBody().getCustomer().getId());
        ResponseEntity<CartDto> cartResponse =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CartDto.class)).block();

        Cart cart;

        if (cartResponse == null || cartResponse.getBody() == null || cartResponse.getBody().getCart() == null) {
            cart = new Cart();
        } else {
            cart = cartResponse.getBody().getCart();
        }

        model.addAttribute("cart", cart);

        return "customer/cart";
    }

    @RequestMapping(value = "/cart-remove-item", method = RequestMethod.DELETE)
    public ResponseEntity cartRemoveItem(HttpServletRequest request, final Authentication authentication,
                       @CookieValue(value = "UserId", defaultValue = "/") String userId,
                       @RequestParam("productId") int productId) throws Exception {

        WebClient client = Client.getWebClient();

        // Gets customer
        String uri = Client.getApiUri(request, "customers?userId=" + userId);
        ResponseEntity<CustomerDto> customer =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        // Deletes product from cart
        uri = Client.getApiUri(request, "carts/customer/" + customer.getBody().getCustomer().getId() + "/product/" + productId);

        client.delete().uri(uri)
                .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String order(HttpServletRequest request, final Authentication authentication,
                            @CookieValue(value = "UserId", defaultValue = "/") String userId) throws Exception {

        String uri = Client.getApiUri(request, "customers");
        WebClient client = Client.getWebClient();

        // Gets existing customer
        ResponseEntity<CustomerDto> customerResponse =
                client.get().uri(uri + "?userId=" + userId)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        Customer customer = customerResponse.getBody().getCustomer();

        // Gets cart
        uri = Client.getApiUri(request, "carts/customer/" + customer.getId());
        ResponseEntity<CartDto> cartResponse =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CartDto.class)).block();

        Cart cart = cartResponse.getBody().getCart();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(cart);

        // Creates order
        uri = Client.getApiUri(request, "orders");

        ResponseEntity<OrderDto> newOrderResponse =
                client.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(cart))
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(OrderDto.class)).block();

        if (newOrderResponse != null && newOrderResponse.getBody() != null && newOrderResponse.getStatusCode() != HttpStatus.NOT_FOUND) {
            uri = Client.getApiUri(request, "carts/customer/" + customer.getId());

            client.delete().uri(uri)
                    .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                    .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();
        }

        return "redirect:confirmation";
    }

    @RequestMapping(value = "/confirmation", method = RequestMethod.GET)
    public String confirmation(HttpServletRequest request, final Authentication authentication,
                        @CookieValue(value = "UserId", defaultValue = "/") String userId,
                        final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        return "customer/confirmation";
    }

    @RequestMapping(value = "/order-history", method = RequestMethod.GET)
    public String orderHistory(HttpServletRequest request, final Authentication authentication,
                               @CookieValue(value = "UserId", defaultValue = "/") String userId,
                               final Model model) throws Exception {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        WebClient client = Client.getWebClient();

        // Gets existing customer
        String uri = Client.getApiUri(request, "customers");
        ResponseEntity<CustomerDto> customerResponse =
                client.get().uri(uri + "?userId=" + userId)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        Customer customer = customerResponse.getBody().getCustomer();

        // Gets orders
        uri = Client.getApiUri(request, "orders/search?customerId=" + customer.getId());
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

        return "customer/orders";
    }
}