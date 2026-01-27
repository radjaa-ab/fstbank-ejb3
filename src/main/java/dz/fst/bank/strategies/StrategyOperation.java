package dz.fst.bank.strategies;

import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.TypeOperation;

public interface StrategyOperation {
    
    boolean valider(Compte compte, double montant);
    
    boolean executer(Compte compte, double montant);
    
    TypeOperation getTypeOperation();
}
