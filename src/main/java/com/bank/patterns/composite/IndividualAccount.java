package com.bank.patterns.composite;

import com.bank.legacy.BankAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Leaf - Représente un compte individuel (feuille dans l'arbre)
 */
public class IndividualAccount implements AccountComponent {

    private final BankAccount account;

    public IndividualAccount(BankAccount account) {
        this.account = account;
    }

    public IndividualAccount(String accountNumber, String accountType, String customerName,
                             String customerEmail, String customerPhone, double initialDeposit,
                             double interestRate, double overdraftLimit) {
        this.account = new BankAccount(accountNumber, accountType, customerName,
                                       customerEmail, customerPhone, initialDeposit,
                                       interestRate, overdraftLimit);
    }

    @Override
    public double getBalance() {
        return account.getBalance();
    }

    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("ERREUR: Montant invalide pour " + account.getAccountNumber());
            return false;
        }
        account.setBalance(account.getBalance() + amount);
        System.out.println("Dépôt de " + amount + " EUR sur " + account.getAccountNumber());
        return true;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("ERREUR: Montant invalide pour " + account.getAccountNumber());
            return false;
        }
        if (account.getBalance() - amount < -account.getOverdraftLimit()) {
            System.out.println("ERREUR: Solde insuffisant sur " + account.getAccountNumber());
            return false;
        }
        account.setBalance(account.getBalance() - amount);
        System.out.println("Retrait de " + amount + " EUR sur " + account.getAccountNumber());
        return true;
    }

    @Override
    public String getAccountInfo() {
        return String.format("Compte %s (%s) - %s - Solde: %.2f EUR",
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getCustomerName(),
                            account.getBalance());
    }

    @Override
    public void addChild(AccountComponent component) {
        throw new UnsupportedOperationException("Impossible d'ajouter un enfant à un compte individuel");
    }

    @Override
    public void removeChild(AccountComponent component) {
        throw new UnsupportedOperationException("Impossible de retirer un enfant d'un compte individuel");
    }

    @Override
    public List<AccountComponent> getChildren() {
        return new ArrayList<>(); // Liste vide pour une feuille
    }

    @Override
    public String getName() {
        return account.getAccountNumber();
    }

    public BankAccount getUnderlyingAccount() {
        return account;
    }
}
