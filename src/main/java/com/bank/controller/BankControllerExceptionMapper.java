package com.bank.controller;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.bank.exceptions.ApiException;

import org.jboss.logmanager.Logger;

@Provider
public class BankControllerExceptionMapper implements ExceptionMapper<ApiException> {

  private static final Logger LOG = Logger.getLogger(BankControllerExceptionMapper .class.getName());


  @Override
  public Response toResponse(ApiException exception) {
    String errorMessage = String.format("Api exception: $s \n Message: $s ", exception.getClass().getName(), exception.getMessage());
    LOG.severe(errorMessage);

    exception.printStackTrace();

    return Response
      .status(exception.statusCode)
      .entity(exception.getMessage())
      .build();
  }
}
