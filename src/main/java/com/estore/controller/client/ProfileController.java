package com.estore.controller.client;

import com.estore.infrastructure.TokenAuthentication;
import com.estore.utility.TokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    protected String profile(final Authentication authentication, final Model model) {
        // Since we've configured Spring Security to only allow authenticated requests to
        // reach this endpoint, and we control the Authentication implementation, we can safely cast.
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (tokenAuthentication == null) {
            return "redirect:/login";
        }

        String profileJson = TokenUtils.claimsAsJson(tokenAuthentication.getClaims());

        model.addAttribute("profile", tokenAuthentication.getClaims());
        model.addAttribute("profileJson", profileJson);
        return "profile";
    }
}