package dz.fst.bank.factories;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.ClientParticulier;

/**
 * Factory concrète pour créer des clients particuliers
 */
public class ClientParticulierFactory implements IClientFactory {
    
    @Override
    public Client creerClient(String identifiant, String nom, String email, String motDePasse) {
        if (!valider(identifiant, nom, email)) {
            throw new IllegalArgumentException("Données invalides pour le client particulier");
        }
        
        ClientParticulier client = new ClientParticulier(identifiant, nom, "", email, motDePasse);
        System.out.println("ClientParticulierFactory: Client créé - " + identifiant);
        return client;
    }
    
    @Override
    public boolean valider(String identifiant, String nom, String email) {
        if (identifiant == null || identifiant.trim().isEmpty()) {
            return false;
        }
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }
        if (email == null || !email.contains("@")) {
            return false;
        }
        return true;
    }
}
