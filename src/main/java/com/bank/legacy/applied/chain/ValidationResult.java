package com.bank.legacy.applied.chain;

/**
 * Résultat d'une validation dans la chaîne
 */
public class ValidationResult {

    private final boolean valid;
    private final String errorMessage;

    private ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    /**
     * Crée un résultat de succès
     */
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    /**
     * Crée un résultat d'échec avec un message d'erreur
     */
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult[VALID]";
        }
        return "ValidationResult[INVALID: " + errorMessage + "]";
    }
}
