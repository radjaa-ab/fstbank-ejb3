package dz.fst.bank.strategies;

import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.Transaction;
import dz.fst.bank.entities.TypeOperation;
import dz.fst.bank.entities.StatutTransaction;
import javax.persistence.EntityManager;

/**
 * Interface Stratégie pour les opérations transactionnelles
 * Pattern: Strategy
 */
public interface StrategieOperation {
    
    boolean executerOperation(Compte compte, double montant);
    
    Transaction creerTransaction(Compte compte, double montant);
}
