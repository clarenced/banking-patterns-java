package com.bank.legacy.applied.observer;

import java.time.LocalDateTime;

/**
 * Classe représentant un événement de virement
 *
 * Contient toutes les informations relatives à un virement reçu
 */
public class TransferEvent {
    private final String accountNumber;
    private final double amount;
    private final String currency;
    private final String senderName;
    private final String reference;
    private final LocalDateTime timestamp;

    public TransferEvent(String accountNumber, double amount, String currency,
                        String senderName, String reference) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
        this.senderName = senderName;
        this.reference = reference;
        this.timestamp = LocalDateTime.now();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Virement de %.2f %s de %s (Ref: %s) reçu le %s",
                amount, currency, senderName, reference, timestamp);
    }
}
