package com.bank.legacy.applied.observer;


public interface Observer {

    /**
     * Méthode appelée lorsqu'un événement se produit
     * @param event l'événement qui s'est produit
     */
    void update(TransferEvent event);
}
