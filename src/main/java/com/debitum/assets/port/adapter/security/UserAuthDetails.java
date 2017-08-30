package com.debitum.assets.port.adapter.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class UserAuthDetails extends User {

    private UUID id;
    private String login;
    private String phone;
    private String company;
    private String name;
    private UserStatus status;


    public UserAuthDetails(UUID id,
                           String login,
                           String phone,
                           String company,
                           String name,
                           String password,
                           UserStatus status,
                           Collection<? extends GrantedAuthority> authorities) {
        super(login, password, true, true, true, true, authorities);
        this.id = id;
        this.login = login;
        this.phone = phone;
        this.company = company;
        this.name = name;
        this.status = status;

    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public UserStatus getStatus() {
        return status;
    }


}
