package com.bank.legacy.applied.observer;

/**
 * Démonstration du pattern Observer
 *
 * Scénario: Un compte bancaire reçoit des virements et notifie automatiquement
 * différents observateurs (email, SMS, push, audit)
 */
public class ObserverDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("DÉMONSTRATION DU PATTERN OBSERVER");
        System.out.println("Cas d'usage: Notifications de virements bancaires");
        System.out.println("=".repeat(60));

        // Création d'un compte bancaire (Subject)
        Account account = new Account("FR76 1234 5678 9012 3456", "Jean Dupont", 1000.0);

        System.out.println("\n--- Configuration des observateurs ---");

        // Création des observateurs
        Observer emailNotifier = new EmailNotifier("jean.dupont@email.com");
        Observer smsNotifier = new SMSNotifier("+33 6 12 34 56 78");
        Observer pushNotifier = new PushNotifier("DEVICE-ABC-123");
        Observer auditLogger = new AuditLogger();

        // Abonnement des observateurs au compte
        account.attach(emailNotifier);
        account.attach(smsNotifier);
        account.attach(pushNotifier);
        account.attach(auditLogger);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 1: Réception d'un premier virement");
        System.out.println("=".repeat(60));

        // Réception d'un virement - tous les observateurs seront notifiés
        account.receiveTransfer(500.0, "EUR", "Marie Martin", "VIRT-2024-001");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 2: Désabonnement du service SMS");
        System.out.println("=".repeat(60));

        // Le client se désabonne des notifications SMS
        account.detach(smsNotifier);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 3: Réception d'un deuxième virement");
        System.out.println("=".repeat(60));

        // Nouveau virement - seuls les observateurs restants sont notifiés
        account.receiveTransfer(1200.0, "EUR", "Société ACME", "SALAIRE-MARS-2024");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("RÉSUMÉ");
        System.out.println("=".repeat(60));
        System.out.println("Titulaire: " + account.getAccountHolder());
        System.out.println("Numéro de compte: " + account.getAccountNumber());
        System.out.printf("Solde final: %.2f EUR%n", account.getBalance());
        System.out.println("=".repeat(60));

        System.out.println("\nAvantages du pattern Observer:");
        System.out.println("✓ Couplage faible entre le compte et les notifications");
        System.out.println("✓ Ajout/retrait dynamique d'observateurs");
        System.out.println("✓ Un événement peut déclencher plusieurs actions");
        System.out.println("✓ Facilite l'ajout de nouveaux types de notifications");
    }
}
