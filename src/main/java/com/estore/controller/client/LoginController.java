package com.estore.controller.client;

import com.auth0.AuthenticationController;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    private RequestCache requestCache = new HttpSessionRequestCache();

    private AuthenticationController controller;

    @Inject
    public LoginController(AuthenticationController controller) {
        this.controller = controller;
    }



    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req, final HttpServletResponse res) {
        SavedRequest savedRequest = requestCache.getRequest(req, res);
        String redirectUrl = "/";
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        }
        res.addCookie(new Cookie("RedirectUrl", redirectUrl));
        String redirectUri = req.getScheme() + "://" + req.getServerName();
        if ((req.getScheme().equals("http") && req.getServerPort() != 80) || (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            redirectUri += ":" + req.getServerPort();
        }
        redirectUri += "/callback";
        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri)
                .withScope("openid profile email")
                .build();
        return "redirect:" + authorizeUrl;
    }
}