package com.bank.patterns.command;

import java.util.Stack;

/**
 * Invoker - Gère l'exécution des commandes avec historique pour undo/redo
 */
public class TransactionExecutor {

    private final Stack<BankCommand> executedCommands;
    private final Stack<BankCommand> undoneCommands;

    public TransactionExecutor() {
        this.executedCommands = new Stack<>();
        this.undoneCommands = new Stack<>();
    }

    /**
     * Exécute une commande et l'ajoute à l'historique si elle réussit
     * @param command la commande à exécuter
     * @return true si l'exécution a réussi
     */
    public boolean execute(BankCommand command) {
        System.out.println("\n--- Exécution: " + command.getDescription() + " ---");

        if (command.execute()) {
            executedCommands.push(command);
            // Vider la pile des commandes annulées car on a une nouvelle branche
            undoneCommands.clear();
            return true;
        }
        return false;
    }

    /**
     * Annule la dernière commande exécutée
     * @return true si l'annulation a réussi
     */
    public boolean undo() {
        if (executedCommands.isEmpty()) {
            System.out.println("ERREUR: Aucune commande à annuler");
            return false;
        }

        BankCommand command = executedCommands.pop();
        System.out.println("\n--- Annulation: " + command.getDescription() + " ---");

        if (command.undo()) {
            undoneCommands.push(command);
            return true;
        }

        // Si l'annulation échoue, remettre la commande dans l'historique
        executedCommands.push(command);
        return false;
    }

    /**
     * Rejoue la dernière commande annulée
     * @return true si le redo a réussi
     */
    public boolean redo() {
        if (undoneCommands.isEmpty()) {
            System.out.println("ERREUR: Aucune commande à rejouer");
            return false;
        }

        BankCommand command = undoneCommands.pop();
        System.out.println("\n--- Redo: " + command.getDescription() + " ---");

        if (command.execute()) {
            executedCommands.push(command);
            return true;
        }

        // Si le redo échoue, remettre la commande dans les annulées
        undoneCommands.push(command);
        return false;
    }

    /**
     * Affiche l'historique des commandes exécutées
     */
    public void printHistory() {
        System.out.println("\n=== Historique des commandes ===");
        if (executedCommands.isEmpty()) {
            System.out.println("Aucune commande dans l'historique");
        } else {
            int index = 1;
            for (BankCommand cmd : executedCommands) {
                System.out.println(index++ + ". " + cmd.getDescription());
            }
        }
    }

    /**
     * Retourne le nombre de commandes dans l'historique
     */
    public int getHistorySize() {
        return executedCommands.size();
    }

    /**
     * Retourne le nombre de commandes annulées (disponibles pour redo)
     */
    public int getUndoneSize() {
        return undoneCommands.size();
    }
}
