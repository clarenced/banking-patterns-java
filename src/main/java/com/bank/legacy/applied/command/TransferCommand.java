package com.bank.legacy.applied.command;

import com.bank.legacy.old.BankAccount;

/**
 * Macro Command pour effectuer un virement (composition de WithdrawCommand et DepositCommand)
 */
public class TransferCommand implements BankCommand {

    private final WithdrawCommand withdrawCommand;
    private final DepositCommand depositCommand;
    private final BankAccount sourceAccount;
    private final BankAccount destinationAccount;
    private final double amount;

    public TransferCommand(BankAccount sourceAccount, BankAccount destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.withdrawCommand = new WithdrawCommand(sourceAccount, amount);
        this.depositCommand = new DepositCommand(destinationAccount, amount);
    }

    @Override
    public boolean execute() {
        System.out.println("Début du virement de " + amount + " EUR de " +
                          sourceAccount.getAccountNumber() + " vers " + destinationAccount.getAccountNumber());

        // Exécuter le retrait d'abord
        if (!withdrawCommand.execute()) {
            System.out.println("ERREUR: Échec du retrait - virement annulé");
            return false;
        }

        // Puis le dépôt
        if (!depositCommand.execute()) {
            System.out.println("ERREUR: Échec du dépôt - annulation du retrait");
            withdrawCommand.undo();
            return false;
        }

        System.out.println("Virement effectué avec succès");
        return true;
    }

    @Override
    public boolean undo() {
        System.out.println("Annulation du virement de " + amount + " EUR");

        // Annuler dans l'ordre inverse : d'abord le dépôt, puis le retrait
        boolean depositUndone = depositCommand.undo();
        boolean withdrawUndone = withdrawCommand.undo();

        if (depositUndone && withdrawUndone) {
            System.out.println("Virement annulé avec succès");
            return true;
        }

        System.out.println("ERREUR: Problème lors de l'annulation du virement");
        return false;
    }

    @Override
    public String getDescription() {
        return "Virement de " + amount + " EUR de " + sourceAccount.getAccountNumber() +
               " vers " + destinationAccount.getAccountNumber();
    }
}
