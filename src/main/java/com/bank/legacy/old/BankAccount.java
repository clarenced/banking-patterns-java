package com.bank.legacy.old;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe représentant un compte bancaire (CODE LEGACY - À REFACTORER)
 * PROBLÈMES :
 * - Code dupliqué pour chaque type de compte
 * - Logique métier mélangée
 * - Pas de validation cohérente
 * - Couplage fort
 */
public class BankAccount {
    private String accountNumber;
    private String accountType; // "COURANT", "EPARGNE", "PROFESSIONNEL"
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private double balance;
    private double interestRate;
    private double overdraftLimit;
    private Date creationDate;
    private String status; // "ACTIVE", "SUSPENDED", "CLOSED"
    private List<Transaction> transactions; // Liste des transactions

    // Constructeur monolithique avec trop de paramètres
    public BankAccount(String accountNumber, String accountType, String customerName,
                       String customerEmail, String customerPhone, double initialDeposit,
                       double interestRate, double overdraftLimit) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.balance = initialDeposit;
        this.interestRate = interestRate;
        this.overdraftLimit = overdraftLimit;
        this.creationDate = new Date();
        this.status = "ACTIVE";
        this.transactions = new ArrayList<>();
    }

    // Getters et setters basiques
    public String getAccountNumber() { return accountNumber; }
    public String getAccountType() { return accountType; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public double getBalance() { return balance; }
    public double getInterestRate() { return interestRate; }
    public double getOverdraftLimit() { return overdraftLimit; }
    public Date getCreationDate() { return creationDate; }
    public String getStatus() { return status; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setStatus(String status) { this.status = status; }

    // Méthodes pour gérer les transactions
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
