package com.bank.integrations.bank1;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.bank.exceptions.UnsupportedTransactionException;
import com.bank.integrations.BankAccountSourceAdapter;
import com.bank.integrations.bank1.integration.Bank1AccountSource;
import com.bank.integrations.bank1.integration.Bank1Transaction;
import com.bank.model.BankAccount;
import com.bank.model.BankTransaction;
import com.bank.model.BankTransactionType;
import com.bank.model.Currency;

@ApplicationScoped
public class Bank1AccountSourceAdapter implements BankAccountSourceAdapter {
  /*
  * In a real world scenario, this would been injected using dependency injection.
  * Since I didn't want to change the proposed class I'm instantiating in the constructor
  * for demonstration purposes
  */
  private Bank1AccountSource source = new Bank1AccountSource();;

  @Override
  public String getSourceName() {
    return "Bank 1";
  }

  @Override
  public BankAccount getBankAccount(long accountId, LocalDate transactionsFromDate, LocalDate transactionsToDate) {

    Integer balanceInCents = source.getAccountBalance(accountId).intValue() * 100;
    Currency currency = Currency.valueOf(source.getAccountCurrency(accountId));

    Date fromDate = transactionsFromDate != null ? Date.from(transactionsFromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    Date toDate = transactionsFromDate != null ? Date.from(transactionsToDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;    

    List<BankTransaction> transactions = source.getTransactions(accountId, fromDate, toDate)
      .stream()
      .map(this::convertTransaction)
      .toList();

    return new BankAccount(balanceInCents, currency, transactions);
  }
  
  private BankTransaction convertTransaction(Bank1Transaction transaction) {
    Integer balanceInCents = (int)transaction.getAmount() * 100;
    BankTransactionType type = convertTransactionType(transaction.getType());

    return new BankTransaction(type, balanceInCents, transaction.getText());
  }

  private BankTransactionType convertTransactionType(int transaction1Type) {
    switch(transaction1Type) {
      case 1:
        return BankTransactionType.CREDIT;
      case 2:
        return BankTransactionType.DEBIT;
      default:
        throw new UnsupportedTransactionException();
    }
  }
}
