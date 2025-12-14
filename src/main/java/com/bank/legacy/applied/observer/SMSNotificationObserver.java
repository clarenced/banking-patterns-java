package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankAccount;
import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Observateur qui envoie des SMS pour les montants élevés ou les découverts
 */
public class SMSNotificationObserver implements TransactionObserver {

    private static final double HIGH_AMOUNT_THRESHOLD = 1000.0;

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        double amount = transaction.getAmount();

        // Vérification du montant élevé
        if (amount > HIGH_AMOUNT_THRESHOLD) {
            notifyHighAmount(transaction, service);
        }

        // Vérification du découvert après un débit
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            if (source != null && source.getBalance() < 0) {
                notifyOverdraft(source);
            }
        }
    }

    private void notifyHighAmount(Transaction transaction, BankingService service) {
        // Notification au compte source pour les débits importants
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            if (source != null) {
                System.out.println("SMS to " + source.getCustomerPhone() +
                                   ": ALERTE - Transaction de " + transaction.getAmount() +
                                   " EUR effectuée sur votre compte " + source.getAccountNumber());
            }
        }

        // Notification au compte destination pour les crédits importants
        if (transaction.getDestinationAccount() != null) {
            BankAccount dest = service.getAccount(transaction.getDestinationAccount());
            if (dest != null) {
                System.out.println("SMS to " + dest.getCustomerPhone() +
                                   ": Crédit de " + transaction.getAmount() +
                                   " EUR reçu sur votre compte " + dest.getAccountNumber());
            }
        }
    }

    private void notifyOverdraft(BankAccount account) {
        System.out.println("SMS to " + account.getCustomerPhone() +
                          ": ALERTE DÉCOUVERT - Votre compte " + account.getAccountNumber() +
                          " est en découvert (solde: " + account.getBalance() + " EUR)");
    }
}
