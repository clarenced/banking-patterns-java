package com.bank.legacy.applied.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite - Représente un portefeuille de comptes (peut contenir des comptes ou d'autres portefeuilles)
 */
public class AccountPortfolio implements AccountComponent {

    private final String name;
    private final List<AccountComponent> accounts;

    public AccountPortfolio(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    @Override
    public double getBalance() {
        double total = 0;
        for (AccountComponent account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("ERREUR: Montant invalide");
            return false;
        }
        if (accounts.isEmpty()) {
            System.out.println("ERREUR: Portefeuille vide, impossible de déposer");
            return false;
        }

        // Répartition équitable sur tous les comptes
        double amountPerAccount = amount / accounts.size();
        System.out.println("Répartition de " + amount + " EUR sur " + accounts.size() +
                          " compte(s) (" + amountPerAccount + " EUR chacun)");

        boolean allSuccess = true;
        for (AccountComponent account : accounts) {
            if (!account.deposit(amountPerAccount)) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("ERREUR: Montant invalide");
            return false;
        }
        if (accounts.isEmpty()) {
            System.out.println("ERREUR: Portefeuille vide, impossible de retirer");
            return false;
        }

        // Vérification du solde total
        if (getBalance() < amount) {
            System.out.println("ERREUR: Solde total insuffisant dans le portefeuille " + name);
            return false;
        }

        // Retrait proportionnel au solde de chaque compte
        double totalBalance = getBalance();
        System.out.println("Retrait proportionnel de " + amount + " EUR du portefeuille " + name);

        boolean allSuccess = true;
        for (AccountComponent account : accounts) {
            double proportion = account.getBalance() / totalBalance;
            double accountAmount = amount * proportion;
            if (accountAmount > 0) {
                if (!account.withdraw(accountAmount)) {
                    allSuccess = false;
                }
            }
        }
        return allSuccess;
    }

    @Override
    public String getAccountInfo() {
        return getAccountInfo(0);
    }

    private String getAccountInfo(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(indentLevel);

        sb.append(indent).append("📁 Portefeuille: ").append(name);
        sb.append(" (Solde total: ").append(String.format("%.2f", getBalance())).append(" EUR)\n");

        for (AccountComponent account : accounts) {
            if (account instanceof AccountPortfolio) {
                sb.append(((AccountPortfolio) account).getAccountInfo(indentLevel + 1));
            } else {
                sb.append(indent).append("  📄 ").append(account.getAccountInfo()).append("\n");
            }
        }

        return sb.toString();
    }

    @Override
    public void addChild(AccountComponent component) {
        accounts.add(component);
        System.out.println("Ajout de " + component.getName() + " au portefeuille " + name);
    }

    @Override
    public void removeChild(AccountComponent component) {
        if (accounts.remove(component)) {
            System.out.println("Retrait de " + component.getName() + " du portefeuille " + name);
        } else {
            System.out.println("ERREUR: " + component.getName() + " non trouvé dans " + name);
        }
    }

    @Override
    public List<AccountComponent> getChildren() {
        return new ArrayList<>(accounts);
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAccountCount() {
        int count = 0;
        for (AccountComponent account : accounts) {
            if (account instanceof AccountPortfolio) {
                count += ((AccountPortfolio) account).getAccountCount();
            } else {
                count++;
            }
        }
        return count;
    }
}
