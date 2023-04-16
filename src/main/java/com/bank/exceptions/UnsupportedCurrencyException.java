package com.bank.exceptions;

public class UnsupportedCurrencyException extends ApiException {

  public UnsupportedCurrencyException(String currency) {
    super(String.format("Unsupported currency %s", currency), 500);
  }
  
}
