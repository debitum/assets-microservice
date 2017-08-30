package com.debitum.assets.resource.helpers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "Identifier",
        description = "Identifier"
)
public class  IdDTO {

    @ApiModelProperty(value = "Identifier", readOnly = true)
    private Long id;

    public IdDTO() {
    }

    public IdDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static IdDTO of(Long id) {
        return new IdDTO(id);
    }
}
