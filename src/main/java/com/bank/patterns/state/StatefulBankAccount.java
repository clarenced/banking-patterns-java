package com.bank.patterns.state;

import com.bank.legacy.BankAccount;

/**
 * Adaptateur pour BankAccount avec gestion d'état via le pattern State
 */
public class StatefulBankAccount {

    private final BankAccount account;
    private AccountState state;

    public StatefulBankAccount(BankAccount account) {
        this.account = account;
        this.state = new ActiveState();
    }

    public StatefulBankAccount(String accountNumber, String accountType, String customerName,
                               String customerEmail, String customerPhone, double initialDeposit,
                               double interestRate, double overdraftLimit) {
        this.account = new BankAccount(accountNumber, accountType, customerName,
                                       customerEmail, customerPhone, initialDeposit,
                                       interestRate, overdraftLimit);
        this.state = new ActiveState();
    }

    /**
     * Effectue un dépôt si l'état le permet
     */
    public boolean deposit(double amount) {
        if (!state.canDeposit()) {
            System.out.println("ERREUR: Dépôt non autorisé - Compte en état " + state.getStateName());
            return false;
        }

        if (amount <= 0) {
            System.out.println("ERREUR: Montant invalide");
            return false;
        }

        account.setBalance(account.getBalance() + amount);
        System.out.println("Dépôt de " + amount + " EUR effectué. Nouveau solde: " + account.getBalance() + " EUR");
        return true;
    }

    /**
     * Effectue un retrait si l'état le permet et respecte la limite
     */
    public boolean withdraw(double amount) {
        if (!state.canWithdraw()) {
            System.out.println("ERREUR: Retrait non autorisé - Compte en état " + state.getStateName());
            return false;
        }

        if (amount <= 0) {
            System.out.println("ERREUR: Montant invalide");
            return false;
        }

        // Vérification de la limite de retrait selon l'état
        if (amount > state.getWithdrawalLimit()) {
            System.out.println("ERREUR: Montant supérieur à la limite de retrait (" +
                              state.getWithdrawalLimit() + " EUR) pour l'état " + state.getStateName());
            return false;
        }

        // Vérification du découvert
        if (account.getBalance() - amount < -account.getOverdraftLimit()) {
            System.out.println("ERREUR: Solde insuffisant (découvert dépassé)");
            return false;
        }

        account.setBalance(account.getBalance() - amount);
        System.out.println("Retrait de " + amount + " EUR effectué. Nouveau solde: " + account.getBalance() + " EUR");
        return true;
    }

    /**
     * Effectue un virement si l'état le permet
     */
    public boolean transfer(StatefulBankAccount destination, double amount) {
        if (!state.canTransfer()) {
            System.out.println("ERREUR: Virement non autorisé - Compte source en état " + state.getStateName());
            return false;
        }

        if (!destination.getState().canDeposit()) {
            System.out.println("ERREUR: Virement non autorisé - Compte destination en état " +
                              destination.getState().getStateName());
            return false;
        }

        // Vérification du découvert
        if (account.getBalance() - amount < -account.getOverdraftLimit()) {
            System.out.println("ERREUR: Solde insuffisant pour le virement");
            return false;
        }

        account.setBalance(account.getBalance() - amount);
        destination.account.setBalance(destination.account.getBalance() + amount);
        System.out.println("Virement de " + amount + " EUR effectué vers " + destination.getAccountNumber());
        return true;
    }

    /**
     * Change l'état du compte selon l'action demandée
     */
    public void changeState(String action) {
        state.handleStateTransition(this, action);
    }

    // Getters et setters
    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    public double getBalance() {
        return account.getBalance();
    }

    public String getCustomerName() {
        return account.getCustomerName();
    }

    public BankAccount getUnderlyingAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "Compte " + account.getAccountNumber() +
               " (" + account.getCustomerName() + ") - État: " + state.getStateName() +
               " - Solde: " + account.getBalance() + " EUR";
    }
}
