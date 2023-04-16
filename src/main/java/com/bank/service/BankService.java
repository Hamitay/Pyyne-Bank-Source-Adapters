package com.bank.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountTransactionsDto;
import com.bank.dto.TransactionGroupDto;
import com.bank.integrations.BankAccountSourceAdapter;
import com.bank.integrations.bank1.Bank1AccountSourceAdapter;
import com.bank.integrations.bank2.Bank2AccountSourceAdapter;
import com.bank.model.BankAccount;

/*
 * Service responsible for fetching bank accounts in all the supported bank sources and converthing
 * the accounts to DTO to be consumed by the controller layer or other services
 */
@ApplicationScoped
public class BankService {

  @Inject
  Bank1AccountSourceAdapter bank1AccountSourceAdapter;

  @Inject
  Bank2AccountSourceAdapter bank2AccountSourceAdapter;

  
  private List<BankAccountSourceAdapter> getAdapters() {
    return List.of(bank1AccountSourceAdapter, bank2AccountSourceAdapter);
  }

  public List<AccountBalanceDto> getBankBalances(long accountId) {
    return getAdapters()
      .stream()
      .map(adapter -> {
        BankAccount bankAccount = adapter.getBankAccount(accountId, null, null);
        return new AccountBalanceDto(bankAccount.getBalanceInCents(), bankAccount.getCurrency(), adapter.getSourceName());
      })
      .toList();
  }

  public AccountTransactionsDto getBankTransactions(long accountId, Date fromDate, Date toDate) {
    List<TransactionGroupDto> x =  getAdapters()
    .stream()
    .map(adapter -> {
      BankAccount bankAccount = adapter.getBankAccount(accountId, fromDate, toDate);
      return new TransactionGroupDto(adapter.getSourceName(), bankAccount.geTransactions());
    })
    .toList();

      return new AccountTransactionsDto(x);
  } 
}
