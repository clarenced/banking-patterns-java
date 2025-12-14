package com.bank.legacy.old;

import java.util.*;

/**
 * Service bancaire principal (CODE LEGACY HORRIBLE - À REFACTORER)
 * PROBLÈMES :
 * - Méthode géante avec trop de responsabilités
 * - Code dupliqué partout
 * - Logique if/else imbriquée
 * - Pas de séparation des préoccupations
 * - Calculs de frais en dur
 * - Validation dispersée
 */
public class BankingService {

    private Map<String, BankAccount> accounts = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private int accountCounter = 1000;
    private int transactionCounter = 1;

    public BankAccount createAccount(String type, String name, String email, String phone, double initialDeposit) {
        String accountNumber = "ACC" + (accountCounter++);

        BankAccount account = null;

        // CODE DUPLIQUÉ et LOGIQUE EN DUR
        if (type.equals("COURANT")) {
            // Validation manuelle pour compte courant
            if (initialDeposit < 100) {
                System.out.println("ERREUR: Dépôt initial minimum 100 EUR pour compte courant");
                return null;
            }
            account = new BankAccount(accountNumber, "COURANT", name, email, phone, initialDeposit, 0.0, 500.0);
            System.out.println("Compte courant créé: " + accountNumber);
            System.out.println("Email de bienvenue envoyé à: " + email);

        } else if (type.equals("EPARGNE")) {
            // Validation manuelle pour compte épargne
            if (initialDeposit < 500) {
                System.out.println("ERREUR: Dépôt initial minimum 500 EUR pour compte épargne");
                return null;
            }
            account = new BankAccount(accountNumber, "EPARGNE", name, email, phone, initialDeposit, 2.5, 0.0);
            System.out.println("Compte épargne créé: " + accountNumber);
            System.out.println("Email de bienvenue envoyé à: " + email);

        } else if (type.equals("PROFESSIONNEL")) {
            // Validation manuelle pour compte professionnel
            if (initialDeposit < 1000) {
                System.out.println("ERREUR: Dépôt initial minimum 1000 EUR pour compte professionnel");
                return null;
            }
            account = new BankAccount(accountNumber, "PROFESSIONNEL", name, email, phone, initialDeposit, 0.5, 2000.0);
            System.out.println("Compte professionnel créé: " + accountNumber);
            System.out.println("Email de bienvenue envoyé à: " + email);
            System.out.println("SMS envoyé à: " + phone);
        } else {
            System.out.println("ERREUR: Type de compte inconnu");
            return null;
        }

        accounts.put(accountNumber, account);
        return account;
    }

