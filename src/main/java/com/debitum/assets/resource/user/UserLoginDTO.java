package com.debitum.assets.resource.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "UserLoginDTO",
        description = "Users login existence resource"
)
public class UserLoginDTO {

    @ApiModelProperty(value = "Users login", required = true)
    private String login;

    @ApiModelProperty(value = "Does users login already used", readOnly = true)
    private boolean exists;

    UserLoginDTO() {
    }

    public UserLoginDTO(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isExists() {
        return exists;
    }

    public UserLoginDTO setExists(boolean exists) {
        this.exists = exists;
        return this;
    }
}
