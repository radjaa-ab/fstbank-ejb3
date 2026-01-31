package dz.fst.bank.factories;

import dz.fst.bank.entities.Client;

/**
 * Interface Factory pour la création de clients
 * Pattern: Factory Method
 */
public interface IClientFactory {
    
    /**
     * Méthode factory pour créer un client
     * @return Instance de Client
     */
    Client creerClient(String identifiant, String nom, String email, String motDePasse);
    
    /**
     * Valider les données avant création
     * @return true si valide
     */
    boolean valider(String identifiant, String nom, String email);
}
