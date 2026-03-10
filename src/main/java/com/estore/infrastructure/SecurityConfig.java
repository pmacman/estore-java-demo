package com.estore.infrastructure;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.estore.controller.client.LogoutController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value(EstoreConstants.AuthSetting.Audience)
    private String audience;

    @Value(EstoreConstants.AuthSetting.IssuerUri)
    private String issuer;

    @Value(value = EstoreConstants.AuthSetting.Domain)
    private String domain;

    @Value(value = EstoreConstants.AuthSetting.ClientId)
    private String clientId;

    @Value(value = EstoreConstants.AuthSetting.ClientSecret)
    private String clientSecret;

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuer);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // API
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/states/**").permitAll()
//                .mvcMatchers(HttpMethod.DELETE, "/api/products/**").hasRole(EstoreConstants.Role.Partner)
//                .mvcMatchers(HttpMethod.POST, "/api/products/**").hasRole(EstoreConstants.Role.Partner)
//                .mvcMatchers(HttpMethod.PUT, "/api/products/**").hasRole(EstoreConstants.Role.Partner)
                .mvcMatchers("/api/**").authenticated()
                .and()
                .oauth2ResourceServer().jwt();

        // Client
        http.authorizeRequests()
                .mvcMatchers("/dist/**", "/callback/**", "/login/**", "/locale/**").permitAll()
                .mvcMatchers( "/customer/**", "/partner/**", "/product/**").authenticated()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/loginAction").permitAll()
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutController();
    }

    @Bean
    public AuthenticationController authenticationController() {
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .withJwkProvider(jwkProvider)
                .build();
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}