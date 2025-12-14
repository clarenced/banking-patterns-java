package com.bank.patterns.state;

/**
 * État Gelé - Aucune opération autorisée
 */
public class FrozenState implements AccountState {

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
        return "FROZEN";
    }

    @Override
    public void handleStateTransition(StatefulBankAccount account, String action) {
        switch (action.toUpperCase()) {
            case "UNFREEZE":
            case "ACTIVATE":
                account.setState(new ActiveState());
                System.out.println("Compte " + account.getAccountNumber() + " dégelé et réactivé");
                break;
            case "CLOSE":
                account.setState(new ClosedState());
                System.out.println("Compte " + account.getAccountNumber() + " fermé définitivement");
                break;
            case "FREEZE":
                System.out.println("Le compte est déjà gelé");
                break;
            default:
                System.out.println("Action non reconnue pour l'état FROZEN: " + action);
        }
    }
}
