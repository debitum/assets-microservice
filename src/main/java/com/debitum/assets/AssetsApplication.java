package com.debitum.assets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(considerNestedRepositories = true)
@EnableFeignClients
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@EnableResourceServer
@EnableAsync
@EnableScheduling
public class AssetsApplication {

    public static final String TEST_PROFILE = "test";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AssetsApplication.class);
        app.run(args);
    }
}
