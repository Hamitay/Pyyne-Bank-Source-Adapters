package com.bank.dto;

import java.util.List;

public class AccountTransactionsDto {
  
  private List<TransactionGroupDto> transactionsyBank;

  public AccountTransactionsDto(List<TransactionGroupDto> transactionsyBank) {
    this.transactionsyBank = transactionsyBank;
  }

  public List<TransactionGroupDto> getTransactionsyBank() {
    return transactionsyBank;
  }

}


