package dz.fst.bank.session;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.Compte;
import dz.fst.bank.observers.NotificationClient;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * Session Bean pour gérer les notifications des clients
 * Implémente le pattern Observer
 */
@Stateful
public class GestionNotificationBean implements GestionNotificationBeanRemote {
    
    @PersistenceContext(unitName = "FSTBankPU")
    private EntityManager em;
    
    // State management
    private NotificationClient notificationCourante;
    private Map<Long, NotificationClient> notificationsSession;
    
    @PostConstruct
    public void init() {
        notificationsSession = new HashMap<>();
        System.out.println(">>> GestionNotificationBean STATEFUL initialisé");
    }
    
    @PreDestroy
    public void cleanup() {
        notificationsSession.clear();
        notificationCourante = null;
        System.out.println(">>> GestionNotificationBean STATEFUL nettoyé");
    }
    
    public NotificationClient getNotificationCourante() {
        return notificationCourante;
    }
    
    public void setNotificationCourante(NotificationClient notification) {
        this.notificationCourante = notification;
    }
    
    public Map<Long, NotificationClient> getNotificationsSession() {
        return notificationsSession;
    }
        Compte compte = em.find(Compte.class, compteId);
        Client client = em.find(Client.class, clientId);
        
        if (compte == null || client == null) {
            throw new IllegalArgumentException("Compte ou client introuvable");
        }
        
        // Vérifier que le client est propriétaire du compte
        if (!compte.getProprietaires().contains(client)) {
            throw new IllegalArgumentException("Le client n'est pas propriétaire de ce compte");
        }
        
        // Créer ou récupérer le NotificationClient
        String key = clientId + "_" + compteId;
        NotificationClient notification = notificationsCache.get(key);
        
        if (notification == null) {
            notification = new NotificationClient(client);
            notificationsCache.put(key, notification);
        }
        
        // Ajouter comme observateur
        compte.ajouterObservateur(notification);
        notification.activerNotifications();
    }
    
    @Override
    public void desactiverNotifications(Long compteId, Long clientId) {
        Compte compte = em.find(Compte.class, compteId);
        Client client = em.find(Client.class, clientId);
        
        if (compte == null || client == null) {
            throw new IllegalArgumentException("Compte ou client introuvable");
        }
        
        String key = clientId + "_" + compteId;
        NotificationClient notification = notificationsCache.get(key);
        
        if (notification != null) {
            compte.retirerObservateur(notification);
            notification.desactiverNotifications();
        }
    }
    
    @Override
    public void afficherHistoriqueNotifications(Long clientId) {
        Client client = em.find(Client.class, clientId);
        
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable");
        }
        
        // Afficher toutes les notifications du client
        boolean trouve = false;
        for (NotificationClient notification : notificationsCache.values()) {
            if (notification.getClient().getId().equals(clientId)) {
                notification.afficherHistoriqueNotifications();
                trouve = true;
            }
        }
        
        if (!trouve) {
            System.out.println("Aucune notification pour le client " + client.getNom());
        }
    }
}
