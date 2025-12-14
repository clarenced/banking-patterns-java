package com.bank.patterns.chain;

import com.bank.legacy.BankingService;
import com.bank.legacy.Transaction;

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
