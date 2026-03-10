package com.estore.controller.client;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.estore.controller.api.exception.UserNotFoundException;
import com.estore.dto.PartnerDto;
import com.estore.infrastructure.TokenAuthentication;
import com.estore.service.PartnerService;
import com.estore.utility.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpStatusCodeException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class CallbackController {
    private AuthenticationController controller;
    private final PartnerService partnerService;

    @Inject
    public CallbackController(AuthenticationController controller, PartnerService partnerService) {
        this.partnerService = partnerService;
        this.controller = controller;
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res,
                               @CookieValue(value = "RedirectUrl", defaultValue = "/") String redirectUrl) throws Exception {
        handle(req, res, redirectUrl);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res,
                                @CookieValue(value = "RedirectUrl", defaultValue = "/") String redirectUrl) throws Exception {
        handle(req, res, redirectUrl);
    }

    private void handle(HttpServletRequest req, HttpServletResponse res, String redirectUrl) throws Exception {
        try {
            // Remove RedirectUrl cookie
            Cookie cookie = new Cookie("RedirectUrl", null);
            cookie.setMaxAge(0);
            res.addCookie(cookie);

            Tokens tokens = controller.handle(req);
            String tokenId = tokens.getIdToken();

            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokenId));
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            Map<String, Claim> claims = tokenAuth.getClaims();
            Claim subClaim = claims.get("sub");
            String userId = subClaim.asString();
            // Adds auth user_id to cookie
            res.addCookie(new Cookie("UserId", userId));

            if (redirectUrl.contains("partner")) {
                // Get partner by userId
                // If null then display partner profile
                // else display partner welcome page
                try {
                    partnerService.getPartnerByContactUserId(userId);
                }
                catch (UserNotFoundException ex) {
                    res.sendRedirect("/partner");
                    return;
                }
            }

            res.sendRedirect(redirectUrl);
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            res.sendRedirect("/login");
        }
    }
}