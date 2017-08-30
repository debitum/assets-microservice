package com.debitum.assets.resource.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "LoginDTO",
        description = "Login resource"
)
public class LoginDTO {

    @ApiModelProperty(value = "Users login")
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
