package com.debitum.assets.resource.investment;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.investment.exception.InvalidInvestmentEntriesException;
import com.debitum.assets.domain.model.security.exception.DomainAccessException;
import com.debitum.assets.port.adapter.security.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_INVESTMENTS_EDIT;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;


@Api(value = "batched-investments-entries-resource", description = "Actions with batched investments entries")
@RestController
@RequestMapping(BatchedInvestmentEntryResource.ROOT_MAPPING)
class BatchedInvestmentEntryResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/batched-investments-entries";

    private final InvestmentApplication investmentApplication;

    public BatchedInvestmentEntryResource(InvestmentApplication investmentApplication) {
        this.investmentApplication = investmentApplication;
    }


    @ApiOperation("Batched investment entry will be saved as new ones or updated if exists")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Investment entries persisted successfully")}
    )
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_INVESTMENTS_EDIT)
    public void persist(@RequestBody List<InvestmentEntryDTO> dtoList) {
        if (isNotEmpty(dtoList)) {

            Set<UUID> investmentIds = dtoList.stream().map(InvestmentEntryDTO::getInvestmentId).collect(Collectors.toSet());
            if (investmentIds.size() != 1) {
                throw new InvalidInvestmentEntriesException();
            } else {
                investmentIds.forEach(id -> {
                    if (!investmentApplication.get(id).getUserId().equals(SecurityUtils.getCurrentUser().getId())) {
                        throw new DomainAccessException();
                    }
                });
            }

            List<InvestmentApplication.PersistInvestmentEntryCommand> commands = dtoList.stream().map(dto ->
                    new InvestmentApplication.PersistInvestmentEntryCommand(
                            dto.getId(),
                            dto.getInvoiceId(),
                            dto.getInvestmentId(),
                            dto.getAmount())
            ).collect(Collectors.toList());
            investmentApplication.execute(commands);
        }
    }
}
