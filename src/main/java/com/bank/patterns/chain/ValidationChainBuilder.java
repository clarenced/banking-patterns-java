package com.bank.patterns.chain;

/**
 * Builder pour construire la chaîne de validation
 */
public class ValidationChainBuilder {

    /**
     * Construit la chaîne complète de validation dans l'ordre logique
     * @return le premier validateur de la chaîne
     */
    public static TransactionValidator buildChain() {
        // Création des validateurs
        AmountValidator amountValidator = new AmountValidator();
        AccountExistsValidator accountExistsValidator = new AccountExistsValidator();
        AccountStateValidator accountStateValidator = new AccountStateValidator();
        BalanceValidator balanceValidator = new BalanceValidator();
        DailyLimitValidator dailyLimitValidator = new DailyLimitValidator();
        FraudDetectionValidator fraudDetectionValidator = new FraudDetectionValidator();

        // Chaînage dans l'ordre logique:
        // 1. Montant valide
        // 2. Comptes existent
        // 3. État des comptes OK
        // 4. Solde suffisant
        // 5. Limite quotidienne
        // 6. Détection de fraude
        amountValidator.setNext(accountExistsValidator);
        accountExistsValidator.setNext(accountStateValidator);
        accountStateValidator.setNext(balanceValidator);
        balanceValidator.setNext(dailyLimitValidator);
        dailyLimitValidator.setNext(fraudDetectionValidator);

        return amountValidator;
    }

    /**
     * Construit une chaîne simplifiée (sans fraude detection ni daily limit)
     * Utile pour les tests
     */
    public static TransactionValidator buildSimpleChain() {
        AmountValidator amountValidator = new AmountValidator();
        AccountExistsValidator accountExistsValidator = new AccountExistsValidator();
        BalanceValidator balanceValidator = new BalanceValidator();

        amountValidator.setNext(accountExistsValidator);
        accountExistsValidator.setNext(balanceValidator);

        return amountValidator;
    }

    /**
     * Construit une chaîne personnalisée à partir des validateurs fournis
     */
    public static TransactionValidator buildCustomChain(TransactionValidator... validators) {
        if (validators == null || validators.length == 0) {
            throw new IllegalArgumentException("Au moins un validateur est requis");
        }

        for (int i = 0; i < validators.length - 1; i++) {
            validators[i].setNext(validators[i + 1]);
        }

        return validators[0];
    }
}
