package com.bank.patterns.observer;

/**
 * Observer concret - Notification Push (Application mobile)
 *
 * Envoie une notification push lorsqu'un virement est reçu
 */
public class PushNotifier implements Observer {
    private final String deviceId;

    public PushNotifier(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void update(TransferEvent event) {
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│ 🔔 NOTIFICATION PUSH                                │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ Device: " + deviceId);
        System.out.println("│");
        System.out.println("│ 💰 Nouveau virement reçu");
        System.out.printf("│ Montant: %.2f %s%n", event.getAmount(), event.getCurrency());
        System.out.println("│ De: " + event.getSenderName());
        System.out.println("└─────────────────────────────────────────────────────┘");
    }

    public String getDeviceId() {
        return deviceId;
    }
}
