package com.bank.legacy.old;

import com.bank.legacy.applied.decorator2.FeeCalculator;

import java.util.Date;

/**
 * Classe représentant une transaction (CODE LEGACY - À REFACTORER)
 */
public class Transaction {
    private String transactionId;
    private String type; // "DEPOT", "RETRAIT", "VIREMENT"
    private double amount;
    private String sourceAccount;
    private String destinationAccount;
    private Date transactionDate;
    private String status; // "PENDING", "COMPLETED", "REJECTED"
    private String rejectionReason;
    private FeeCalculator.Currency currency;

    public Transaction(String transactionId, String type, double amount,
                       String sourceAccount, String destinationAccount, FeeCalculator.Currency currency) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.transactionDate = new Date();
        this.status = "PENDING";
    }

    public Transaction(String transactionId, String type, double amount,
                       String sourceAccount, String destinationAccount) {
        this(transactionId, type, amount, sourceAccount, destinationAccount, FeeCalculator.Currency.EUR);
    }



    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public Date getTransactionDate() { return transactionDate; }
    public String getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }

    public void setStatus(String status) { this.status = status; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public FeeCalculator.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(FeeCalculator.Currency currency) {
        this.currency = currency;
    }

    public boolean weekend() {
        return false;
    }
}
