package com.debitum.assets.port.adapter.security.configuration;


import com.debitum.assets.port.adapter.security.UserAuthDetails;
import com.debitum.assets.port.adapter.security.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Configuration
class UserInfoTokenServicesConfiguration {

    @Autowired
    private ResourceServerProperties sso;

    @Autowired(required = false)
    @Qualifier("userInfoRestTemplate")
    private OAuth2RestOperations restTemplate;

    @Autowired(required = false)
    private AuthoritiesExtractor authoritiesExtractor;

    /**
     * userInfoTokenServices bean mainly provides Principal mapping to UserAuthDetails which is stored
     * in security context to get info about logged in user.
     */
    @Bean
    @Primary
    public UserInfoTokenServices userInfoTokenServices() {
        UserInfoTokenServices services = new UserInfoTokenServices(
                this.sso.getUserInfoUri(), this.sso.getClientId()) {
            @Override
            protected Object getPrincipal(Map<String, Object> map) {
                Map<String, Object> principal = (Map<String, Object>) map.get("principal");


                List authorities = (List) principal.get("authorities");
                List<SimpleGrantedAuthority> authoritiesConverted = (List<SimpleGrantedAuthority>) authorities.stream().map(auth -> new SimpleGrantedAuthority((String) auth)).collect(Collectors.toList());

                Integer impersonateUserId = (Integer) principal.getOrDefault("impersonateUserId", null);
                return new UserAuthDetails(UUID.fromString((String) principal.get("id")),
                        (String) principal.get("username"),
                        (String) principal.get("phone"),
                        (String) principal.get("company"),
                        (String) principal.get("name"),
                        "HIDDEN_PASSWORD",
                        UserStatus.valueOf((String) principal.get("status")),
                        authoritiesConverted);
            }
        };
        services.setRestTemplate(this.restTemplate);
        services.setTokenType(this.sso.getTokenType());
        if (this.authoritiesExtractor != null) {
            services.setAuthoritiesExtractor(this.authoritiesExtractor);
        }
        return services;
    }
}