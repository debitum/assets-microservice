package com.debitum.assets.resource.user;


import com.debitum.assets.application.user.UserApplicationService;
import com.debitum.assets.domain.model.user.UserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;
import static com.debitum.assets.resource.config.ErrorCodedException.errorCodedSupplier;
import static com.debitum.assets.resource.user.UserDetailsDTO.from;
import static java.text.MessageFormat.format;


@Api(value = "login-resource",
        description = "Actions that could be used by other microservices for getting user authorities")
@RequestMapping(PrivateLoginResource.ROOT_MAPPING)
@RestController
class PrivateLoginResource {

    public static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/private";
    public static String GET_USER_FAILED_NOT_FOUND = "GET_USER_FAILED_NOT_FOUND";

    private final UserApplicationService userApplicationService;

    PrivateLoginResource(
            UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @ApiOperation("Get user details by login")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Users details loaded successfully")}
    )
    @RequestMapping(value = "/login/{login}/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO getByLogin(@PathVariable("login") String login) {
        UserDetails user = userApplicationService.findUserBy(login);
        if (user == null) {
            throw errorCodedSupplier(
                    GET_USER_FAILED_NOT_FOUND,
                    format("User with login [{0}] not found.", login)
            ).get();
        }
        UserDetailsDTO userDetails = from(user);
        return userDetails;
    }


}
