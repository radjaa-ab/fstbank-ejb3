package dz.fst.bank.observers;

import dz.fst.bank.entities.Transaction;

/**
 * Interface Observable - Pour les comptes qui peuvent être observés
 */
public interface ObservableCompte {
    
    /**
     * Ajouter un observateur
     * @param observateur L'observateur à ajouter
     */
    void ajouterObservateur(ObservateurCompte observateur);
    
    /**
     * Retirer un observateur
     * @param observateur L'observateur à retirer
     */
    void retirerObservateur(ObservateurCompte observateur);
    
    /**
     * Notifier tous les observateurs
     * @param transaction La transaction effectuée
     * @param message Le message de notification
     */
    void notifierObservateurs(Transaction transaction, String message);
}
