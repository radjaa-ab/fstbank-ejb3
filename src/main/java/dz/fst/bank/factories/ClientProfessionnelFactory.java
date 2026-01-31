package dz.fst.bank.factories;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.ClientProfessionnel;

/**
 * Factory concrète pour créer des clients professionnels
 */
public class ClientProfessionnelFactory implements IClientFactory {
    
    @Override
    public Client creerClient(String identifiant, String raisonSociale, String email, String motDePasse) {
        // Pour les pros, on utilise le nom comme raison sociale et on génère un SIRET
        String siret = genererSIRET(identifiant);
        
        if (!valider(identifiant, raisonSociale, email)) {
            throw new IllegalArgumentException("Données invalides pour le client professionnel");
        }
        
        ClientProfessionnel client = new ClientProfessionnel(identifiant, raisonSociale, siret, email, motDePasse);
        System.out.println("ClientProfessionnelFactory: Client professionnel créé - " + identifiant);
        return client;
    }
    
    @Override
    public boolean valider(String identifiant, String raisonSociale, String email) {
        if (identifiant == null || identifiant.trim().isEmpty()) {
            return false;
        }
        if (raisonSociale == null || raisonSociale.trim().isEmpty()) {
            return false;
        }
        if (email == null || !email.contains("@")) {
            return false;
        }
        return true;
    }
    
    /**
     * Générer un SIRET fictif à partir de l'identifiant
     */
    private String genererSIRET(String identifiant) {
        // Générer un SIRET de 14 chiffres
        long hash = identifiant.hashCode();
        String siret = String.format("%014d", Math.abs(hash) % 100000000000000L);
        return siret;
    }
}
