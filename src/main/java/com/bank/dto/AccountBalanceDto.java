package com.bank.dto;

import com.bank.model.Currency;

public class AccountBalanceDto {

  private Integer balanceInCents;

  private Currency currency;

  private String bankAccountSource;

  public AccountBalanceDto(Integer balance, Currency currency, String bankAccountSource) {
    this.balanceInCents = balance;
    this.currency = currency;
    this.bankAccountSource = bankAccountSource;
  }  

  public Integer getBalance() {
    return balanceInCents;
  }

  public Currency getCurrency() {
    return currency;
  }

  public String getBankAccountSource() {
    return bankAccountSource;
  }

}
