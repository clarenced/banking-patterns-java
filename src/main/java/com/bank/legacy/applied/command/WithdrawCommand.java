package com.bank.legacy.applied.command;

import com.bank.legacy.old.BankAccount;

/**
 * Commande concrète pour effectuer un retrait
 */
public class WithdrawCommand implements BankCommand {

    private final BankAccount account;
    private final double amount;
    private boolean executed;

    public WithdrawCommand(BankAccount account, double amount) {
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

        // Vérification du solde avec découvert autorisé
        if (account.getBalance() - amount < -account.getOverdraftLimit()) {
            System.out.println("ERREUR: Solde insuffisant (découvert dépassé)");
            return false;
        }

        account.setBalance(account.getBalance() - amount);
        executed = true;
        System.out.println("Retrait de " + amount + " EUR effectué sur le compte " + account.getAccountNumber());
        return true;
    }

    @Override
    public boolean undo() {
        if (!executed) {
            System.out.println("ERREUR: Impossible d'annuler - la commande n'a pas été exécutée");
            return false;
        }

        account.setBalance(account.getBalance() + amount);
        executed = false;
        System.out.println("Annulation du retrait de " + amount + " EUR sur le compte " + account.getAccountNumber());
        return true;
    }

    @Override
    public String getDescription() {
        return "Retrait de " + amount + " EUR sur le compte " + account.getAccountNumber();
    }

    public boolean isExecuted() {
        return executed;
    }
}
