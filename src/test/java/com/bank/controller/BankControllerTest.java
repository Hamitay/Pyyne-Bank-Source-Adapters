package com.bank.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountTransactionsDto;
import com.bank.dto.TransactionGroupDto;
import com.bank.exceptions.ApiException;
import com.bank.model.BankTransaction;
import com.bank.model.BankTransactionType;
import com.bank.model.Currency;
import com.bank.service.BankService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
public class BankControllerTest {

  @InjectMock
  BankService bankService;

  @Test
  public void testPrintBalancesBaseCase() {
    Long accountId = 12L;

    AccountBalanceDto balanceDto = new AccountBalanceDto(100, Currency.BRL, "Bank 3");

    Mockito.when(bankService.getBankBalances(accountId)).thenReturn(List.of(balanceDto));

    given()
      .when().get("/bank/balances/12")
      .then()
      .statusCode(200)
      .body(is("[{\"currency\":\"BRL\",\"bankAccountSource\":\"Bank 3\",\"balance\":100}]"));
  }

  @Test
  public void testPrintBalancesMultipleResponses() {
    Long accountId = 12L;

    AccountBalanceDto firstBalanceDto = new AccountBalanceDto(100, Currency.BRL, "Itaú");
    AccountBalanceDto seconBalanceDto = new AccountBalanceDto(0, Currency.USD, "Morgan Chase");
    AccountBalanceDto thirdAccountBalanceDto = new AccountBalanceDto(-500, Currency.EUR, "Deutsch Bank");

    Mockito.when(bankService.getBankBalances(accountId)).thenReturn(List.of(firstBalanceDto, seconBalanceDto, thirdAccountBalanceDto));

    given()
      .when().get("/bank/balances/12")
      .then()
      .statusCode(200)
      .body(is("[{\"currency\":\"BRL\",\"bankAccountSource\":\"Itaú\",\"balance\":100}," +
        "{\"currency\":\"USD\",\"bankAccountSource\":\"Morgan Chase\",\"balance\":0}," +
        "{\"currency\":\"EUR\",\"bankAccountSource\":\"Deutsch Bank\",\"balance\":-500}]"));
  }

  @Test
  public void testPrintBalancesInvalidAccountId() {
    given()
      .when().get("/bank/balances/invalidId")
      .then()
      .statusCode(404);
  }

  @Test
  public void testPrintBalancesApiException() {
    Long accountId = 43L;
    Mockito.when(bankService.getBankBalances(accountId)).thenThrow(new ApiException("Test exception", 403));

    given()
      .when().get("/bank/balances/43")
      .then()
      .statusCode(403);
  }

  @Test
  public void testPrintBalancesUnhandledException() {
    Long accountId = 43L;
    Mockito.when(bankService.getBankBalances(accountId)).thenThrow(new RuntimeException());

    given()
      .when().get("/bank/balances/43")
      .then()
      .statusCode(500);
  }

  @Test
  public void testPrintTransactionsBaseCase() {
    Long accountId = 12L;

    BankTransaction creditTransaction = new BankTransaction(BankTransactionType.CREDIT, 120, "Amazon");
    BankTransaction debitTransaction = new BankTransaction(BankTransactionType.DEBIT, -90, "Google");
    TransactionGroupDto transactionGroupDto = new TransactionGroupDto("Bank 42", List.of(creditTransaction, debitTransaction));

    AccountTransactionsDto transactionsDto = new AccountTransactionsDto(List.of(transactionGroupDto));

    Mockito.when(bankService.getBankTransactions(accountId, null, null)).thenReturn(transactionsDto);

    given()
      .when().get("/bank/transactions/12")
      .then()
      .statusCode(200)
      .body(is("{\"transactionsyBank\":[{\"bankAccountSource\":\"Bank 42\",\"transactions\":[{\"type\":\"CREDIT\",\"amountInCents\":120,\"text\":\"Amazon\"},{\"type\":\"DEBIT\",\"amountInCents\":-90,\"text\":\"Google\"}]}]}"));
  }

