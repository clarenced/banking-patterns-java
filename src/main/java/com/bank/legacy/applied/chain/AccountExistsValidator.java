package com.bank.legacy.applied.chain;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Validateur de l'existence des comptes
 */
public class AccountExistsValidator extends AbstractTransactionValidator {

    public AccountExistsValidator() {
        super("AccountExistsValidator");
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        String sourceAccount = transaction.getSourceAccount();
        String destAccount = transaction.getDestinationAccount();

        logValidation("Vérification de l'existence des comptes");

        // Vérification du compte source (si spécifié)
        if (sourceAccount != null && !sourceAccount.isEmpty()) {
            if (service.getAccount(sourceAccount) == null) {
                logValidation("ÉCHEC - Compte source introuvable: " + sourceAccount);
                return ValidationResult.failure("Compte source introuvable: " + sourceAccount);
            }
            logValidation("OK - Compte source trouvé: " + sourceAccount);
        }

        // Vérification du compte destination (si spécifié)
        if (destAccount != null && !destAccount.isEmpty()) {
            if (service.getAccount(destAccount) == null) {
                logValidation("ÉCHEC - Compte destination introuvable: " + destAccount);
                return ValidationResult.failure("Compte destination introuvable: " + destAccount);
            }
            logValidation("OK - Compte destination trouvé: " + destAccount);
        }

        return validateNext(transaction, service);
    }
}
