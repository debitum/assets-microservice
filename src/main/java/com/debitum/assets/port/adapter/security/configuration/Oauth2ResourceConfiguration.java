package com.debitum.assets.port.adapter.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class Oauth2ResourceConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * Every request is allowed. @RolesAllowed annotation prevents access with no rights for resources.
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll();
    }


}
