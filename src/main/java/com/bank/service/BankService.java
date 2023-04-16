package com.bank.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountTransactionsDto;
import com.bank.dto.TransactionGroupDto;
import com.bank.integrations.BankAccountSourceAdapter;
import com.bank.integrations.bank1.Bank1AccountSourceAdapter;
import com.bank.integrations.bank2.Bank2AccountSourceAdapter;
import com.bank.model.BankAccount;

@ApplicationScoped
public class BankService {

  private List<BankAccountSourceAdapter> bankAdapters;

  @PostConstruct
  void init() {
    this.bankAdapters = List.of(new Bank1AccountSourceAdapter(), new Bank2AccountSourceAdapter());
  }

  public List<AccountBalanceDto> getBankBalances(long accountId) {
    return bankAdapters
      .stream()
      .map(adapter -> {
        BankAccount bankAccount = adapter.getBankAccount(accountId, null, null);
        return new AccountBalanceDto(bankAccount.getBalanceInCents(), bankAccount.getCurrency(), adapter.getSourceName());
      })
      .toList();
  }

  public AccountTransactionsDto getBankTransactions(long accountId) {
    List<TransactionGroupDto> x =  bankAdapters
    .stream()
    .map(adapter -> {
      BankAccount bankAccount = adapter.getBankAccount(accountId, null, null);
      return new TransactionGroupDto(adapter.getSourceName(), bankAccount.geTransactions());
    })
    .toList();

      return new AccountTransactionsDto(x);
  } 
}
