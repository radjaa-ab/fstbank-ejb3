package dz.fst.bank.observers;

import dz.fst.bank.entities.Transaction;

/**
 * Interface Observer - Pour les clients qui veulent être notifiés
 */
public interface ObservateurCompte {
    
    /**
     * Méthode appelée quand une transaction est effectuée
     * @param transaction La transaction effectuée
     * @param message Message de notification
     */
    void notifier(Transaction transaction, String message);
    
    /**
     * Obtenir l'identifiant de l'observateur
     * @return Identifiant
     */
    String getIdentifiantObservateur();
}
