package dz.fst.bank.strategies;

import dz.fst.bank.entities.*;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class GestionnaireTransaction {
    
    private StrategyOperation strategie;
    private EntityManager em;
    private List<Transaction> historiqueTransactions;
    
    public GestionnaireTransaction(EntityManager em) {
        this.em = em;
        this.historiqueTransactions = new ArrayList<>();
    }
    
    public void setStrategie(StrategyOperation strategie) {
        this.strategie = strategie;
    }
    
    public boolean effectuerOperation(Compte compte, double montant) {
        if (strategie == null) {
            System.out.println("Erreur: Aucune stratégie définie");
            return false;
        }
        
        // Créer la transaction
        Transaction transaction = new Transaction(compte, montant, strategie.getTypeOperation());
        transaction.setStatut(StatutTransaction.EN_COURS);
        
        // Si c'est un virement, ajouter le compte destination
        if (strategie instanceof StrategieVirement) {
            transaction.setCompteDestination(
                ((StrategieVirement) strategie).getCompteDestination()
            );
            transaction.setDescription("Virement de " + montant + " vers " + 
                                      transaction.getCompteDestination().getNumeroCompte());
        } else if (strategie instanceof StrategieRetrait) {
            transaction.setDescription("Retrait de " + montant);
        } else if (strategie instanceof StrategieDepot) {
            transaction.setDescription("Dépôt de " + montant);
        }
        
        // Exécuter l'opération
        boolean success = strategie.executer(compte, montant);
        
        if (success) {
            transaction.setStatut(StatutTransaction.VALIDEE);
            System.out.println("Transaction validée: " + transaction.getDescription());
        } else {
            transaction.setStatut(StatutTransaction.REJETEE);
            System.out.println("Transaction rejetée: " + transaction.getDescription());
        }
        
        // Persister la transaction
        em.persist(transaction);
        historiqueTransactions.add(transaction);
        
        return success;
    }
    
    public List<Transaction> getHistorique() {
        return new ArrayList<>(historiqueTransactions);
    }
}
