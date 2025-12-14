package com.bank.legacy.applied.chain;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Interface Handler pour la chaîne de validation des transactions
 */
public interface TransactionValidator {

    /**
     * Définit le validateur suivant dans la chaîne
     */
    void setNext(TransactionValidator next);

    /**
     * Valide la transaction et passe au suivant si OK
     */
    ValidationResult validate(Transaction transaction, BankingService service);
}
