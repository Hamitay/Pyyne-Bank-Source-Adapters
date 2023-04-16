package com.bank.exceptions;

public class UnsupportedCurrencyException extends Exception {

  public UnsupportedCurrencyException(String currency) {
    super(String.format("Unsupported currency %s", currency));
  }
  
}
