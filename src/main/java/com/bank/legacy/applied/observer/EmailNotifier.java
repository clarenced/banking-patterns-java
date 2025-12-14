package com.bank.legacy.applied.observer;

/**
 * Observer concret - Notification par Email
 *
 * Envoie une notification par email lorsqu'un virement est reçu
 */
public class EmailNotifier implements Observer {
    private final String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void update(TransferEvent event) {
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│ 📧 NOTIFICATION EMAIL                               │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ À: " + emailAddress);
        System.out.println("│ Sujet: Virement reçu");
        System.out.println("│");
        System.out.println("│ Bonjour,");
        System.out.println("│");
        System.out.printf("│ Vous avez reçu un virement de %.2f %s%n",
                event.getAmount(), event.getCurrency());
        System.out.println("│ De: " + event.getSenderName());
        System.out.println("│ Référence: " + event.getReference());
        System.out.println("│ Compte: " + event.getAccountNumber());
        System.out.println("│");
        System.out.println("│ Cordialement,");
        System.out.println("│ Votre Banque");
        System.out.println("└─────────────────────────────────────────────────────┘");
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
