package com.bank.legacy;

/**
 * Classe principale pour tester le code legacy
 */
public class Main {

    public static void main(String[] args) {
        BankingService service = new BankingService();

        System.out.println("=== CRÉATION DE COMPTES ===\n");

        // Création de différents types de comptes
        BankAccount compte1 = service.createAccount("COURANT", "Jean Dupont", "jean.dupont@email.fr", "0601020304", 500.0);
        BankAccount compte2 = service.createAccount("EPARGNE", "Marie Martin", "marie.martin@email.fr", "0605060708", 1000.0);
        BankAccount compte3 = service.createAccount("PROFESSIONNEL", "Entreprise XYZ", "contact@xyz.fr", "0610111213", 5000.0);

        System.out.println("\n=== TRANSACTIONS ===\n");

        // Dépôt
        service.processTransaction("DEPOT", null, compte1.getAccountNumber(), 200.0);

        // Retrait
        service.processTransaction("RETRAIT", compte1.getAccountNumber(), null, 100.0);

        // Virement
        service.processTransaction("VIREMENT", compte1.getAccountNumber(), compte2.getAccountNumber(), 150.0);

        // Transaction qui échoue (solde insuffisant)
        service.processTransaction("RETRAIT", compte1.getAccountNumber(), null, 10000.0);

        System.out.println("\n=== APPLICATION DES INTÉRÊTS ===\n");
        service.applyInterest();

        System.out.println("\n=== RELEVÉ BANCAIRE ===\n");
        System.out.println(service.generateStatement(compte1.getAccountNumber()));
    }
}
