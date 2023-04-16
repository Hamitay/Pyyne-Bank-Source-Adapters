package com.bank.dto;

import java.util.List;

import com.bank.model.BankTransaction;

public class TransactionGroupDto {
  private String bankAccountSource;

  private List<BankTransaction> transactions;

  public TransactionGroupDto(String bankAccountSource, List<BankTransaction> transactions) {
    this.bankAccountSource = bankAccountSource;
    this.transactions = transactions;
  }

  public String getBankAccountSource() {
    return bankAccountSource;
  }

  public List<BankTransaction> getTransactions() {
    return transactions;
  }
}