package com.debitum.assets.resource.wallet;


import com.debitum.assets.application.wallet.WalletApplication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import static com.debitum.assets.domain.model.user.Authority.Const.ROLE_WALLET_OWNER;
import static com.debitum.assets.resource.config.Constants.DEFAULT_REST_API_PREFIX;


@Api(value = "contract-wallet-resource", description = "Actions with contract wallet")
@RestController
@RequestMapping(WalletResource.ROOT_MAPPING)
class WalletResource {

    static final String ROOT_MAPPING = DEFAULT_REST_API_PREFIX + "/contract-wallet";

    private final WalletApplication walletApplication;

    public WalletResource(WalletApplication walletApplication) {
        this.walletApplication = walletApplication;
    }

    @ApiOperation("Load contract balance")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Balance loaded successfully")}
    )
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_WALLET_OWNER)
    public WalletDTO getBalance() {
        return WalletDTO.from(
                walletApplication.getContractBalance(),
                walletApplication.getWalletBalance()
        );
    }

    @ApiOperation("Transfer ether from owner to smart contract")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Unexpected error occured"),
            @ApiResponse(code = 200, message = "Transfer sent to blockchain successfully")}
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(ROLE_WALLET_OWNER)
    public void getBalance(@RequestBody WalletTransferDTO dto) {
        walletApplication.transferFromOwnerAcountToSmartContract(dto.getAmount());
    }
}
