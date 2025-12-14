package com.bank.legacy.applied.composite;

/**
 * Démonstration du pattern Composite pour la gestion de portefeuilles
 */
public class CompositeDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("DÉMONSTRATION DU PATTERN COMPOSITE");
        System.out.println("=".repeat(60));

        // Création de comptes individuels
        IndividualAccount compteJean = new IndividualAccount(
            "ACC001", "COURANT", "Jean Dupont",
            "jean@email.com", "+33600000001", 1000.0, 0.0, 500.0);

        IndividualAccount compteMarie = new IndividualAccount(
            "ACC002", "EPARGNE", "Marie Dupont",
            "marie@email.com", "+33600000002", 3000.0, 2.5, 0.0);

        IndividualAccount comptePierre = new IndividualAccount(
            "ACC003", "COURANT", "Pierre Martin",
            "pierre@email.com", "+33600000003", 500.0, 0.0, 200.0);

        IndividualAccount compteSophie = new IndividualAccount(
            "ACC004", "EPARGNE", "Sophie Martin",
            "sophie@email.com", "+33600000004", 1500.0, 2.5, 0.0);

        // Création du portefeuille familial Dupont
        System.out.println("\n--- Création du portefeuille familial Dupont ---");
        AccountPortfolio familleD = new AccountPortfolio("Famille Dupont");
        familleD.addChild(compteJean);
        familleD.addChild(compteMarie);

        // Création du portefeuille familial Martin
        System.out.println("\n--- Création du portefeuille familial Martin ---");
        AccountPortfolio familleM = new AccountPortfolio("Famille Martin");
        familleM.addChild(comptePierre);
        familleM.addChild(compteSophie);

        // Création d'un portefeuille entreprise contenant les deux familles
        System.out.println("\n--- Création du portefeuille Entreprise ---");
        AccountPortfolio entreprise = new AccountPortfolio("BankCorp Holdings");
        entreprise.addChild(familleD);
        entreprise.addChild(familleM);

        // Ajout d'un compte corporate direct
        IndividualAccount compteCorporate = new IndividualAccount(
            "ACC005", "PROFESSIONNEL", "BankCorp SA",
            "contact@bankcorp.com", "+33100000000", 50000.0, 0.5, 10000.0);
        entreprise.addChild(compteCorporate);

        // Affichage de la structure hiérarchique
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STRUCTURE HIÉRARCHIQUE");
        System.out.println("=".repeat(60));
        System.out.println(entreprise.getAccountInfo());

        // Opérations sur un compte individuel
        System.out.println("=".repeat(60));
        System.out.println("OPÉRATIONS SUR COMPTE INDIVIDUEL");
        System.out.println("=".repeat(60));
        System.out.println("Solde de Jean avant: " + compteJean.getBalance() + " EUR");
        compteJean.deposit(500);
        System.out.println("Solde de Jean après: " + compteJean.getBalance() + " EUR");

        // Opérations sur un portefeuille familial
        System.out.println("\n" + "=".repeat(60));
        System.out.println("OPÉRATIONS SUR PORTEFEUILLE FAMILIAL");
        System.out.println("=".repeat(60));
        System.out.println("Solde famille Dupont avant: " + familleD.getBalance() + " EUR");
        familleD.deposit(1000); // Répartition équitable
        System.out.println("Solde famille Dupont après: " + familleD.getBalance() + " EUR");
        System.out.println("  - Jean: " + compteJean.getBalance() + " EUR");
        System.out.println("  - Marie: " + compteMarie.getBalance() + " EUR");

        // Retrait proportionnel
        System.out.println("\n--- Retrait proportionnel de 1000 EUR ---");
        familleD.withdraw(1000);
        System.out.println("Solde famille Dupont après retrait: " + familleD.getBalance() + " EUR");
        System.out.println("  - Jean: " + compteJean.getBalance() + " EUR");
        System.out.println("  - Marie: " + compteMarie.getBalance() + " EUR");

        // Opérations sur le portefeuille entreprise (multi-niveau)
        System.out.println("\n" + "=".repeat(60));
        System.out.println("OPÉRATIONS SUR PORTEFEUILLE ENTREPRISE (MULTI-NIVEAU)");
        System.out.println("=".repeat(60));
        System.out.println("Solde total entreprise: " + entreprise.getBalance() + " EUR");
        System.out.println("Nombre de comptes: " + entreprise.getAccountCount());

        System.out.println("\n--- Dépôt de 10000 EUR sur l'ensemble ---");
        entreprise.deposit(10000);
        System.out.println("\nSolde total entreprise après dépôt: " + entreprise.getBalance() + " EUR");

        // Affichage final de la structure
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STRUCTURE FINALE");
        System.out.println("=".repeat(60));
        System.out.println(entreprise.getAccountInfo());

        // Test d'opération impossible
        System.out.println("=".repeat(60));
        System.out.println("TEST D'OPÉRATION IMPOSSIBLE");
        System.out.println("=".repeat(60));
        try {
            compteJean.addChild(compteMarie); // Devrait lancer une exception
        } catch (UnsupportedOperationException e) {
            System.out.println("Exception attendue: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("FIN DE LA DÉMONSTRATION");
        System.out.println("=".repeat(60));
    }
}
