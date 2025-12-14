package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankAccount;
import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Observateur qui envoie des notifications par email
 */
public class EmailNotificationObserver implements TransactionObserver {

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        String type = transaction.getType();
        double amount = transaction.getAmount();

        // Notification au compte source
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            if (source != null) {
                sendEmail(source.getCustomerEmail(), type, amount, "débit", source.getAccountNumber());
            }
        }

        // Notification au compte destination
        if (transaction.getDestinationAccount() != null) {
            BankAccount dest = service.getAccount(transaction.getDestinationAccount());
            if (dest != null) {
                sendEmail(dest.getCustomerEmail(), type, amount, "crédit", dest.getAccountNumber());
            }
        }
    }

    private void sendEmail(String email, String type, double amount, String operation, String accountNumber) {
        System.out.println("EMAIL to " + email + ": Transaction " + type +
                          " de " + amount + " EUR (" + operation + ") sur compte " + accountNumber + " completed");
    }
}
