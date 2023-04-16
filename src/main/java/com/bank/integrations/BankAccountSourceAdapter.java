package com.bank.integrations;

import java.util.Date;

import com.bank.model.BankAccount;

/*
 * Adapter for the BankAccountSource.
 * This interface serves as a layer of abstraction to convert BankAccounts from any BankAccountSource to the
 * BankAccount domain model object
 */
public interface BankAccountSourceAdapter {

  /*
   * Returns the source name, in case API callers want to filter by source type
   */
  public String getSourceName();
  
  /*
   * Returns a BankAccount converted from the source domain BankAccount type
   */
  public BankAccount getBankAccount(long accountId,  Date transactionsFromDate, Date transactionsToDate);

}
