package dz.fst.bank.session;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.ClientParticulier;
import dz.fst.bank.entities.ClientProfessionnel;
import dz.fst.bank.factories.ClientFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Stateless
public class GestionClientBean implements GestionClientBeanRemote {
    
    @PersistenceContext(unitName = "FSTBankPU")
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        System.out.println(">>> GestionClientBean initialisé");
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println(">>> GestionClientBean détruit");
    }
    
    @Override
    public Client creerClientParticulier(String identifiant, String nom, String prenom, 
                                        String email, String motDePasse) {
        Client client = ClientFactory.creerClientParticulier(
            identifiant, nom, prenom, email, motDePasse
        );
        em.persist(client);
        System.out.println("Client particulier créé: " + client);
        return client;
    }
    
    @Override
    public Client creerClientProfessionnel(String identifiant, String raisonSociale, 
                                          String siret, String email, String motDePasse) {
        Client client = ClientFactory.creerClientProfessionnel(
            identifiant, raisonSociale, siret, email, motDePasse
        );
        em.persist(client);
        System.out.println("Client professionnel créé: " + client);
        return client;
    }
    
    @Override
    public Client rechercherClient(String identifiant) {
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c WHERE c.identifiant = :identifiant", 
            Client.class
        );
        query.setParameter("identifiant", identifiant);
        
        List<Client> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    @Override
    public Client rechercherClientParId(Long id) {
        return em.find(Client.class, id);
    }
    
    @Override
    public List<Client> listerTousLesClients() {
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c ORDER BY c.nom", 
            Client.class
        );
        return query.getResultList();
    }
    
    @Override
    public void modifierClient(Client client) {
        em.merge(client);
        System.out.println("Client modifié: " + client);
    }
    
    @Override
    public void supprimerClient(Long clientId) {
        Client client = em.find(Client.class, clientId);
        if (client != null) {
            em.remove(client);
            System.out.println("Client supprimé: " + client);
        }
    }
    
    @Override
    public boolean authentifierClient(String identifiant, String motDePasse) {
        Client client = rechercherClient(identifiant);
        return client != null && client.getMotDePasse().equals(motDePasse);
    }
}
