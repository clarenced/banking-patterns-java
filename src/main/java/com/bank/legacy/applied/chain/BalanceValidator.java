package com.bank.legacy.applied.chain;

import com.bank.legacy.old.BankAccount;
import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Validateur du solde suffisant pour les retraits et virements
 */
public class BalanceValidator extends AbstractTransactionValidator {

    public BalanceValidator() {
        super("BalanceValidator");
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        String type = transaction.getType();

        // Le dépôt n'a pas besoin de vérification de solde
        if ("DEPOT".equals(type)) {
            logValidation("Dépôt - pas de vérification de solde nécessaire");
            return validateNext(transaction, service);
        }

        String sourceAccount = transaction.getSourceAccount();
        if (sourceAccount == null || sourceAccount.isEmpty()) {
            return validateNext(transaction, service);
        }

        BankAccount account = service.getAccount(sourceAccount);
        if (account == null) {
            // Déjà vérifié par AccountExistsValidator, mais sécurité
            return validateNext(transaction, service);
        }

        double amount = transaction.getAmount();
        double availableBalance = account.getBalance() + account.getOverdraftLimit();

        logValidation("Vérification du solde: " + account.getBalance() + " EUR" +
                     " (découvert autorisé: " + account.getOverdraftLimit() + " EUR)");

        if (amount > availableBalance) {
            logValidation("ÉCHEC - Solde insuffisant (disponible: " + availableBalance +
                         " EUR, demandé: " + amount + " EUR)");
            return ValidationResult.failure("Solde insuffisant. Disponible: " +
                                           availableBalance + " EUR, demandé: " + amount + " EUR");
        }

        logValidation("OK - Solde suffisant");
        return validateNext(transaction, service);
    }
}
