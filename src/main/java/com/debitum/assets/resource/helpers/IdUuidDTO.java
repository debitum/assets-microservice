package com.debitum.assets.resource.helpers;


import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

public class IdUuidDTO {

    @ApiModelProperty(value = "Identifier", readOnly = true)
    private UUID id;

    public IdUuidDTO() {
    }

    public IdUuidDTO(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static IdUuidDTO of(UUID id) {
        return new IdUuidDTO(id);
    }
}
