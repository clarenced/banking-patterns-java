package com.bank.patterns.observer;

/**
 * Interface Subject (Observable)
 *
 * Définit le contrat pour les objets qui peuvent être observés
 * et qui notifient les observateurs des changements d'état
 */
public interface Subject {

    /**
     * Ajoute un observateur à la liste des observateurs
     * @param observer l'observateur à ajouter
     */
    void attach(Observer observer);

    /**
     * Retire un observateur de la liste des observateurs
     * @param observer l'observateur à retirer
     */
    void detach(Observer observer);

    /**
     * Notifie tous les observateurs d'un événement
     * @param event l'événement à notifier
     */
    void notifyObservers(TransferEvent event);
}
