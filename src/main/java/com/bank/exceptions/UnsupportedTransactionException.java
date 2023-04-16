package com.bank.exceptions;

public class UnsupportedTransactionException extends RuntimeException {

  public UnsupportedTransactionException() {
    super("Unsupported transaction type");
  }
  
}

