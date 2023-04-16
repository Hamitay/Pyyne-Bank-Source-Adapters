package com.bank;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountTransactionsDto;
import com.bank.service.BankService;

/**
 * Controller that pulls information form multiple bank integrations and prints them to the console.
 *
 * Created by Par Renyard on 5/12/21.
 */

@Path("/bank")
public class BankController {

    @Inject
    private BankService bankService;

    @GET
    @Path("/balances")
    public List<AccountBalanceDto> printBalances() {
        return bankService.getBankBalances(0L);
    }

    @GET()
    @Path("/transactions")
    public AccountTransactionsDto printTransactions() {
        return bankService.getBankTransactions(0L);
    }
}
