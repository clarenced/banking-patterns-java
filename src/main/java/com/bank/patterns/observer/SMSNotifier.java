package com.bank.patterns.observer;

/**
 * Observer concret - Notification par SMS
 *
 * Envoie une notification par SMS lorsqu'un virement est reçu
 */
public class SMSNotifier implements Observer {
    private final String phoneNumber;

    public SMSNotifier(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(TransferEvent event) {
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│ 📱 NOTIFICATION SMS                                 │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ À: " + phoneNumber);
        System.out.println("│");
        System.out.printf("│ Virement reçu: %.2f %s de %s%n",
                event.getAmount(), event.getCurrency(), event.getSenderName());
        System.out.println("│ Ref: " + event.getReference());
        System.out.println("└─────────────────────────────────────────────────────┘");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
