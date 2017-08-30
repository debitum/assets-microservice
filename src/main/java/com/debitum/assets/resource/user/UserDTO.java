package com.debitum.assets.resource.user;

import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.domain.model.user.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@ApiModel(
        value = "UserDTO",
        description = "Users resource"
)
public class UserDTO {

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


    @ApiModelProperty(value = "Users status")
    private UserStatus status;


    @ApiModelProperty(value = "Last edit timestamp", readOnly = true)
    private Instant updatedOn;


    static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setCompany(user.getCompany());
        dto.setName(user.getName());
        dto.setStatus(user.getStatus());
        dto.setLogin(user.getLogin());
        dto.setPhone(user.getPhone());
        dto.setUpdatedOn(user.getUpdatedOn());
        return dto;
    }

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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }


}
