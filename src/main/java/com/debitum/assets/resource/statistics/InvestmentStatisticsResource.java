package com.debitum.assets.resource.statistics;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.investment.InvestmentEntry;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_AUTHENTICATED;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;

@Api(value = "statistics-resource", description = "Information about invoices and investments statistics")
@RestController
@RequestMapping(InvestmentStatisticsResource.ROOT_MAPPING)
class InvestmentStatisticsResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/statistics";

    private final InvestmentApplication investmentApplication;

    public InvestmentStatisticsResource(InvestmentApplication investmentApplication) {
        this.investmentApplication = investmentApplication;
    }

    @ApiOperation("Get invoice investment statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Statistics loaded successfully")}
    )
    @RequestMapping(
            value = "/invoices/{invoiceId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_AUTHENTICATED)
    public List<InvestmentStatsDTO> get(@PathVariable String invoiceId) {
        List<InvestmentEntry> investmentEntries = investmentApplication.investmentEntriesOfInvoice(UUID.fromString(invoiceId));
        AtomicInteger index = new AtomicInteger();
        return investmentEntries.stream()
                .map(entry -> InvestmentStatsDTO.from("Investor " + index.incrementAndGet(), entry.getAmount()))
                .collect(Collectors.toList());
    }
}
