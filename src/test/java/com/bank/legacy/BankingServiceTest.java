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

    // ========== TESTS POUR LES FRAIS ==========

    @Test
    public void testCurrentAccountWithdrawalFeesUnder1000() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 2000.0);
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 500.0);

        // Retrait < 1000 EUR : pas de frais
        // 2000 - 500 = 1500
        assertEquals(1500.0, account.getBalance());
    }

    @Test
    public void testCurrentAccountWithdrawalFeesOver1000() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 3000.0);
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 1500.0);

        // Retrait > 1000 EUR : frais de 2.50 EUR
        // 3000 - 1500 - 2.50 = 1497.50
        assertEquals(1497.50, account.getBalance());
    }

    @Test
    public void testSavingsAccountWithdrawalFees() {
        BankAccount account = service.createAccount("EPARGNE", "Test User", "test@email.fr", "0600000000", 2000.0);
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 500.0);

        // Retrait épargne : frais fixe de 1.00 EUR
        // 2000 - 500 - 1.00 = 1499.00
        assertEquals(1499.0, account.getBalance());
    }

    @Test
    public void testBusinessAccountWithdrawalFeesUnder5000() {
        BankAccount account = service.createAccount("PROFESSIONNEL", "Test Company", "test@company.fr", "0600000000", 10000.0);
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 3000.0);

        // Retrait < 5000 EUR : frais de 2.00 EUR
        // 10000 - 3000 - 2.00 = 6998.00
        assertEquals(6998.0, account.getBalance());
    }

    @Test
    public void testBusinessAccountWithdrawalFeesOver5000() {
        BankAccount account = service.createAccount("PROFESSIONNEL", "Test Company", "test@company.fr", "0600000000", 10000.0);
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 6000.0);

        // Retrait > 5000 EUR : frais de 5.00 EUR
        // 10000 - 6000 - 5.00 = 3995.00
        assertEquals(3995.0, account.getBalance());
    }

    @Test
    public void testCurrentAccountTransferFees() {
        BankAccount source = service.createAccount("COURANT", "User 1", "user1@email.fr", "0600000001", 2000.0);
        BankAccount destination = service.createAccount("COURANT", "User 2", "user2@email.fr", "0600000002", 500.0);

        service.processTransaction("VIREMENT", source.getAccountNumber(), destination.getAccountNumber(), 500.0);

        // Virement compte courant : frais de 1.00 EUR
        // Source: 2000 - 500 - 1.00 = 1499.00
        assertEquals(1499.0, source.getBalance());
        // Destination: 500 + 500 = 1000
        assertEquals(1000.0, destination.getBalance());
    }

    @Test
    public void testSavingsAccountTransferFees() {
        BankAccount source = service.createAccount("EPARGNE", "User 1", "user1@email.fr", "0600000001", 3000.0);
        BankAccount destination = service.createAccount("COURANT", "User 2", "user2@email.fr", "0600000002", 500.0);

        service.processTransaction("VIREMENT", source.getAccountNumber(), destination.getAccountNumber(), 1000.0);

        // Virement depuis épargne : frais élevés de 2.50 EUR
        // Source: 3000 - 1000 - 2.50 = 1997.50
        assertEquals(1997.50, source.getBalance());
        // Destination: 500 + 1000 = 1500
        assertEquals(1500.0, destination.getBalance());
    }

    @Test
    public void testBusinessAccountTransferFees() {
        BankAccount source = service.createAccount("PROFESSIONNEL", "Company", "company@email.fr", "0600000001", 5000.0);
        BankAccount destination = service.createAccount("COURANT", "User 2", "user2@email.fr", "0600000002", 500.0);

        service.processTransaction("VIREMENT", source.getAccountNumber(), destination.getAccountNumber(), 1000.0);

        // Virement compte professionnel : frais réduits de 0.50 EUR
        // Source: 5000 - 1000 - 0.50 = 3999.50
        assertEquals(3999.50, source.getBalance());
        // Destination: 500 + 1000 = 1500
        assertEquals(1500.0, destination.getBalance());
    }

    @Test
    public void testMultipleWithdrawalsAccumulateFees() {
        BankAccount account = service.createAccount("COURANT", "Test User", "test@email.fr", "0600000000", 5000.0);

        // Premier retrait > 1000 : frais 2.50
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 1200.0);
        assertEquals(3797.50, account.getBalance()); // 5000 - 1200 - 2.50

        // Deuxième retrait > 1000 : frais 2.50
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 1100.0);
        assertEquals(2695.0, account.getBalance()); // 3797.50 - 1100 - 2.50
    }

    @Test
    public void testSavingsAccountMultipleWithdrawalsFees() {
        BankAccount account = service.createAccount("EPARGNE", "Test User", "test@email.fr", "0600000000", 3000.0);

        // Premier retrait : frais 1.00
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 500.0);
        assertEquals(2499.0, account.getBalance()); // 3000 - 500 - 1.00

        // Deuxième retrait : frais 1.00
        service.processTransaction("RETRAIT", account.getAccountNumber(), null, 300.0);
        assertEquals(2198.0, account.getBalance()); // 2499 - 300 - 1.00
    }
}
