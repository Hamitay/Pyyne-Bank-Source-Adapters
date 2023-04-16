package com.bank.model;



public class BankTransaction {

  private BankTransactionType type;

  private Integer amountInCents;

  private String text;

  public BankTransaction(BankTransactionType type, Integer amountInCents, String text) {
    this.type = type;
    this.amountInCents = amountInCents;
    this.text = text;
  }

  public BankTransactionType getType() {
    return type;
  }

  public Integer getAmountInCents() {
    return amountInCents;
  }

  public String getText() {
    return text;
  }
}
