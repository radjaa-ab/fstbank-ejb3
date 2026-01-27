package dz.fst.bank.session;

import dz.fst.bank.entities.*;
import dz.fst.bank.strategies.*;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Stateful
public class GestionTransactionBean implements GestionTransactionBeanRemote {
    
    @PersistenceContext(unitName = "FSTBankPU")
    private EntityManager em;
    
    private GestionnaireTransaction gestionnaireTransaction;
    
    // State management
    private List<Transaction> transactionsSession;
    
    @PostConstruct
    public void init() {
        gestionnaireTransaction = new GestionnaireTransaction(em);
        transactionsSession = new ArrayList<>();
        System.out.println(">>> GestionTransactionBean STATEFUL initialisé");
    }
    
    @PreDestroy
    public void cleanup() {
        transactionsSession.clear();
        System.out.println(">>> GestionTransactionBean STATEFUL nettoyé");
    }
    
    public List<Transaction> getTransactionsSession() {
        return transactionsSession;
    }
    
    @Override
    public boolean effectuerVirement(Long compteSourceId, Long compteDestId, double montant) {
        Compte compteSource = em.find(Compte.class, compteSourceId);
        Compte compteDest = em.find(Compte.class, compteDestId);
        
        if (compteSource == null || compteDest == null) {
            System.out.println("Erreur: Compte source ou destination introuvable");
            return false;
        }
        
        if (!compteSource.isActif() || !compteDest.isActif()) {
            System.out.println("Erreur: Compte source ou destination inactif");
            return false;
        }
        
        StrategieVirement strategie = new StrategieVirement(compteDest, em);
        gestionnaireTransaction.setStrategie(strategie);
        
        boolean success = gestionnaireTransaction.effectuerOperation(compteSource, montant);
        
        if (success) {
            System.out.println("Virement réussi: " + montant + " de " + 
                             compteSource.getNumeroCompte() + " vers " + 
                             compteDest.getNumeroCompte());
        }
        
        return success;
    }
    
    @Override
    public boolean effectuerRetrait(Long compteId, double montant) {
        Compte compte = em.find(Compte.class, compteId);
        
        if (compte == null) {
            System.out.println("Erreur: Compte introuvable");
            return false;
        }
        
        if (!compte.isActif()) {
            System.out.println("Erreur: Compte inactif");
            return false;
        }
        
        StrategieRetrait strategie = new StrategieRetrait(em);
        gestionnaireTransaction.setStrategie(strategie);
        
        boolean success = gestionnaireTransaction.effectuerOperation(compte, montant);
        
        if (success) {
            System.out.println("Retrait réussi: " + montant + " du compte " + 
                             compte.getNumeroCompte());
        }
        
        return success;
    }
    
    @Override
    public boolean effectuerDepot(Long compteId, double montant) {
        Compte compte = em.find(Compte.class, compteId);
        
        if (compte == null) {
            System.out.println("Erreur: Compte introuvable");
            return false;
        }
        
        if (!compte.isActif()) {
            System.out.println("Erreur: Compte inactif");
            return false;
        }
        
        StrategieDepot strategie = new StrategieDepot(em);
        gestionnaireTransaction.setStrategie(strategie);
        
        boolean success = gestionnaireTransaction.effectuerOperation(compte, montant);
        
        if (success) {
            System.out.println("Dépôt réussi: " + montant + " sur le compte " + 
                             compte.getNumeroCompte());
        }
        
        return success;
    }
    
    @Override
    public List<Transaction> consulterHistorique(Long compteId) {
        TypedQuery<Transaction> query = em.createQuery(
            "SELECT t FROM Transaction t WHERE t.compteSource.id = :compteId " +
            "OR t.compteDestination.id = :compteId ORDER BY t.dateTransaction DESC", 
            Transaction.class
        );
        query.setParameter("compteId", compteId);
        return query.getResultList();
    }
    
    @Override
    public Transaction consulterTransaction(Long transactionId) {
        return em.find(Transaction.class, transactionId);
    }
    
    @Override
    public List<Transaction> listerTransactionsParPeriode(Long compteId, Date debut, Date fin) {
        TypedQuery<Transaction> query = em.createQuery(
            "SELECT t FROM Transaction t WHERE (t.compteSource.id = :compteId " +
            "OR t.compteDestination.id = :compteId) " +
            "AND t.dateTransaction BETWEEN :debut AND :fin " +
            "ORDER BY t.dateTransaction DESC", 
            Transaction.class
        );
        query.setParameter("compteId", compteId);
        query.setParameter("debut", debut);
        query.setParameter("fin", fin);
        return query.getResultList();
    }
}
