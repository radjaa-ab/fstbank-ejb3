package dz.fst.bank.observers;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Observateur concret - Représente un client qui reçoit des notifications
 */
public class NotificationClient implements ObservateurCompte {
    
    private Client client;
    private List<String> notifications;
    private boolean notificationsActives;
    
    public NotificationClient(Client client) {
        this.client = client;
        this.notifications = new ArrayList<>();
        this.notificationsActives = true;
    }
    
    @Override
    public void notifier(Transaction transaction, String message) {
        if (!notificationsActives) {
            return;
        }
        
        String notification = String.format(
            "[%s] %s - Transaction #%d: %s (Montant: %.2f DA)",
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(
                transaction.getDateTransaction()
            ),
            client.getNom(),
            transaction.getId() != null ? transaction.getId() : 0,
            message,
            transaction.getMontant()
        );
        
        notifications.add(notification);
        
        // Afficher la notification
        System.out.println("\n📧 NOTIFICATION CLIENT:");
        System.out.println("   → " + notification);
        
        // Simulation d'envoi email
        envoyerEmail(notification);
    }
    
    @Override
    public String getIdentifiantObservateur() {
        return client.getIdentifiant();
    }
    
    private void envoyerEmail(String message) {
        // Simulation d'envoi d'email
        System.out.println("   ✉️  Email envoyé à: " + client.getEmail());
    }
    
    // Méthodes utilitaires
    public void activerNotifications() {
        this.notificationsActives = true;
        System.out.println("✓ Notifications activées pour " + client.getNom());
    }
    
    public void desactiverNotifications() {
        this.notificationsActives = false;
        System.out.println("✗ Notifications désactivées pour " + client.getNom());
    }
    
    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }
    
    public void afficherHistoriqueNotifications() {
        System.out.println("\n━━━━ HISTORIQUE NOTIFICATIONS: " + client.getNom() + " ━━━━");
        if (notifications.isEmpty()) {
            System.out.println("Aucune notification");
        } else {
            notifications.forEach(n -> System.out.println("  • " + n));
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    public Client getClient() {
        return client;
    }
    
    public boolean isNotificationsActives() {
        return notificationsActives;
    }
}
