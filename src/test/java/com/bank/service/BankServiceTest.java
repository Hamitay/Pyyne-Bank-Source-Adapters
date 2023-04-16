package com.bank.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountTransactionsDto;
import com.bank.integrations.bank1.Bank1AccountSourceAdapter;
import com.bank.integrations.bank2.Bank2AccountSourceAdapter;
import com.bank.model.BankAccount;
import com.bank.model.BankTransaction;
import com.bank.model.BankTransactionType;
import com.bank.model.Currency;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class BankServiceTest {

  @InjectMock
  Bank1AccountSourceAdapter bank1AccountSourceAdapter;

  @InjectMock
  Bank2AccountSourceAdapter bank2AccountSourceAdapter;

  @Inject
  BankService bankService;

  private static Long ACCOUNT_ID = 42L;

  @BeforeAll
  public void setup() {
    Mockito.when(bank1AccountSourceAdapter.getSourceName()).thenReturn("Bank 1");
    Mockito.when(bank2AccountSourceAdapter.getSourceName()).thenReturn("Bank 2");
  }

  @Test
  public void testGetBankBalances() {
    BankAccount bank1Account = new BankAccount(100, Currency.USD, List.of());
    Mockito.when(bank1AccountSourceAdapter.getBankAccount(ACCOUNT_ID, null, null)).thenReturn(bank1Account);

    BankAccount bank2Account = new BankAccount(250, Currency.BRL, List.of());
    Mockito.when(bank2AccountSourceAdapter.getBankAccount(ACCOUNT_ID, null, null)).thenReturn(bank2Account);

    List<AccountBalanceDto> balances = bankService.getBankBalances(ACCOUNT_ID);

    Assertions.assertEquals(2, balances.size());

    Assertions.assertEquals(100, balances.get(0).getBalance());
    Assertions.assertEquals(Currency.USD, balances.get(0).getCurrency());
    Assertions.assertEquals(250, balances.get(1).getBalance());
    Assertions.assertEquals(Currency.BRL, balances.get(1).getCurrency());
  }

  @Test
  public void testGetBankTransactions() {
    BankTransaction creditTransaction = new BankTransaction(BankTransactionType.CREDIT, 120, "Amazon");
    BankAccount bank1Account = new BankAccount(100, Currency.USD, List.of(creditTransaction));

    Mockito.when(bank1AccountSourceAdapter.getBankAccount(ACCOUNT_ID, null, null)).thenReturn(bank1Account);

    BankTransaction debitTransaction = new BankTransaction(BankTransactionType.DEBIT, -960, "Google");
    BankAccount bank2Account = new BankAccount(100, Currency.BRL, List.of(creditTransaction, debitTransaction));
    Mockito.when(bank2AccountSourceAdapter.getBankAccount(ACCOUNT_ID, null, null)).thenReturn(bank2Account);

    AccountTransactionsDto transactions = bankService.getBankTransactions(ACCOUNT_ID, null, null);

    Assertions.assertEquals(2, transactions.getTransactionsyBank().size());

    Assertions.assertEquals("Bank 1", transactions.getTransactionsyBank().get(0).getBankAccountSource());
    Assertions.assertEquals(1, transactions.getTransactionsyBank().get(0).getTransactions().size());
    Assertions.assertEquals(120, transactions.getTransactionsyBank().get(0).getTransactions().get(0).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.CREDIT, transactions.getTransactionsyBank().get(0).getTransactions().get(0).getType());
    Assertions.assertEquals("Amazon", transactions.getTransactionsyBank().get(0).getTransactions().get(0).getText());

    Assertions.assertEquals("Bank 2", transactions.getTransactionsyBank().get(1).getBankAccountSource());
    Assertions.assertEquals(2, transactions.getTransactionsyBank().get(1).getTransactions().size());
    Assertions.assertEquals(120, transactions.getTransactionsyBank().get(1).getTransactions().get(0).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.CREDIT, transactions.getTransactionsyBank().get(1).getTransactions().get(0).getType());
    Assertions.assertEquals("Amazon", transactions.getTransactionsyBank().get(1).getTransactions().get(0).getText());
    Assertions.assertEquals(-960, transactions.getTransactionsyBank().get(1).getTransactions().get(1).getAmountInCents());
    Assertions.assertEquals(BankTransactionType.DEBIT, transactions.getTransactionsyBank().get(1).getTransactions().get(1).getType());
    Assertions.assertEquals("Google", transactions.getTransactionsyBank().get(1).getTransactions().get(1).getText());
  }
  
      
}