  @Test
  public void testPrintTransactionsMultipleBanks() {
    Long accountId = 12L;

    BankTransaction firstBankCreditTransaction = new BankTransaction(BankTransactionType.CREDIT, 120, "Amazon");
    BankTransaction firstBankDebitTransaction = new BankTransaction(BankTransactionType.DEBIT, -90, "Google");
    TransactionGroupDto firstBankTransactionGroupDto = new TransactionGroupDto("Bank 42", List.of(firstBankCreditTransaction, firstBankDebitTransaction));

    BankTransaction secondCreditTransaction = new BankTransaction(BankTransactionType.CREDIT, 342, "Trader Joes");
    BankTransaction secondDebitTransaction = new BankTransaction(BankTransactionType.DEBIT, 05, "BestBuy");
    TransactionGroupDto secondTransactionGroupDto = new TransactionGroupDto("Super Bank", List.of(secondCreditTransaction, secondDebitTransaction));

    BankTransaction thirdCreditTransaction = new BankTransaction(BankTransactionType.CREDIT, 999999999, "Microsoft");
    BankTransaction thirdDebitTransaction = new BankTransaction(BankTransactionType.DEBIT, 100, "Mcdonalds");
    TransactionGroupDto thirdTransactionGroupDto = new TransactionGroupDto("Bank 42", List.of(thirdCreditTransaction, thirdDebitTransaction));

    AccountTransactionsDto transactionsDto = new AccountTransactionsDto(List.of(firstBankTransactionGroupDto, secondTransactionGroupDto, thirdTransactionGroupDto));

    Mockito.when(bankService.getBankTransactions(accountId, null, null)).thenReturn(transactionsDto);

    given()
      .when().get("/bank/transactions/12")
      .then()
      .statusCode(200)
      .body(is("{\"transactionsyBank\":[{\"bankAccountSource\":\"Bank 42\",\"transactions\":[{\"type\":\"CREDIT\",\"amountInCents\":120,\"text\":\"Amazon\"},{\"type\":\"DEBIT\",\"amountInCents\":-90,\"text\":\"Google\"}]},{\"bankAccountSource\":\"Super Bank\",\"transactions\":[{\"type\":\"CREDIT\",\"amountInCents\":342,\"text\":\"Trader Joes\"},{\"type\":\"DEBIT\",\"amountInCents\":5,\"text\":\"BestBuy\"}]},{\"bankAccountSource\":\"Bank 42\",\"transactions\":[{\"type\":\"CREDIT\",\"amountInCents\":999999999,\"text\":\"Microsoft\"},{\"type\":\"DEBIT\",\"amountInCents\":100,\"text\":\"Mcdonalds\"}]}]}"));
  }

  @Test
  public void testPrintTransactionsWithDateFilters() {
    Long accountId = 12L;
    
    LocalDate fromDate = LocalDate.parse("2023-01-01");
    LocalDate toDate = LocalDate.parse("2023-04-01");

    BankTransaction creditTransaction = new BankTransaction(BankTransactionType.CREDIT, 120, "Amazon");
    BankTransaction debitTransaction = new BankTransaction(BankTransactionType.DEBIT, -90, "Google");
    TransactionGroupDto transactionGroupDto = new TransactionGroupDto("Bank 42", List.of(creditTransaction, debitTransaction));

    AccountTransactionsDto transactionsDto = new AccountTransactionsDto(List.of(transactionGroupDto));

    Mockito.when(bankService.getBankTransactions(accountId, fromDate, toDate)).thenReturn(transactionsDto);

    given()
      .when().get("/bank/transactions/12?fromDate=2023-01-01&toDate=2023-04-01")
      .then()
      .statusCode(200)
      .body(is("{\"transactionsyBank\":[{\"bankAccountSource\":\"Bank 42\",\"transactions\":[{\"type\":\"CREDIT\",\"amountInCents\":120,\"text\":\"Amazon\"},{\"type\":\"DEBIT\",\"amountInCents\":-90,\"text\":\"Google\"}]}]}"));
  }

  @Test
  public void testPrintTransactionsInvalidAccountId() {
    given()
      .when().get("/bank/transactions/invalidId")
      .then()
      .statusCode(404);
  }

  @Test
  public void testPrintTransactionsApiException() {
    Long accountId = 43L;
    Mockito.when(bankService.getBankTransactions(accountId, null, null)).thenThrow(new ApiException("Test exception", 423));

    given()
      .when().get("/bank/transactions/43")
      .then()
      .statusCode(423);
  }

  @Test
  public void testPrintTransactionsUnhandledException() {
    Long accountId = 43L;
    Mockito.when(bankService.getBankTransactions(accountId, null, null)).thenThrow(new RuntimeException());

    given()
      .when().get("/bank/transactions/43")
      .then()
      .statusCode(500);
  }
}