    public boolean processTransaction(String type, String sourceAccount, String destinationAccount, double amount) {
        String txId = "TX" + (transactionCounter++);
        Transaction tx = new Transaction(txId, type, amount, sourceAccount, destinationAccount);

        // VALIDATION HORRIBLE avec IF/ELSE imbriqués
        if (type.equals("DEPOT")) {
            BankAccount account = accounts.get(destinationAccount);
            if (account == null) {
                System.out.println("ERREUR: Compte destination introuvable");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Compte introuvable");
                transactions.add(tx);
                return false;
            }
            if (amount <= 0) {
                System.out.println("ERREUR: Montant invalide");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Montant invalide");
                transactions.add(tx);
                return false;
            }
            if (amount > 10000) {
                System.out.println("ERREUR: Dépôt supérieur à 10000 EUR nécessite une vérification");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Montant trop élevé");
                transactions.add(tx);
                return false;
            }

            account.setBalance(account.getBalance() + amount);
            tx.setStatus("COMPLETED");
            transactions.add(tx);
            System.out.println("Dépôt effectué sur " + destinationAccount);
            System.out.println("Email envoyé à: " + account.getCustomerEmail());
            return true;

        } else if (type.equals("RETRAIT")) {
            BankAccount account = accounts.get(sourceAccount);
            if (account == null) {
                System.out.println("ERREUR: Compte source introuvable");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Compte introuvable");
                transactions.add(tx);
                return false;
            }
            if (amount <= 0) {
                System.out.println("ERREUR: Montant invalide");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Montant invalide");
                transactions.add(tx);
                return false;
            }

            // CALCUL DE FRAIS EN DUR
            double fees = 0;
            if (account.getAccountType().equals("COURANT")) {
                if (amount > 1000) {
                    fees = 2.5;
                }
            } else if (account.getAccountType().equals("EPARGNE")) {
                fees = 1.0; // Frais fixe pour retrait épargne
            } else if (account.getAccountType().equals("PROFESSIONNEL")) {
                if (amount > 5000) {
                    fees = 5.0;
                } else {
                    fees = 2.0;
                }
            }

            double totalAmount = amount + fees;

            // Vérification découvert
            if (account.getBalance() - totalAmount < -account.getOverdraftLimit()) {
                System.out.println("ERREUR: Solde insuffisant (découvert dépassé)");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Solde insuffisant");
                transactions.add(tx);
                return false;
            }

            account.setBalance(account.getBalance() - totalAmount);
            tx.setStatus("COMPLETED");
            transactions.add(tx);
            System.out.println("Retrait effectué sur " + sourceAccount + " (frais: " + fees + " EUR)");
            System.out.println("Email envoyé à: " + account.getCustomerEmail());

            // Alerte découvert
            if (account.getBalance() < 0) {
                System.out.println("ALERTE: Compte en découvert!");
                System.out.println("SMS d'alerte envoyé à: " + account.getCustomerPhone());
            }
            return true;

        } else if (type.equals("VIREMENT")) {
            BankAccount source = accounts.get(sourceAccount);
            BankAccount destination = accounts.get(destinationAccount);

            if (source == null || destination == null) {
                System.out.println("ERREUR: Compte source ou destination introuvable");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Compte introuvable");
                transactions.add(tx);
                return false;
            }
            if (amount <= 0) {
                System.out.println("ERREUR: Montant invalide");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Montant invalide");
                transactions.add(tx);
                return false;
            }

            // CALCUL DE FRAIS EN DUR pour virement
            double fees = 0;
            if (source.getAccountType().equals("COURANT")) {
                fees = 1.0;
            } else if (source.getAccountType().equals("EPARGNE")) {
                fees = 2.5; // Frais élevé pour virement depuis épargne
            } else if (source.getAccountType().equals("PROFESSIONNEL")) {
                fees = 0.5;
            }

            double totalAmount = amount + fees;

            if (source.getBalance() - totalAmount < -source.getOverdraftLimit()) {
                System.out.println("ERREUR: Solde insuffisant");
                tx.setStatus("REJECTED");
                tx.setRejectionReason("Solde insuffisant");
                transactions.add(tx);
                return false;
            }

            source.setBalance(source.getBalance() - totalAmount);
            destination.setBalance(destination.getBalance() + amount);
            tx.setStatus("COMPLETED");
            transactions.add(tx);
            System.out.println("Virement effectué de " + sourceAccount + " vers " + destinationAccount);
            System.out.println("Email envoyé à: " + source.getCustomerEmail());
            System.out.println("Email envoyé à: " + destination.getCustomerEmail());
            return true;
        }

        System.out.println("ERREUR: Type de transaction inconnu");
        tx.setStatus("REJECTED");
        tx.setRejectionReason("Type inconnu");
        transactions.add(tx);
        return false;
    }

    public String generateStatement(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            return "Compte introuvable";
        }

        StringBuilder statement = new StringBuilder();
        statement.append("========================================\n");
        statement.append("RELEVÉ BANCAIRE\n");
        statement.append("========================================\n");
        statement.append("Compte: ").append(account.getAccountNumber()).append("\n");
        statement.append("Type: ").append(account.getAccountType()).append("\n");
        statement.append("Titulaire: ").append(account.getCustomerName()).append("\n");
        statement.append("Date de création: ").append(account.getCreationDate()).append("\n");
        statement.append("Solde actuel: ").append(account.getBalance()).append(" EUR\n");
        statement.append("========================================\n");
        statement.append("TRANSACTIONS\n");
        statement.append("========================================\n");

        for (Transaction tx : transactions) {
            if (tx.getSourceAccount() != null && tx.getSourceAccount().equals(accountNumber)) {
                statement.append(tx.getTransactionDate()).append(" | ");
                statement.append(tx.getType()).append(" | ");
                statement.append("-").append(tx.getAmount()).append(" EUR | ");
                statement.append(tx.getStatus()).append("\n");
            } else if (tx.getDestinationAccount() != null && tx.getDestinationAccount().equals(accountNumber)) {
                statement.append(tx.getTransactionDate()).append(" | ");
                statement.append(tx.getType()).append(" | ");
                statement.append("+").append(tx.getAmount()).append(" EUR | ");
                statement.append(tx.getStatus()).append("\n");
            }
        }

