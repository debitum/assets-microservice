package com.debitum.assets.resource.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(
        value = "PasswordChangeDTO",
        description = "Password change resource"
)
public class PasswordChangeDTO {

    @ApiModelProperty(value = "Users old password")
    private String oldPassword;

    @ApiModelProperty(value = "Users new password")
    private String newPassword;

    PasswordChangeDTO() {
    }

    public PasswordChangeDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
