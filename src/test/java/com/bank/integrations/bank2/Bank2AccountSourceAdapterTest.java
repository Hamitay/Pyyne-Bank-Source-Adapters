package com.bank.integrations.bank2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bank.integrations.bank2.integration.Bank2AccountBalance;
import com.bank.integrations.bank2.integration.Bank2AccountSource;
import com.bank.integrations.bank2.integration.Bank2AccountTransaction;
import com.bank.model.BankAccount;
import com.bank.model.BankTransactionType;
import com.bank.model.Currency;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class Bank2AccountSourceAdapterTest {

  @InjectMock
  Bank2AccountSource source;

  @Inject
  Bank2AccountSourceAdapter adapter;

  @Test
  public void testGetSourceName() {
    Assertions.assertEquals("Bank 2", adapter.getSourceName());
  }

  @Test
  public void testGetBankAccount() {

    Long accountId = 42L;


    Bank2AccountBalance balance = new Bank2AccountBalance(765.23, "EUR");
    Mockito.when(source.getBalance(accountId)).thenReturn(balance);

    List<Bank2AccountTransaction> transactions = Arrays.asList(
      new Bank2AccountTransaction(125d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Amazon.com"),
      new Bank2AccountTransaction(500d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Car insurance"),
      new Bank2AccountTransaction(800d, Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT, "Salary")
    );

    Mockito.when(source.getTransactions(accountId, null, null)).thenReturn(transactions);

    
    BankAccount bankAccount = adapter.getBankAccount(accountId, null, null);

    Assertions.assertEquals(76523, bankAccount.getBalanceInCents());
    Assertions.assertEquals(Currency.EUR, bankAccount.getCurrency());

    Assertions.assertEquals(3, bankAccount.geTransactions().size());

    Assertions.assertEquals(12500, bankAccount.geTransactions().get(0).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.DEBIT, bankAccount.geTransactions().get(0).getType());
    Assertions.assertEquals("Amazon.com", bankAccount.geTransactions().get(0).getText());

    
    Assertions.assertEquals(50000, bankAccount.geTransactions().get(1).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.DEBIT, bankAccount.geTransactions().get(1).getType());
    Assertions.assertEquals("Car insurance", bankAccount.geTransactions().get(1).getText());

    
    Assertions.assertEquals(80000, bankAccount.geTransactions().get(2).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.CREDIT, bankAccount.geTransactions().get(2).getType());
    Assertions.assertEquals("Salary", bankAccount.geTransactions().get(2).getText());
  }
  
}
