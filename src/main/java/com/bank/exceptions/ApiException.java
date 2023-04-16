package com.bank.exceptions;

public class ApiException extends RuntimeException {
  
  public int statusCode;

  public ApiException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
}
