package com.debitum.assets.resource.dashboard;


import com.debitum.assets.application.investment.DashboardApplication;
import com.debitum.assets.port.adapter.security.SecurityUtils;
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

@Api(value = "dashboard-resource", description = "Information about clients invoices and investments")
@RestController
@RequestMapping(DashboardResource.ROOT_MAPPING)
class DashboardResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/dashboard";

    private final DashboardApplication dashboardApplication;

    public DashboardResource(DashboardApplication dashboardApplication) {
        this.dashboardApplication = dashboardApplication;
    }

    @ApiOperation("Get dashboard info")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Dashboard loaded successfully")}
    )
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_AUTHENTICATED)
    public DashboardDTO get() {
        return DashboardDTO.from(dashboardApplication.overviewForUser(SecurityUtils.getCurrentUser().getId()));
    }
}
