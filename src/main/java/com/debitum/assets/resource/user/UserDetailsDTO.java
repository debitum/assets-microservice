package com.debitum.assets.resource.user;



import com.debitum.assets.domain.model.user.Authority;
import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.domain.model.user.UserDetails;
import com.debitum.assets.domain.model.user.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@ApiModel(
        value = "UserDetailsDTO",
        description = "Users details with authorities resource"
)
public class UserDetailsDTO {

    @ApiModelProperty(value = "Users identifier")
    private UUID id;

    @ApiModelProperty(value = "Users login")
    private String login;

    @ApiModelProperty(value = "Users phone number")
    private String phone;

    @ApiModelProperty(value = "Users company")
    private String company;

    @ApiModelProperty(value = "Users fullname")
    private String name;

    @ApiModelProperty(value = "Users password")
    private String password;

    @ApiModelProperty(value = "Users status")
    private UserStatus status;


    @ApiModelProperty(value = "Users authorities")
    private Set<Authority> authorities;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }



    public static UserDetailsDTO from(UserDetails userDetails) {
        UserDetailsDTO dto = new UserDetailsDTO();
        User user = userDetails.getUser();
        dto.setId(user.getId());
        dto.setCompany(user.getCompany());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setStatus(user.getStatus());
        dto.setLogin(user.getLogin());
        dto.setPhone(user.getPhone());
        dto.setAuthorities(userDetails.getAuthorities());
        return dto;
    }
}
