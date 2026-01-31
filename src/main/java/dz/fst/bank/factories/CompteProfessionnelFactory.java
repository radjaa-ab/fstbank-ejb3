package dz.fst.bank.factories;

import dz.fst.bank.entities.*;

/**
 * Factory concrète pour créer des comptes professionnels
 */
public class CompteProfessionnelFactory implements ICompteFactory {
    
    @Override
    public Compte creerCompte(String numeroCompte, double soldeInitial, Client client) {
        if (!valider(numeroCompte, soldeInitial)) {
            throw new IllegalArgumentException("Données invalides pour le compte professionnel");
        }
        
        if (!(client instanceof ClientProfessionnel)) {
            throw new IllegalArgumentException("Ce type de compte nécessite un client professionnel");
        }
        
        CompteProfessionnel compte = new CompteProfessionnel();
        compte.setNumeroCompte(numeroCompte);
        compte.setSolde(soldeInitial);
        compte.setProprietaire((ClientProfessionnel) client);
        
        System.out.println("CompteProfessionnelFactory: Compte professionnel créé - " + numeroCompte);
        return compte;
    }
    
    @Override
    public boolean valider(String numeroCompte, double soldeInitial) {
        if (numeroCompte == null || numeroCompte.trim().isEmpty()) {
            return false;
        }
        // Les comptes professionnels peuvent avoir un découvert autorisé
        if (soldeInitial < -10000) {
            return false;
        }
        return true;
    }
}
