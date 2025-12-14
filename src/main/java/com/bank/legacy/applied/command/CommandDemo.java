package com.bank.legacy.applied.command;

import com.bank.legacy.old.BankAccount;

/**
 * Démonstration du pattern Command avec undo/redo
 */
public class CommandDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("DÉMONSTRATION DU PATTERN COMMAND");
        System.out.println("=".repeat(60));

        // Création des comptes
        BankAccount compte1 = new BankAccount("ACC001", "COURANT", "Jean Dupont",
            "jean@email.com", "+33600000001", 1000.0, 0.0, 500.0);
        BankAccount compte2 = new BankAccount("ACC002", "EPARGNE", "Marie Martin",
            "marie@email.com", "+33600000002", 2000.0, 2.5, 0.0);

        // Création de l'exécuteur de transactions
        TransactionExecutor executor = new TransactionExecutor();

        // Affichage des soldes initiaux
        System.out.println("\n--- Soldes initiaux ---");
        System.out.println("Compte 1 (Jean): " + compte1.getBalance() + " EUR");
        System.out.println("Compte 2 (Marie): " + compte2.getBalance() + " EUR");

        // Création et exécution de commandes
        BankCommand depot1 = new DepositCommand(compte1, 500.0);
        BankCommand retrait1 = new WithdrawCommand(compte1, 200.0);
        BankCommand virement1 = new TransferCommand(compte1, compte2, 300.0);

        // Exécution des commandes
        executor.execute(depot1);
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");

        executor.execute(retrait1);
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");

        executor.execute(virement1);
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");
        System.out.println("Solde Compte 2: " + compte2.getBalance() + " EUR");

        // Affichage de l'historique
        executor.printHistory();

        // Démonstration du UNDO
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DÉMONSTRATION UNDO");
        System.out.println("=".repeat(60));

        executor.undo(); // Annule le virement
        System.out.println("Après annulation du virement:");
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");
        System.out.println("Solde Compte 2: " + compte2.getBalance() + " EUR");

        executor.undo(); // Annule le retrait
        System.out.println("Après annulation du retrait:");
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");

        // Démonstration du REDO
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DÉMONSTRATION REDO");
        System.out.println("=".repeat(60));

        executor.redo(); // Rejoue le retrait
        System.out.println("Après redo du retrait:");
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");

        executor.redo(); // Rejoue le virement
        System.out.println("Après redo du virement:");
        System.out.println("Solde Compte 1: " + compte1.getBalance() + " EUR");
        System.out.println("Solde Compte 2: " + compte2.getBalance() + " EUR");

        // Historique final
        executor.printHistory();

        // Test d'une commande échouée
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST D'UNE COMMANDE ÉCHOUÉE");
        System.out.println("=".repeat(60));

        BankCommand retraitEchoue = new WithdrawCommand(compte1, 50000.0);
        executor.execute(retraitEchoue);
        System.out.println("Le retrait excessif a été refusé");
        System.out.println("Solde Compte 1 inchangé: " + compte1.getBalance() + " EUR");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("FIN DE LA DÉMONSTRATION");
        System.out.println("=".repeat(60));
    }
}
