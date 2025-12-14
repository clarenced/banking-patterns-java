package com.bank.legacy.applied.chain;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Validateur du montant de la transaction
 */
public class AmountValidator extends AbstractTransactionValidator {

    private static final double MAX_AMOUNT = 50000.0;

    public AmountValidator() {
        super("AmountValidator");
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        logValidation("Vérification du montant: " + transaction.getAmount() + " EUR");

        if (transaction.getAmount() <= 0) {
            logValidation("ÉCHEC - Montant invalide (doit être positif)");
            return ValidationResult.failure("Le montant doit être positif");
        }

        if (transaction.getAmount() > MAX_AMOUNT) {
            logValidation("ÉCHEC - Montant trop élevé (max " + MAX_AMOUNT + " EUR)");
            return ValidationResult.failure("Le montant dépasse la limite maximale de " + MAX_AMOUNT + " EUR");
        }

        logValidation("OK - Montant valide");
        return validateNext(transaction, service);
    }
}
