package com.bank.patterns.observer;

import com.bank.legacy.BankingService;
import com.bank.legacy.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject (Observable) pour les notifications de transactions
 */
public class TransactionSubject {

    private final List<TransactionObserver> observers = new ArrayList<>();

    /**
     * Ajoute un observateur
     */
    public void attach(TransactionObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[TransactionSubject] Observateur ajouté: " + observer.getClass().getSimpleName());
        }
    }

    /**
     * Retire un observateur
     */
    public void detach(TransactionObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("[TransactionSubject] Observateur retiré: " + observer.getClass().getSimpleName());
        }
    }

    /**
     * Notifie tous les observateurs d'une transaction complétée
     * Un observateur peut échouer sans bloquer les autres
     */
    public void notifyObservers(Transaction transaction, BankingService service) {
        System.out.println("[TransactionSubject] Notification de " + observers.size() + " observateur(s)...");

        for (TransactionObserver observer : observers) {
            try {
                observer.onTransactionCompleted(transaction, service);
            } catch (Exception e) {
                System.err.println("[TransactionSubject] Erreur dans " +
                    observer.getClass().getSimpleName() + ": " + e.getMessage());
                // Continue avec les autres observateurs
            }
        }
    }

    /**
     * Retourne le nombre d'observateurs
     */
    public int getObserverCount() {
        return observers.size();
    }
}
