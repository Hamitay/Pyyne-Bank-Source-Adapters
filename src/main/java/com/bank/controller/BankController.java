package com.bank.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
    @Path("/balances/{accountId}")
    public Response printBalances(Long accountId) {
        LOG.info(String.format("Received request to print balances for account %s", accountId));

        try {
            List<AccountBalanceDto> balances = bankService.getBankBalances(accountId);

            return Response.ok(balances).build();
        } 
        catch (Exception e) {
            LOG.severe(String.format("Error printing balances for account %s", accountId));
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET()
    @Path("/transactions/{accountId}")
    public Response printTransactions(
        Long accountId, 
        @QueryParam("fromDate") LocalDate fromDate, 
        @QueryParam("toDate") LocalDate toDate
    ) {
        LOG.info(String.format("Received request to print transactions for account %s", accountId));

        try {
            AccountTransactionsDto accountTransactions = bankService.getBankTransactions(accountId, fromDate, toDate);
            
            return Response.ok(accountTransactions).build();
        } catch (Exception e) {
            LOG.severe(String.format("Error printing transactions for account %s", accountId));
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
