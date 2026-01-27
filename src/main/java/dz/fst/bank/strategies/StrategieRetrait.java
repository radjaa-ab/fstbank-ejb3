package dz.fst.bank.strategies;

import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.TypeOperation;
import javax.persistence.EntityManager;

public class StrategieRetrait implements StrategyOperation {
    
    private EntityManager em;
    
    public StrategieRetrait(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public boolean valider(Compte compte, double montant) {
        if (montant <= 0) {
            System.out.println("Validation échouée: Montant invalide");
            return false;
        }
        
        if (compte.getSolde() < montant) {
            System.out.println("Validation échouée: Solde insuffisant");
            return false;
        }
        
        if (!compte.isActif()) {
            System.out.println("Validation échouée: Compte inactif");
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean executer(Compte compte, double montant) {
        if (!valider(compte, montant)) {
            return false;
        }
        
        boolean success = compte.debiter(montant);
        if (success) {
            em.merge(compte);
        }
        return success;
    }
    
    @Override
    public TypeOperation getTypeOperation() {
        return TypeOperation.RETRAIT;
    }
}
