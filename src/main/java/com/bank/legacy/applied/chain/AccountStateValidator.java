package com.bank.legacy.applied.chain;

import com.bank.legacy.old.BankAccount;
import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Validateur de l'état du compte (utilise le status de BankAccount)
 */
public class AccountStateValidator extends AbstractTransactionValidator {

    public AccountStateValidator() {
        super("AccountStateValidator");
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        String type = transaction.getType();
        String sourceAccount = transaction.getSourceAccount();
        String destAccount = transaction.getDestinationAccount();

        logValidation("Vérification de l'état des comptes");

        // Vérification du compte source pour RETRAIT et VIREMENT
        if (("RETRAIT".equals(type) || "VIREMENT".equals(type)) &&
            sourceAccount != null && !sourceAccount.isEmpty()) {

            BankAccount source = service.getAccount(sourceAccount);
            if (source != null && !"ACTIVE".equals(source.getStatus())) {
                logValidation("ÉCHEC - Compte source non actif: " + source.getStatus());
                return ValidationResult.failure("Compte source non actif (état: " + source.getStatus() + ")");
            }
        }

        // Vérification du compte destination pour DEPOT et VIREMENT
        if (("DEPOT".equals(type) || "VIREMENT".equals(type)) &&
            destAccount != null && !destAccount.isEmpty()) {

            BankAccount dest = service.getAccount(destAccount);
            if (dest != null) {
                String status = dest.getStatus();
                // Pour les dépôts, on peut accepter les comptes SUSPENDED
                if ("CLOSED".equals(status) || "FROZEN".equals(status)) {
                    logValidation("ÉCHEC - Compte destination non disponible: " + status);
                    return ValidationResult.failure("Compte destination non disponible (état: " + status + ")");
                }
            }
        }

        logValidation("OK - État des comptes valide");
        return validateNext(transaction, service);
    }
}
