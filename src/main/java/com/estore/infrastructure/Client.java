package com.estore.infrastructure;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {
    private static WebClient webClient;

    public static void init() {
        webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public static WebClient getWebClient() {
        return webClient;
    }

    public static String getToken(Authentication authentication) {
        String token = "";

        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            token = tokenAuthentication.getCredentials();
        }

        return token;
    }

    public static String getApiUri(HttpServletRequest request, String relativeUrl) throws MalformedURLException {
        return getBaseUrl(request) + "/api/" + relativeUrl;
    }

    private static String getBaseUrl(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
    }
}