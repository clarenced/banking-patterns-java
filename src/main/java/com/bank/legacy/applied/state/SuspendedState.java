package com.bank.legacy.applied.state;

/**
 * État Suspendu - Dépôts autorisés, retraits limités, virements interdits
 */
public class SuspendedState implements AccountState {

    private static final double DAILY_WITHDRAWAL_LIMIT = 500.0;

    @Override
    public boolean canDeposit() {
        return true;
    }

    @Override
    public boolean canWithdraw() {
        return true; // Autorisé mais avec limite
    }

    @Override
    public boolean canTransfer() {
        return false;
    }

    @Override
    public double getWithdrawalLimit() {
        return DAILY_WITHDRAWAL_LIMIT;
    }

    @Override
    public String getStateName() {
        return "SUSPENDED";
    }

    @Override
    public void handleStateTransition(StatefulBankAccount account, String action) {
        switch (action.toUpperCase()) {
            case "ACTIVATE":
                account.setState(new ActiveState());
                System.out.println("Compte " + account.getAccountNumber() + " réactivé");
                break;
            case "FREEZE":
                account.setState(new FrozenState());
                System.out.println("Compte " + account.getAccountNumber() + " gelé");
                break;
            case "SUSPEND":
                System.out.println("Le compte est déjà suspendu");
                break;
            default:
                System.out.println("Action non reconnue pour l'état SUSPENDED: " + action);
        }
    }
}
