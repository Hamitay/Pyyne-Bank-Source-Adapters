package com.bank.exceptions;

public class UnsupportedTransactionException extends ApiException {

  public UnsupportedTransactionException() {
    super("Unsupported transaction type", 500);
  }
  
}

