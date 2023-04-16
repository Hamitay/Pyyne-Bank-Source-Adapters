package com.bank.integrations.bank1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bank.integrations.bank1.integration.Bank1AccountSource;
import com.bank.integrations.bank1.integration.Bank1Transaction;
import com.bank.model.BankAccount;
import com.bank.model.BankTransactionType;
import com.bank.model.Currency;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class Bank1AccountSourceAdapterTest {

  @InjectMock
  Bank1AccountSource source;

  @Inject
  Bank1AccountSourceAdapter adapter;

  @Test
  public void testGetSourceName() {
    Assertions.assertEquals("Bank 1", adapter.getSourceName());
  }

  @Test
  public void testGetBankAccount() {
    Long accountId = 42L;

    Mockito.when(source.getAccountBalance(accountId)).thenReturn(25.45d);
    Mockito.when(source.getAccountCurrency(accountId)).thenReturn("BRL");

    List<Bank1Transaction> transactions = Arrays.asList(
      new Bank1Transaction(100d, Bank1Transaction.TYPE_CREDIT, "Check deposit"),
      new Bank1Transaction(25.5d, Bank1Transaction.TYPE_DEBIT, "Debit card purchase"),
      new Bank1Transaction(225d, Bank1Transaction.TYPE_DEBIT, "Rent payment")
    );

    Mockito.when(source.getTransactions(accountId, null, null)).thenReturn(transactions);

    
    BankAccount bankAccount = adapter.getBankAccount(accountId, null, null);

    Assertions.assertEquals(2545, bankAccount.getBalanceInCents());
    Assertions.assertEquals(Currency.BRL, bankAccount.getCurrency());

    Assertions.assertEquals(3, bankAccount.geTransactions().size());

    Assertions.assertEquals(10000, bankAccount.geTransactions().get(0).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.CREDIT, bankAccount.geTransactions().get(0).getType());
    Assertions.assertEquals("Check deposit", bankAccount.geTransactions().get(0).getText());

    
    Assertions.assertEquals(2550, bankAccount.geTransactions().get(1).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.DEBIT, bankAccount.geTransactions().get(1).getType());
    Assertions.assertEquals("Debit card purchase", bankAccount.geTransactions().get(1).getText());

    
    Assertions.assertEquals(22500, bankAccount.geTransactions().get(2).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.DEBIT, bankAccount.geTransactions().get(2).getType());
    Assertions.assertEquals("Rent payment", bankAccount.geTransactions().get(2).getText());
  }
  
}
