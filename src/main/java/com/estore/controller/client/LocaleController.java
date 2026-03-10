package com.estore.controller.client;

import com.estore.dto.CountryDto;
import com.estore.entity.State;
import com.estore.infrastructure.Client;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/locale")
public class LocaleController {
    @RequestMapping(value = "/states", method = RequestMethod.GET)
    public String states(HttpServletRequest request, final Model model, @RequestParam("country") String country) throws Exception {

        String uri = Client.getApiUri(request, "states?country=" + country);
        WebClient client = Client.getWebClient();

        ParameterizedTypeReference<CollectionModel<State>> type = new ParameterizedTypeReference<CollectionModel<State>>() {};

        ResponseEntity<CollectionModel<State>> result =
                client.get().uri(uri)
                        .exchange().flatMap(response -> response.toEntity(type)).block();

        model.addAttribute("states", result.getBody().getContent());

        return "locale/_states";
    }

    public CollectionModel<CountryDto> getCountries(HttpServletRequest request, final Authentication authentication) throws Exception {
        String uri = Client.getApiUri(request, "countries");
        WebClient client = Client.getWebClient();

        ParameterizedTypeReference<CollectionModel<CountryDto>> type = new ParameterizedTypeReference<CollectionModel<CountryDto>>() {};

        ResponseEntity<CollectionModel<CountryDto>> result =
                client.get().uri(uri)
                        .headers(headers -> headers.setBearerAuth(Client.getToken(authentication)))
                        .exchange().flatMap(response -> response.toEntity(type)).block();

        return result.getBody();
    }
}