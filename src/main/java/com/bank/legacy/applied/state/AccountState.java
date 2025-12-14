package com.bank.legacy.applied.state;

/**
 * Interface State pour les états d'un compte bancaire
 */
public interface AccountState {

    /**
     * Vérifie si le dépôt est autorisé dans cet état
     */
    boolean canDeposit();

    /**
     * Vérifie si le retrait est autorisé dans cet état
     */
    boolean canWithdraw();

    /**
     * Vérifie si le virement est autorisé dans cet état
     */
    boolean canTransfer();

    /**
     * Retourne la limite de retrait dans cet état
     */
    double getWithdrawalLimit();

    /**
     * Retourne le nom de l'état
     */
    String getStateName();

    /**
     * Gère les transitions d'état
     * @param account le compte dont l'état change
     * @param action l'action demandée (SUSPEND, FREEZE, ACTIVATE, UNFREEZE, CLOSE)
     */
    void handleStateTransition(StatefulBankAccount account, String action);
}
