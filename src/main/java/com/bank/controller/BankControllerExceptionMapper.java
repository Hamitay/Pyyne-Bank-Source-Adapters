package com.bank.controller;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.bank.exceptions.ApiException;

@Provider
public class BankControllerExceptionMapper implements ExceptionMapper<ApiException> {

  @Override
  public Response toResponse(ApiException exception) {
    return Response
      .status(exception.statusCode)
      .entity(exception.getMessage())
      .build();
  }
}
