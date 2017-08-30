package com.debitum.assets.resource.user;


import com.debitum.assets.application.user.UserApplicationService;
import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.port.adapter.security.SecurityUtils;
import com.debitum.assets.port.adapter.user.persistence.UserFilter;
import com.debitum.assets.resource.helpers.IdUuidDTO;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.security.RolesAllowed;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.debitum.assets.domain.model.user.Authority.Const.*;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;


@Api(value = "users-resource", description = "Actions with users")
@RestController
@RequestMapping(UserResource.ROOT_MAPPING)
class UserResource {
    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/users";

    private final UserApplicationService userApplicationService;

    UserResource(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @ApiOperation("Create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User created successfully")}
    )
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public IdUuidDTO create(@RequestBody UserDTO dto) {
        User newUser = userApplicationService.createUser(
                dto.getLogin(),
                dto.getPhone(),
                dto.getCompany(),
                dto.getName()
        );
        return new IdUuidDTO(
                newUser.getId()
        );
    }


    @ApiOperation("Change user's status")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User status changed successfully")}
    )
    @RequestMapping(
            value = "/{userId}/state",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_USERS_EDIT)
    public UserDTO changeUserStatus(@PathVariable UUID userId, @RequestBody UserDTO dto) {
        User loaded = userApplicationService.changeUserStatus(userId, dto.getStatus());
        return UserDTO.from(loaded);
    }

    @ApiOperation("Update user")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User updated successfully")}
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_USERS_EDIT)
    public IdUuidDTO update(@RequestBody UserDTO dto) {
        User newUser = userApplicationService.updateUser(
                dto.getId(),
                dto.getPhone(),
                dto.getCompany(),
                dto.getName()
        );
        return new IdUuidDTO(
                newUser.getId()
        );
    }

    @ApiOperation("Update users profile")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User profile changed successfully")}
    )
    @RequestMapping(
            value = "/profile",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_AUTHENTICATED)
    public UserDTO updateProfile(@RequestBody UserDTO dto) {
        User user = userApplicationService.getUser(
                SecurityUtils.getCurrentUser().getId()
        );
        User updatedUser = userApplicationService.updateUser(
                user.getId(),
                dto.getPhone(),
                dto.getCompany(),
                dto.getName()
        );
        return UserDTO.from(updatedUser);
    }

    @ApiOperation("Update users profile")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User profile changed successfully")}
    )
    @RequestMapping(
            value = "/profile/password",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_AUTHENTICATED)
    public void updatePassword(@RequestBody PasswordChangeDTO dto) {
        userApplicationService.updatePassword(
                SecurityUtils.getCurrentUser().getId(),
                dto.getOldPassword(),
                dto.getNewPassword());
    }

    @ApiOperation("Get users profile")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User profile loaded successfully")}
    )
    @RequestMapping(
            value = "/profile",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_AUTHENTICATED)
    public UserDTO getProfile() {
        User user = userApplicationService.getUser(
                SecurityUtils.getCurrentUser().getId()
        );

        return UserDTO.from(user);
    }

    @ApiOperation("Change initial users password")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Password changed successfully")}
    )
    @RequestMapping(
            value = "/password/{activationKey}",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void changeInitialPassword(@PathVariable String activationKey, @RequestBody PasswordDTO dto) {
        userApplicationService.changeInitialPassword(activationKey, dto.getPassword());
    }

    @ApiOperation("Resend initial users password activation link to user")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Activation link sent successfully")}
    )
    @RequestMapping(
            value = "/password-resend/{userId}",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void resendInitialPassword(@PathVariable UUID userId) {
        userApplicationService.resendInitialPasswordActivationLink(userId);
    }

    @ApiOperation("Change users password")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Password changed successfully")}
    )
    @RequestMapping(
            value = "/password-reset/{activationKey}",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@PathVariable String activationKey, @RequestBody PasswordDTO dto) {
        userApplicationService.resetPassword(activationKey, dto.getPassword());
    }

    @ApiOperation("Initiate password reset")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Password reset/reminder initiated successfully")}
    )
    @RequestMapping(
            value = "/password-reset",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void initiatePasswordReset(@RequestBody LoginDTO dto) {
        userApplicationService.initiatePasswordReminder(dto.getLogin());
    }

    @ApiOperation("Get existing user")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User loaded successfully")}
    )
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_USERS_VIEW, ROLE_USERS_EDIT})
    public UserDTO load(@PathVariable UUID id) {
        User loaded = userApplicationService.getUser(id);
        return UserDTO.from(loaded);
    }

    @ApiOperation("Load all users")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "User loaded successfully")}
    )
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_USERS_VIEW, ROLE_USERS_EDIT})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "User name fragment", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "states", value = "User states", required = false, dataType = "String[]", paramType = "query"),
    })
    public List<UserDTO> findAll(@ApiIgnore UserFilter userFilter) {
        List<User> loaded = userApplicationService.findAll(userFilter);
        return loaded.stream().map(
                UserDTO::from
        ).sorted(
                Comparator.comparing(UserDTO::getName)
        ).collect(
                Collectors.toList()
        );
    }

    @ApiOperation("Validate if users login already used")
    @ApiResponse(code = 200, message = "Users login validated successfully")
    @RequestMapping(
            value = "/logins",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_AUTHENTICATED, ROLE_USERS_VIEW, ROLE_USERS_EDIT})
    public UserLoginDTO nameExists(@RequestBody UserLoginDTO dto) {
        return dto.setExists(userApplicationService.loginIsUsed(dto.getLogin()));
    }

}
