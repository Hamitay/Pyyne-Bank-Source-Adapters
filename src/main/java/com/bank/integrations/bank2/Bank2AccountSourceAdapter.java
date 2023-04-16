package com.bank.integrations.bank2;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.bank.exceptions.UnsupportedTransactionException;
import com.bank.integrations.BankAccountSourceAdapter;
import com.bank.integrations.bank2.integration.Bank2AccountBalance;
import com.bank.integrations.bank2.integration.Bank2AccountSource;
import com.bank.integrations.bank2.integration.Bank2AccountTransaction;
import com.bank.integrations.bank2.integration.Bank2AccountTransaction.TRANSACTION_TYPES;
import com.bank.model.BankAccount;
import com.bank.model.BankTransaction;
import com.bank.model.BankTransactionType;
import com.bank.model.Currency;

public class Bank2AccountSourceAdapter implements BankAccountSourceAdapter{

  private Bank2AccountSource source;

  public Bank2AccountSourceAdapter() {
    /*
     * In a real world scenario, this would been injected using dependency injection.
     * Since I didn't want to change the proposed class I'm instantiating in the constructor
     * for demonstration purposes
     */
    this.source = new Bank2AccountSource();
  }

  @Override
  public String getSourceName() {
    return "Bank 2";
  }

  @Override
  public BankAccount getBankAccount(long accountId, Date transactionsFromDate,
      Date transactionsToDate) {

      Bank2AccountBalance balance = source.getBalance(accountId);

      Integer balanceInCents = (int) balance.getBalance() * 100;
      Currency currency = Currency.valueOf(balance.getCurrency());

      List<BankTransaction> transactions = source.getTransactions(accountId, transactionsFromDate, transactionsToDate)
        .stream()
        .map(this::convertTransaction)
        .toList();

      return new BankAccount(balanceInCents, currency, transactions);
  }

  private BankTransaction convertTransaction(Bank2AccountTransaction transaction) {
    Integer balanceInCents = (int)transaction.getAmount() * 100;
    BankTransactionType type = convertTransactionType(transaction.getType());

    return new BankTransaction(type, balanceInCents, transaction.getText());
  }
  
  private BankTransactionType convertTransactionType(TRANSACTION_TYPES transactionType) {
    switch(transactionType) {
      case CREDIT:
        return BankTransactionType.CREDIT;
      case DEBIT:
        return BankTransactionType.DEBIT;
      default:
        throw new UnsupportedTransactionException();
    }
  }
}
