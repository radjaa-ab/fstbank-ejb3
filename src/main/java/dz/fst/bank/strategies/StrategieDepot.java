package dz.fst.bank.strategies;

import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.TypeOperation;
import javax.persistence.EntityManager;

public class StrategieDepot implements StrategyOperation {
    
    private EntityManager em;
    private static final double MONTANT_MAX_DEPOT = 50000.0;
    
    public StrategieDepot(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public boolean valider(Compte compte, double montant) {
        if (montant <= 0) {
            System.out.println("Validation échouée: Montant invalide");
            return false;
        }
        
        if (montant > MONTANT_MAX_DEPOT) {
            System.out.println("Validation échouée: Montant dépasse le maximum autorisé (" + 
                             MONTANT_MAX_DEPOT + ")");
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
        
        compte.crediter(montant);
        em.merge(compte);
        return true;
    }
    
    @Override
    public TypeOperation getTypeOperation() {
        return TypeOperation.DEPOT;
    }
}
