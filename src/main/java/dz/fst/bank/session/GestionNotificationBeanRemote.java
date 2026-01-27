package dz.fst.bank.session;

import javax.ejb.Remote;

/**
 * Interface distante pour la gestion des notifications
 */
@Remote
public interface GestionNotificationBeanRemote {
    
    /**
     * Activer les notifications pour un client sur un compte
     */
    void activerNotifications(Long compteId, Long clientId);
    
    /**
     * Désactiver les notifications pour un client sur un compte
     */
    void desactiverNotifications(Long compteId, Long clientId);
    
    /**
     * Afficher l'historique des notifications d'un client
     */
    void afficherHistoriqueNotifications(Long clientId);
}
