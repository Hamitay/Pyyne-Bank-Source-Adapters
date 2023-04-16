package com.bank.dto;

import com.bank.model.Currency;

/**
 * DTO representing a given account balance for a given bank account source
 */
public class AccountBalanceDto {

  // Monetary values should always be integers to avoid floating point uncertainty
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
