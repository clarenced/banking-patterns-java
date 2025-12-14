package com.bank.legacy.applied.state;

/**
 * Démonstration du pattern State pour la gestion des états d'un compte
 */
public class StateDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("DÉMONSTRATION DU PATTERN STATE");
        System.out.println("=".repeat(60));

        // Création d'un compte avec état
        StatefulBankAccount compte = new StatefulBankAccount(
            "ACC001", "COURANT", "Jean Dupont",
            "jean@email.com", "+33600000001", 1000.0, 0.0, 500.0);

        System.out.println("\n--- État initial ---");
        System.out.println(compte);

        // Test des opérations en état ACTIVE
        System.out.println("\n--- Test en état ACTIVE ---");
        compte.deposit(500);
        compte.withdraw(200);

        // Suspension du compte
        System.out.println("\n--- Suspension du compte ---");
        compte.changeState("SUSPEND");
        System.out.println(compte);

        // Test des opérations en état SUSPENDED
        System.out.println("\n--- Test en état SUSPENDED ---");
        compte.deposit(100);  // Devrait fonctionner
        compte.withdraw(300); // Devrait fonctionner (< 500)
        compte.withdraw(600); // Devrait échouer (> limite de 500)

        // Création d'un second compte pour test de virement
        StatefulBankAccount compte2 = new StatefulBankAccount(
            "ACC002", "EPARGNE", "Marie Martin",
            "marie@email.com", "+33600000002", 2000.0, 2.5, 0.0);

        System.out.println("\n--- Test de virement depuis compte suspendu ---");
        compte.transfer(compte2, 100); // Devrait échouer

        // Gel du compte
        System.out.println("\n--- Gel du compte ---");
        compte.changeState("FREEZE");
        System.out.println(compte);

        // Test des opérations en état FROZEN
        System.out.println("\n--- Test en état FROZEN ---");
        compte.deposit(100);  // Devrait échouer
        compte.withdraw(50);  // Devrait échouer

        // Dégel du compte
        System.out.println("\n--- Dégel du compte ---");
        compte.changeState("UNFREEZE");
        System.out.println(compte);

        // Les opérations devraient à nouveau fonctionner
        System.out.println("\n--- Test après dégel ---");
        compte.deposit(100);

        // Fermeture du compte (via freeze d'abord)
        System.out.println("\n--- Fermeture du compte ---");
        compte.changeState("FREEZE");
        compte.changeState("CLOSE");
        System.out.println(compte);

        // Test des opérations sur compte fermé
        System.out.println("\n--- Test en état CLOSED ---");
        compte.deposit(100);  // Devrait échouer
        compte.withdraw(50);  // Devrait échouer
        compte.changeState("ACTIVATE"); // Devrait échouer

        // Démonstration du diagramme de transition
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DIAGRAMME DES TRANSITIONS D'ÉTAT");
        System.out.println("=".repeat(60));
        System.out.println("""

            ACTIVE ──SUSPEND──> SUSPENDED
              │                    │
              └────────FREEZE──────┴──> FROZEN ──CLOSE──> CLOSED
                       │                   │
                       └──────UNFREEZE─────┘

            États et permissions:
            - ACTIVE: Tout autorisé
            - SUSPENDED: Dépôts OK, Retraits limités (500 EUR), Virements NON
            - FROZEN: Rien autorisé
            - CLOSED: Rien autorisé, aucune transition possible
            """);

        System.out.println("=".repeat(60));
        System.out.println("FIN DE LA DÉMONSTRATION");
        System.out.println("=".repeat(60));
    }
}
