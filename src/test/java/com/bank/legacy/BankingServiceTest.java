package com.bank.legacy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests basiques pour le code legacy
 */
public class BankingServiceTest {

    private BankingService service;

    @BeforeEach
    public void setUp() {
        service = new BankingService();
    }

    @Test
    public void testCreateCurrentAccount() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 500.0);
        assertNotNull(account);
        assertEquals("COURANT", account.getAccountType());
        assertEquals(500.0, account.getBalance());
    }

    @Test
    public void testCreateSavingsAccount() {
        BankAccount account = service.createAccount("EPARGNE", "Test User", "test@email.fr", "0600000000", 1000.0);
        assertNotNull(account);
        assertEquals("EPARGNE", account.getAccountType());
        assertEquals(2.5, account.getInterestRate());
    }

    @Test
    public void testDepositTransaction() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 500.0);
        boolean result = service.processTransaction("DEPOT", null, account.getAccountNumber(), 200.0);
        assertTrue(result);
        assertEquals(700.0, account.getBalance());
    }

    @Test
    public void testWithdrawalTransaction() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 500.0);
        boolean result = service.processTransaction("RETRAIT", account.getAccountNumber(), null, 100.0);
        assertTrue(result);
        // Balance devrait être 500 - 100 = 400 (pas de frais pour retrait < 1000)
        assertEquals(400.0, account.getBalance());
    }

    @Test
    public void testInsufficientFunds() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 100.0);
        boolean result = service.processTransaction("RETRAIT", account.getAccountNumber(), null, 1000.0);
        assertFalse(result);
        assertEquals(100.0, account.getBalance()); // Balance inchangée
    }

    @Test
    public void testTransferTransaction() {
        BankAccount source = service.createAccount("COURANT", "User 1", "user1@email.fr", "0600000001", 1000.0);
        BankAccount destination = service.createAccount("COURANT", "User 2", "user2@email.fr", "0600000002", 500.0);

        boolean result = service.processTransaction("VIREMENT", source.getAccountNumber(), destination.getAccountNumber(), 200.0);
        assertTrue(result);

        // Source: 1000 - 200 - 1 (frais) = 799
        assertEquals(799.0, source.getBalance());
        // Destination: 500 + 200 = 700
        assertEquals(700.0, destination.getBalance());
    }
}
