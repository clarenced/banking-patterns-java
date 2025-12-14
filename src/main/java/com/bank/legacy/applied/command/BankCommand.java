package com.bank.legacy.applied.command;

/**
 * Interface Command pour les opérations bancaires
 * Permet l'exécution, l'annulation et la description des commandes
 */
public interface BankCommand {

    /**
     * Exécute la commande
     * @return true si l'exécution a réussi, false sinon
     */
    boolean execute();

    /**
     * Annule la commande (undo)
     * @return true si l'annulation a réussi, false sinon
     */
    boolean undo();

    /**
     * Retourne une description de la commande
     * @return description textuelle de la commande
     */
    String getDescription();
}
