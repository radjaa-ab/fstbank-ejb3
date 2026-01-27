package dz.fst.bank.session;

import dz.fst.bank.entities.*;
import dz.fst.bank.factories.CompteFactory;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Stateful
public class GestionCompteBean implements GestionCompteBeanRemote {
    
    @PersistenceContext(unitName = "FSTBankPU")
    private EntityManager em;
    
    private Random random = new Random();
    
    // State management
    private Compte compteCourant;
    private List<Compte> comptesSession;
    
    @PostConstruct
    public void init() {
        comptesSession = new ArrayList<>();
        System.out.println(">>> GestionCompteBean STATEFUL initialisé");
    }
    
    @PreDestroy
    public void cleanup() {
        comptesSession.clear();
        compteCourant = null;
        System.out.println(">>> GestionCompteBean STATEFUL nettoyé");
    }
    
    public Compte getCompteCourant() {
        return compteCourant;
    }
    
    public void setCompteCourant(Compte compte) {
        this.compteCourant = compte;
    }
    
    public List<Compte> getComptesSession() {
        return comptesSession;
    }
    
    private String genererNumeroCompte() {
        return "FST" + System.currentTimeMillis() + random.nextInt(1000);
    }
    
    @Override
    public Compte creerCompteParticulierSimple(Long clientId) {
        Client client = em.find(Client.class, clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable");
        }
        
        String numeroCompte = genererNumeroCompte();
        Compte compte = CompteFactory.creerCompteParticulierSimple(numeroCompte, client);
        em.persist(compte);
        System.out.println("Compte particulier simple créé: " + compte);
        return compte;
    }
    
    @Override
    public Compte creerCompteParticulierPartage(List<Long> clientsIds) {
        if (clientsIds == null || clientsIds.isEmpty()) {
            throw new IllegalArgumentException("Liste de clients vide");
        }
        
        if (clientsIds.size() > 10) {
            throw new IllegalArgumentException("Maximum 10 propriétaires autorisés");
        }
        
        List<Client> clients = new ArrayList<>();
        for (Long clientId : clientsIds) {
            Client client = em.find(Client.class, clientId);
            if (client == null) {
                throw new IllegalArgumentException("Client introuvable: " + clientId);
            }
            clients.add(client);
        }
        
        String numeroCompte = genererNumeroCompte();
        Compte compte = CompteFactory.creerCompteParticulierPartage(
            numeroCompte, 
            clients.toArray(new Client[0])
        );
        em.persist(compte);
        System.out.println("Compte particulier partagé créé: " + compte);
        return compte;
    }
    
    @Override
    public Compte creerCompteProfessionnel(Long clientId) {
        Client client = em.find(Client.class, clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable");
        }
        
        if (!(client instanceof ClientProfessionnel)) {
            throw new IllegalArgumentException("Le client doit être professionnel");
        }
        
        String numeroCompte = genererNumeroCompte();
        Compte compte = CompteFactory.creerCompteProfessionnel(
            numeroCompte, 
            (ClientProfessionnel) client
        );
        em.persist(compte);
        System.out.println("Compte professionnel créé: " + compte);
        return compte;
    }
    
    @Override
    public Compte rechercherCompte(String numeroCompte) {
        TypedQuery<Compte> query = em.createQuery(
            "SELECT c FROM Compte c WHERE c.numeroCompte = :numero", 
            Compte.class
        );
        query.setParameter("numero", numeroCompte);
        
        List<Compte> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    @Override
    public Compte rechercherCompteParId(Long id) {
        return em.find(Compte.class, id);
    }
    
    @Override
    public List<Compte> listerComptesClient(Long clientId) {
        TypedQuery<Compte> query = em.createQuery(
            "SELECT DISTINCT c FROM Compte c JOIN c.proprietaires p WHERE p.id = :clientId", 
            Compte.class
        );
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
    
    @Override
    public double consulterSolde(Long compteId) {
        Compte compte = em.find(Compte.class, compteId);
        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable");
        }
        return compte.getSolde();
    }
    
    @Override
    public void ajouterProprietaire(Long compteId, Long clientId) {
        Compte compte = em.find(Compte.class, compteId);
        Client client = em.find(Client.class, clientId);
        
        if (compte == null || client == null) {
            throw new IllegalArgumentException("Compte ou client introuvable");
        }
        
        compte.ajouterProprietaire(client);
        em.merge(compte);
        System.out.println("Propriétaire ajouté au compte " + compte.getNumeroCompte());
    }
    
    @Override
    public void retirerProprietaire(Long compteId, Long clientId) {
        Compte compte = em.find(Compte.class, compteId);
        Client client = em.find(Client.class, clientId);
        
        if (compte == null || client == null) {
            throw new IllegalArgumentException("Compte ou client introuvable");
        }
        
        compte.retirerProprietaire(client);
        em.merge(compte);
        System.out.println("Propriétaire retiré du compte " + compte.getNumeroCompte());
    }
    
    @Override
    public void activerCompte(Long compteId) {
        Compte compte = em.find(Compte.class, compteId);
        if (compte != null) {
            compte.setActif(true);
            em.merge(compte);
            System.out.println("Compte activé: " + compte.getNumeroCompte());
        }
    }
    
    @Override
    public void desactiverCompte(Long compteId) {
        Compte compte = em.find(Compte.class, compteId);
        if (compte != null) {
            compte.setActif(false);
            em.merge(compte);
            System.out.println("Compte désactivé: " + compte.getNumeroCompte());
        }
    }
    
    @Override
    public void modifierDecouvert(Long compteId, double montant) {
        Compte compte = em.find(Compte.class, compteId);
        if (compte instanceof CompteParticulierSimple) {
            ((CompteParticulierSimple) compte).setDecouvertAutorise(montant);
            em.merge(compte);
        } else if (compte instanceof CompteParticulierPartage) {
            ((CompteParticulierPartage) compte).setDecouvertAutorise(montant);
            em.merge(compte);
        }
    }
}
