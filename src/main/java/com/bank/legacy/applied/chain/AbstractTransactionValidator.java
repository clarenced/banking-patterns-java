package com.bank.legacy.applied.chain;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Classe abstraite de base pour les validateurs de transaction
 */
public abstract class AbstractTransactionValidator implements TransactionValidator {

    protected TransactionValidator next;
    protected final String validatorName;

    protected AbstractTransactionValidator(String validatorName) {
        this.validatorName = validatorName;
    }

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    /**
     * Passe la validation au prochain validateur dans la chaîne
     * @return ValidationResult.success() s'il n'y a pas de suivant
     */
    protected ValidationResult validateNext(Transaction transaction, BankingService service) {
        if (next != null) {
            return next.validate(transaction, service);
        }
        return ValidationResult.success();
    }

    /**
     * Log la validation en cours
     */
    protected void logValidation(String message) {
        System.out.println("[" + validatorName + "] " + message);
    }
}
