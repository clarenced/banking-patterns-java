package com.bank.patterns.command;

import com.bank.legacy.BankAccount;

/**
 * Commande concrète pour effectuer un dépôt
 */
public class DepositCommand implements BankCommand {

    private final BankAccount account;
    private final double amount;
    private boolean executed;

    public DepositCommand(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (amount <= 0) {
            System.out.println("ERREUR: Le montant doit être positif");
            return false;
        }

        account.setBalance(account.getBalance() + amount);
        executed = true;
        System.out.println("Dépôt de " + amount + " EUR effectué sur le compte " + account.getAccountNumber());
        return true;
    }

    @Override
    public boolean undo() {
        if (!executed) {
            System.out.println("ERREUR: Impossible d'annuler - la commande n'a pas été exécutée");
            return false;
        }

        account.setBalance(account.getBalance() - amount);
        executed = false;
        System.out.println("Annulation du dépôt de " + amount + " EUR sur le compte " + account.getAccountNumber());
        return true;
    }

    @Override
    public String getDescription() {
        return "Dépôt de " + amount + " EUR sur le compte " + account.getAccountNumber();
    }

    public boolean isExecuted() {
        return executed;
    }
}
