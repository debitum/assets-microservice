package com.debitum.assets.resource.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "PasswordDTO",
        description = "Password setup resource"
)
public class PasswordDTO {

    @ApiModelProperty(value = "Users new password")
    private String password;

    public PasswordDTO() {
    }

    public PasswordDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
