package com.bank.legacy.applied.composite;

import java.util.List;

/**
 * Interface Component pour le pattern Composite
 * Permet de traiter uniformément les comptes individuels et les portefeuilles
 */
public interface AccountComponent {

    /**
     * Retourne le solde total (récursif pour les portefeuilles)
     */
    double getBalance();

    /**
     * Effectue un dépôt
     * @param amount montant à déposer
     * @return true si le dépôt a réussi
     */
    boolean deposit(double amount);

    /**
     * Effectue un retrait
     * @param amount montant à retirer
     * @return true si le retrait a réussi
     */
    boolean withdraw(double amount);

    /**
     * Retourne les informations sur le compte/portefeuille
     */
    String getAccountInfo();

    /**
     * Ajoute un composant enfant (pour les portefeuilles)
     * @throws UnsupportedOperationException pour les comptes individuels
     */
    void addChild(AccountComponent component);

    /**
     * Retire un composant enfant (pour les portefeuilles)
     * @throws UnsupportedOperationException pour les comptes individuels
     */
    void removeChild(AccountComponent component);

    /**
     * Retourne la liste des enfants
     * @return liste vide pour les comptes individuels
     */
    List<AccountComponent> getChildren();

    /**
     * Retourne le nom du composant
     */
    String getName();
}
