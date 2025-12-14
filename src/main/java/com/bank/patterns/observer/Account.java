package com.bank.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class Account implements Subject {
    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private final List<Observer> observers;

    public Account(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.observers = new ArrayList<>();
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[COMPTE] Observateur ajouté au compte " + accountNumber);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("[COMPTE] Observateur retiré du compte " + accountNumber);
    }

    @Override
    public void notifyObservers(TransferEvent event) {
        System.out.println("[COMPTE] Notification de " + observers.size() + " observateur(s)...");
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    public void receiveTransfer(double amount, String currency, String senderName, String reference) {
        System.out.println("\n[COMPTE] Réception d'un virement de " + amount + " " + currency);

        this.balance += amount;

        TransferEvent event = new TransferEvent(
                this.accountNumber,
                amount,
                currency,
                senderName,
                reference
        );

        notifyObservers(event);

        System.out.println("[COMPTE] Nouveau solde: " + this.balance + " " + currency + "\n");
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }
}
