package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Interface Observer pour les événements de transaction
 */
public interface TransactionObserver {

    /**
     * Méthode appelée lorsqu'une transaction est complétée
     * @param transaction la transaction complétée
     * @param service le service bancaire (pour accéder aux comptes)
     */
    void onTransactionCompleted(Transaction transaction, BankingService service);
}
