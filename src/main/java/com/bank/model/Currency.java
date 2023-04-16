package com.bank.model;

import com.bank.exceptions.UnsupportedCurrencyException;

/**
 * Enumerated type to map all the supported currency codes.
 * This makes our application throws an exception if an invalid or unsupported currency
 * is returned by any of the bank sources
 */
public enum Currency {
  USD("USD"),
  BRL("BRL"),
  EUR("EUR");

  private final String currencyCode;

  Currency(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  @Override
  public String toString() {
      return this.currencyCode;
  }

  public Currency fromCurrencyCode(String currencyCode) throws UnsupportedCurrencyException {
    try {
      return Currency.valueOf(currencyCode);
    } catch(IllegalArgumentException exception) {
      throw new UnsupportedCurrencyException(currencyCode);
    }
  }
}
