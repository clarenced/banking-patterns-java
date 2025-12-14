package com.bank.legacy.applied.state;

/**
 * État Actif - Toutes les opérations sont autorisées
 */
public class ActiveState implements AccountState {

    @Override
    public boolean canDeposit() {
        return true;
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public boolean canTransfer() {
        return true;
    }

    @Override
    public double getWithdrawalLimit() {
        return Double.MAX_VALUE; // Pas de limite
    }

    @Override
    public String getStateName() {
        return "ACTIVE";
    }

    @Override
    public void handleStateTransition(StatefulBankAccount account, String action) {
        switch (action.toUpperCase()) {
            case "SUSPEND":
                account.setState(new SuspendedState());
                System.out.println("Compte " + account.getAccountNumber() + " suspendu");
                break;
            case "FREEZE":
                account.setState(new FrozenState());
                System.out.println("Compte " + account.getAccountNumber() + " gelé");
                break;
            case "CLOSE":
                System.out.println("ERREUR: Impossible de fermer directement un compte actif. Gelez-le d'abord.");
                break;
            default:
                System.out.println("Action non reconnue pour l'état ACTIVE: " + action);
        }
    }
}