        statement.append("========================================\n");

        return statement.toString();
    }

    public void applyInterest() {
        System.out.println("Application des intérêts...");
        for (BankAccount account : accounts.values()) {
            if (account.getAccountType().equals("EPARGNE")) {
                double interest = account.getBalance() * account.getInterestRate() / 100;
                account.setBalance(account.getBalance() + interest);
                System.out.println("Intérêts appliqués sur " + account.getAccountNumber() + ": " + interest + " EUR");
                System.out.println("Email envoyé à: " + account.getCustomerEmail());
            } else if (account.getAccountType().equals("PROFESSIONNEL")) {
                double interest = account.getBalance() * account.getInterestRate() / 100;
                account.setBalance(account.getBalance() + interest);
                System.out.println("Intérêts appliqués sur " + account.getAccountNumber() + ": " + interest + " EUR");
                System.out.println("Email envoyé à: " + account.getCustomerEmail());
            }
        }
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Affiche les transactions d'un compte dans l'ordre chronologique
     */
    public void afficherTransactionsChronologique(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("ERREUR: Compte introuvable");
            return;
        }

        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.isEmpty()) {
            System.out.println("Aucune transaction pour ce compte.");
            return;
        }

        // Tri par date
        accountTransactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        System.out.println("\n=== Transactions chronologiques pour le compte " + accountNumber + " ===");
        System.out.printf("%-15s | %-10s | %10s | %-30s | %-10s%n",
            "ID", "Type", "Montant", "Date", "Status");
        System.out.println("-".repeat(90));

        for (Transaction tx : accountTransactions) {
            System.out.printf("%-15s | %-10s | %9.2f€ | %-30s | %-10s%n",
                tx.getTransactionId(),
                tx.getType(),
                tx.getAmount(),
                tx.getTransactionDate(),
                tx.getStatus());
        }
    }

    /**
     * Affiche les transactions d'un compte triées par montant
     */
    public void afficherTransactionsMontant(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("ERREUR: Compte introuvable");
            return;
        }

        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.isEmpty()) {
            System.out.println("Aucune transaction pour ce compte.");
            return;
        }

        // Tri par montant
        accountTransactions.sort(Comparator.comparingDouble(Transaction::getAmount));

        System.out.println("\n=== Transactions par montant pour le compte " + accountNumber + " ===");
        System.out.printf("%-15s | %-10s | %10s | %-30s | %-10s%n",
            "ID", "Type", "Montant", "Date", "Status");
        System.out.println("-".repeat(90));

        for (Transaction tx : accountTransactions) {
            System.out.printf("%-15s | %-10s | %9.2f€ | %-30s | %-10s%n",
                tx.getTransactionId(),
                tx.getType(),
                tx.getAmount(),
                tx.getTransactionDate(),
                tx.getStatus());
        }
    }

    /**
     * Affiche les transactions d'un compte filtrées par type
     */
    public void afficherTransactionsParType(String accountNumber, String type) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("ERREUR: Compte introuvable");
            return;
        }

        List<Transaction> accountTransactions = account.getTransactions();
        List<Transaction> filteredTransactions = new ArrayList<>();

        // Filtrer par type
        for (Transaction tx : accountTransactions) {
            if (tx.getType().equals(type)) {
                filteredTransactions.add(tx);
            }
        }

        if (filteredTransactions.isEmpty()) {
            System.out.println("Aucune transaction de type " + type + " pour ce compte.");
            return;
        }

        // Tri par date
        filteredTransactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        System.out.println("\n=== Transactions de type " + type + " pour le compte " + accountNumber + " ===");
        System.out.printf("%-15s | %-10s | %10s | %-30s | %-10s%n",
            "ID", "Type", "Montant", "Date", "Status");
        System.out.println("-".repeat(90));

        for (Transaction tx : filteredTransactions) {
            System.out.printf("%-15s | %-10s | %9.2f€ | %-30s | %-10s%n",
                tx.getTransactionId(),
                tx.getType(),
                tx.getAmount(),
                tx.getTransactionDate(),
                tx.getStatus());
        }
    }
}
