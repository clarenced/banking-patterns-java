package com.bank.legacy;

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

    public Transaction(String transactionId, String type, double amount,
                      String sourceAccount, String destinationAccount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.transactionDate = new Date();
        this.status = "PENDING";
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
}
