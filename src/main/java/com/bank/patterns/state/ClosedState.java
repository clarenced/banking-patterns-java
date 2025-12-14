package com.bank.patterns.state;

/**
 * État Fermé - Compte fermé définitivement, aucune opération ni transition possible
 */
public class ClosedState implements AccountState {

    @Override
    public boolean canDeposit() {
        return false;
    }

    @Override
    public boolean canWithdraw() {
        return false;
    }

    @Override
    public boolean canTransfer() {
        return false;
    }

    @Override
    public double getWithdrawalLimit() {
        return 0;
    }

    @Override
    public String getStateName() {
        return "CLOSED";
    }

    @Override
    public void handleStateTransition(StatefulBankAccount account, String action) {
        System.out.println("ERREUR: Le compte " + account.getAccountNumber() +
                          " est fermé. Aucune transition possible.");
    }
}
