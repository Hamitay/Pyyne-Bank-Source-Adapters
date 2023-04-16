package com.bank.model;

import java.util.List;

public class BankAccount {
  
  // Monetary values should always be integers to avoid floating point uncertainty
  private Integer balanceInCents;

  private Currency currency;

  private List<BankTransaction> transactions;


  public BankAccount(Integer balanceInCents, Currency currency, List<BankTransaction> transactions) {
    this.balanceInCents = balanceInCents;
    this.currency = currency;
    this.transactions = transactions;
  }

  public Integer getBalanceInCents() {
    return this.balanceInCents;
  }

  public Currency getCurrency() {
    return this.currency;
  }

  public List<BankTransaction> geTransactions() {
    return this.transactions;
  }
}
