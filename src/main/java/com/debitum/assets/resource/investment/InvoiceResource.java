package com.debitum.assets.resource.investment;


import com.debitum.assets.application.investment.InvoiceApplication;
import com.debitum.assets.domain.model.investment.Invoice;
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

import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_INVOICE_EDIT;
import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_INVOICE_VIEW;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;
import static java.util.stream.Collectors.toList;


@Api(value = "invoice-resource", description = "Actions with invoices")
@RestController
@RequestMapping(InvoiceResource.ROOT_MAPPING)
class InvoiceResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/invoices";

    private final InvoiceApplication invoiceApplication;

    public InvoiceResource(InvoiceApplication invoiceApplication) {
        this.invoiceApplication = invoiceApplication;
    }

    @ApiOperation("Create new invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Invoice created successfully")}
    )
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_INVOICE_EDIT)
    public IdUuidDTO create(@RequestBody InvoiceDTO dto) {
        Invoice invoice = invoiceApplication.create(
                dto.getLoanType(),
                dto.getIssueDate(),
                dto.getLoanAmount(),
                dto.getAdvanceRate(),
                dto.getInterestRate(),
                dto.getOriginator(),
                dto.getLoanBalance(),
                dto.getTerm(),
                dto.getListDate(),
                dto.getBusinessSector(),
                dto.getDescription(),
                dto.getInvoiceTransaction(),
                dto.getCreditRank()
        );

        return IdUuidDTO.of(invoice.getId());
    }

    @ApiOperation("Upload invoices")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Invoices uploaded successfully")}
    )
    @RequestMapping(
            value = "/upload",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_INVOICE_EDIT)
    public void upload(@RequestBody InvoiceImportDTO dto) {
        List<InvoiceApplication.PersistInvoiceCommand> commands = dto.getInvoices().stream().map(invoice ->
                new InvoiceApplication.PersistInvoiceCommand(
                        invoice.getLoanType(),
                        invoice.getIssueDate(),
                        invoice.getLoanAmount(),
                        invoice.getAdvanceRate(),
                        invoice.getInterestRate(),
                        invoice.getAvailableForInvestment(),
                        invoice.getOriginator(),
                        invoice.getLoanBalance(),
                        invoice.getTerm(),
                        invoice.getListDate(),
                        invoice.getBusinessSector(),
                        invoice.getDescription(),
                        invoice.getInvoiceTransaction(),
                        invoice.getCreditRank()
                )).collect(toList());
        invoiceApplication.execute(commands);
    }

    @ApiOperation("Load all invoices available for investment")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Invoices loaded successfully")}
    )
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_INVOICE_EDIT, ROLE_INVOICE_VIEW})
    public List<InvoiceDTO> findAvailable() {
        List<Invoice> invoices = invoiceApplication.findAvailableForInvestments();
        return invoices.stream().map(InvoiceDTO::from).collect(toList());
    }

    @ApiOperation("Load all invoices")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Invoices loaded successfully")}
    )
    @RequestMapping(
            value = "/history",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({ROLE_INVOICE_EDIT})
    public List<InvoiceDTO> findAll() {
        List<Invoice> invoices = invoiceApplication.findAll();
        return invoices.stream().map(InvoiceDTO::from).collect(toList());
    }

    @ApiOperation("Repay invoices")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Invoices uploaded successfully")}
    )
    @RequestMapping(
            value = "/repayments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_INVOICE_EDIT)
    public void repay(@RequestBody InvoiceRepaymentDTO dto) {
        invoiceApplication.repayInvoices(dto.getInvoices());
    }

    @ApiOperation("Repay invoices")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occurred"),
            @ApiResponse(code = 200, message = "Invoices uploaded successfully")}
    )
    @RequestMapping(
            value = "/repayments-validations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_INVOICE_EDIT)
    public InvoiceRepaymentValidationDTO repaymentValidation(@RequestBody InvoiceRepaymentDTO dto) {
        return InvoiceRepaymentValidationDTO.from(invoiceApplication.invoiceRepaymentValidation(dto.getInvoices()));
    }

}
