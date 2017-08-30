package com.debitum.assets.resource.settings;


import com.debitum.assets.application.settings.SettingsApplication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_AUTHENTICATED;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;


@Api(value = "settings-resource", description = "Provider of application settings")
@RestController
@RequestMapping(SettingsResource.ROOT_MAPPING)
class SettingsResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/settings";

    private final SettingsApplication settingsApplication;

    public SettingsResource(SettingsApplication settingsApplication) {
        this.settingsApplication = settingsApplication;
    }


    @ApiOperation("Load settings")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Settings loaded successfully")}
    )
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_AUTHENTICATED)
    public SettingsDTO get() {
        return SettingsDTO.from(settingsApplication.getSettings());
    }
}
