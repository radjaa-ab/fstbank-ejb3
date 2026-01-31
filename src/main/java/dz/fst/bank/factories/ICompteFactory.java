package dz.fst.bank.factories;

import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.Client;

/**
 * Interface Factory pour la création de comptes
 * Pattern: Factory Method
 */
public interface ICompteFactory {
    
    /**
     * Méthode factory pour créer un compte
     * @param numeroCompte Numéro du compte
     * @param soldeInitial Solde initial
     * @param client Client propriétaire
     * @return Instance de Compte
     */
    Compte creerCompte(String numeroCompte, double soldeInitial, Client client);
    
    /**
     * Valider les données avant création
     * @return true si valide
     */
    boolean valider(String numeroCompte, double soldeInitial);
}
