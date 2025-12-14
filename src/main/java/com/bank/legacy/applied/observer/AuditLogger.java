package com.bank.legacy.applied.observer;

import java.time.format.DateTimeFormatter;

/**
 * Observer concret - Journalisation d'audit
 *
 * Enregistre les virements dans le journal d'audit pour conformité réglementaire
 */
public class AuditLogger implements Observer {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void update(TransferEvent event) {
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│ 📋 JOURNAL D'AUDIT                                  │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ [" + event.getTimestamp().format(FORMATTER) + "] TRANSFER_RECEIVED");
        System.out.println("│ Account: " + event.getAccountNumber());
        System.out.printf("│ Amount: %.2f %s%n", event.getAmount(), event.getCurrency());
        System.out.println("│ Sender: " + event.getSenderName());
        System.out.println("│ Reference: " + event.getReference());
        System.out.println("│ Status: LOGGED");
        System.out.println("└─────────────────────────────────────────────────────┘");
    }
}
