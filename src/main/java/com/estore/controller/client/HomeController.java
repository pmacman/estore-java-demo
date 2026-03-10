package com.estore.controller.client;

import com.estore.dto.CartDto;
import com.estore.dto.CustomerDto;
import com.estore.dto.ProductDto;
import com.estore.entity.Cart;
import com.estore.entity.CartDetail;
import com.estore.entity.Product;
import com.estore.infrastructure.Client;
import com.estore.infrastructure.TokenAuthentication;
import com.estore.model.CartItem;
import com.estore.model.CollectionModelEx;
import com.estore.model.ProductSearch;
import com.estore.model.ProductSearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
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
@RequestMapping("/")
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(final Authentication authentication, final Model model) {

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            model.addAttribute("profile", tokenAuthentication.getClaims());
        }

        model.addAttribute("productSearch", new ProductSearch());

        // If not authenticated, still show home page.
        // View will render appropriate login/menu based on authentication status
        return "home/index";
    }

    @RequestMapping(value = "/search-submit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductSearchResult> searchSubmit(HttpServletRequest request, final Authentication authentication,
                                                            @CookieValue(value = "UserId", defaultValue = "/") String userId,
                                                            final Model model,
                                                            @ModelAttribute ProductSearch filter) throws Exception {

        WebClient client = Client.getWebClient();

        String uri = Client.getApiUri(request, "products/search?start=" + filter.getStart() + "&length=" + filter.getLength() + "&productName=" + filter.getProductName());

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

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String product(HttpServletRequest request, final Authentication authentication,
                          @CookieValue(value = "UserId", defaultValue = "/") String userId,
                          final Model model, @PathVariable("id") int id) throws Exception {

        String uri = Client.getApiUri(request, "products/" + id);
        WebClient client = Client.getWebClient();

        ResponseEntity<ProductDto> result =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(ProductDto.class)).block();

        Product product;

        if (result == null || result.getStatusCode() == HttpStatus.NOT_FOUND) {
            product = new Product();
        } else {
            product = result.getBody().getProduct();
        }

        model.addAttribute("product", product);

        // Gets customer
        uri = Client.getApiUri(request, "customers?userId=" + userId);
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
            CartDetail cartDetail = new CartDetail(product, 1);
            List<CartDetail> cartDetails = new ArrayList<>();
            cartDetails.add(cartDetail);
            cart = new Cart(customer.getBody().getCustomer(), cartDetails);
        } else {
            cart = cartResponse.getBody().getCart();
            List<CartDetail> cartDetailsToSave = new ArrayList<>();

            for (CartDetail cd : cart.getCartDetails()) {
                if (cd.getProduct().getId() == id) {
                    cartDetailsToSave.add(cd);
                }
            }

            cart.setCartDetails(cartDetailsToSave);
        }

        model.addAttribute("cart", cart);

        return "customer/_productModal";
    }

    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)
    public ResponseEntity addToCart(HttpServletRequest request, final Authentication authentication,
                            @CookieValue(value = "UserId", defaultValue = "/") String userId,
                            @ModelAttribute Cart cart) throws Exception {

        // Gets customer
        String uri = Client.getApiUri(request, "customers?userId=" + userId);
        WebClient client = Client.getWebClient();
        ResponseEntity<CustomerDto> customer =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CustomerDto.class)).block();

        cart.setCustomer(customer.getBody().getCustomer());

        uri = Client.getApiUri(request, "carts");

        CartDetail cartDetail = cart.getCartDetails().get(0);
        CartItem cartItem = new CartItem(customer.getBody().getCustomer().getId(), cartDetail.getProduct().getId(), cartDetail.getQuantity());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(cartItem);

        ResponseEntity<CartDto> savedCart =
                client.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(cartItem))
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(CartDto.class)).block();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}