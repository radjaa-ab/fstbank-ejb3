package dz.fst.bank.strategies;

import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.TypeOperation;
import javax.persistence.EntityManager;

public class StrategieVirement implements StrategyOperation {
    
    private Compte compteDestination;
    private EntityManager em;
    
    public StrategieVirement(Compte compteDestination, EntityManager em) {
        this.compteDestination = compteDestination;
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
        
        if (!compte.isActif() || !compteDestination.isActif()) {
            System.out.println("Validation échouée: Compte source ou destination inactif");
            return false;
        }
        
        if (compte.equals(compteDestination)) {
            System.out.println("Validation échouée: Impossible de virer vers le même compte");
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean executer(Compte compte, double montant) {
        if (!valider(compte, montant)) {
            return false;
        }
        
        boolean debitSuccess = compte.debiter(montant);
        if (debitSuccess) {
            compteDestination.crediter(montant);
            em.merge(compte);
            em.merge(compteDestination);
            return true;
        }
        
        return false;
    }
    
    @Override
    public TypeOperation getTypeOperation() {
        return TypeOperation.VIREMENT;
    }
    
    public Compte getCompteDestination() {
        return compteDestination;
    }
}
