package com.bank.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.logmanager.Logger;

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

    private static final Logger LOG = Logger.getLogger(BankController.class.getName());

    @Inject
    BankService bankService;

    @GET
    @Path("/balances/{account}")
    public Response printBalances(String account) {
        LOG.info(String.format("Received request to print balances for account %s", account));

        try {
            Long accountId = Long.parseLong(account);
            List<AccountBalanceDto> balances = bankService.getBankBalances(accountId);

            return Response.ok(balances).build();
        } 
        catch (Exception e) {
            LOG.severe(String.format("Error printing balances for account %s", account));
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET()
    @Path("/transactions/{account}")
    public Response printTransactions(String account) {
        LOG.info(String.format("Received request to print transactions for account %s", account));

        try {
            Long accountId = Long.parseLong(account);
            AccountTransactionsDto accountTransactions = bankService.getBankTransactions(accountId, null, null);
            
            return Response.ok(accountTransactions).build();
        } catch (Exception e) {
            LOG.severe(String.format("Error printing transactions for account %s", account));
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
