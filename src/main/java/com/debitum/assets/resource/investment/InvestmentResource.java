package com.debitum.assets.resource.investment;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.application.user.UserApplicationService;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.security.exception.DomainAccessException;
import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.port.adapter.security.SecurityUtils;
import com.debitum.assets.resource.helpers.IdUuidDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.debitum.assets.domain.model.security.DataGuardianProvider.guardian;
import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_INVESTMENTS_EDIT;
import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_INVESTMENTS_VIEW;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;


@Api(value = "investment-resource", description = "Actions with investments")
@RestController
@RequestMapping(InvestmentResource.ROOT_MAPPING)
class InvestmentResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/investments";

    private final InvestmentApplication investmentApplication;
    private final UserApplicationService userApplicationService;

    public InvestmentResource(InvestmentApplication investmentApplication,
                              UserApplicationService userApplicationService) {
        this.investmentApplication = investmentApplication;
        this.userApplicationService = userApplicationService;
    }

    @ApiOperation("Create new investment")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Investment created successfully")}
    )
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_INVESTMENTS_EDIT)
    public IdUuidDTO create(@RequestBody InvestmentDTO dto) {
        if (!dto.getUserId().equals(SecurityUtils.getCurrentUser().getId())) {
            throw new DomainAccessException();
        }
        return IdUuidDTO.of(investmentApplication.create(dto.getUserId()).getId());
    }

    @ApiOperation("Load all users investments")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Users investments loaded successfully")}
    )
    @RequestMapping(
            value = "/bucket",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_INVESTMENTS_EDIT, ROLE_INVESTMENTS_VIEW})
    public List<InvestmentDTO> findAll() {
        List<Investment> investments = investmentApplication.findAll(
                SecurityUtils.getCurrentUser().getId()
        );
        User user = userApplicationService.getUser(SecurityUtils.getCurrentUser().getId());

        return investments.stream().
                map(investment -> InvestmentDTO.from(user.getName(), investment))
                .collect(Collectors.toList());
    }

    @ApiOperation("Load investment by identifier ")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Investment loaded successfully")}
    )
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_INVESTMENTS_EDIT, ROLE_INVESTMENTS_VIEW})
    public InvestmentDTO get(@PathVariable("id") String id) {
        Investment investment = investmentApplication.get(
                UUID.fromString(id)
        );
        guardian().checkAccessTo(investment);

        User user = userApplicationService.getUser(SecurityUtils.getCurrentUser().getId());

        return InvestmentDTO.from(user.getName(), investment);
    }

    @ApiOperation("Pay for investment ")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Investment payment sent to blockchain")}
    )
    @RequestMapping(
            value = "/{id}/payment",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_INVESTMENTS_EDIT, ROLE_INVESTMENTS_VIEW})
    public void payforInvestment(@PathVariable("id") String id, @RequestBody WalletPaymentDTO dto) {
        Investment investment = investmentApplication.get(
                UUID.fromString(id)
        );
        guardian().checkAccessTo(investment);

        investmentApplication.sendCoinForInvestment(
                investment,
                dto.getWalletSource(),
                dto.getPassword()
        );
    }

    @ApiOperation("Load investment entries by investment identifier ")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Investment loaded successfully")}
    )
    @RequestMapping(
            value = "/{id}/entries",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_INVESTMENTS_EDIT, ROLE_INVESTMENTS_VIEW})
    public List<InvestmentEntryDTO> findInvestmentsEntries(@PathVariable UUID id) {
        Investment investment = investmentApplication.get(
                id
        );
        guardian().checkAccessTo(investment);

        investmentApplication.findInvestmentsEntries(id);
        return investmentApplication.findInvestmentsEntries(id).stream()
                .map(InvestmentEntryDTO::from)
                .collect(Collectors.toList());
    }

}
