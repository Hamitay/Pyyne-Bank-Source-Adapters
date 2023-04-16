package com.bank.dto;

import java.util.List;

/**
 * DTO representing a a list of transactions grouped by the bank account source
 */
public class AccountTransactionsDto {
  
  private List<TransactionGroupDto> transactionsyBank;

  public AccountTransactionsDto(List<TransactionGroupDto> transactionsyBank) {
    this.transactionsyBank = transactionsyBank;
  }

  public List<TransactionGroupDto> getTransactionsyBank() {
    return transactionsyBank;
  }

}


