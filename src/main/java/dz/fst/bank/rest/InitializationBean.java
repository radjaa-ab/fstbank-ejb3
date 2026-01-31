package dz.fst.bank.rest;

import dz.fst.bank.entities.*;
import dz.fst.bank.session.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Arrays;

/**
 * Singleton qui initialise la base de données au démarrage de l'application
 */
// @Startup - Commented out due to Java 17 proxy module access issues with WildFly 20
@Singleton
public class InitializationBean {
    
    @EJB
    private GestionClientBeanRemote gestionClientBean;
    
    @EJB
    private GestionCompteBeanRemote gestionCompteBean;
    
    @EJB
    private GestionTransactionBeanRemote gestionTransactionBean;
    
    @PostConstruct
    public void init() {
        try {
            // Vérifier si la DB est déjà remplie
            if (gestionClientBean.listerTousLesClients().isEmpty()) {
                System.out.println("=== Initialisation de la base de données ===");
                initializeData();
                System.out.println("=== Initialisation terminée ===");
            } else {
                System.out.println("=== Base de données déjà initialisée ===");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initializeData() {
        try {
            // Créer les clients
            Client client1 = gestionClientBean.creerClientParticulier(
                "C001", "Dupont", "Jean", "jean@gmail.com", "pass123"
            );
            Client client2 = gestionClientBean.creerClientParticulier(
                "C002", "Martin", "Marie", "marie@gmail.com", "pass123"
            );
            Client client3 = gestionClientBean.creerClientProfessionnel(
                "CP001", "TechCorp", "12345678901234", "contact@techcorp.dz", "pass123"
            );
            
            // Créer les comptes
            Compte compte1 = gestionCompteBean.creerCompteParticulierSimple(client1.getId());
            gestionTransactionBean.effectuerDepot(compte1.getId(), 7000.00);
            
            Compte compte2 = gestionCompteBean.creerCompteParticulierPartage(
                Arrays.asList(client1.getId(), client2.getId())
            );
            gestionTransactionBean.effectuerDepot(compte2.getId(), 15000.00);
            
            Compte compte3 = gestionCompteBean.creerCompteParticulierSimple(client2.getId());
            gestionTransactionBean.effectuerDepot(compte3.getId(), 7000.00);
            
            Compte compte4 = gestionCompteBean.creerCompteProfessionnel(client3.getId());
            gestionTransactionBean.effectuerDepot(compte4.getId(), 105000.00);
            
            // Transactions de démonstration
            gestionTransactionBean.effectuerDepot(compte1.getId(), 2000.00);
            gestionTransactionBean.effectuerRetrait(compte3.getId(), 1000.00);
            
            System.out.println("Données initialisées avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation des données: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
