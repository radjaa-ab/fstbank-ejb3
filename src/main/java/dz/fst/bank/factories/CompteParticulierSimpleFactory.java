package dz.fst.bank.factories;

import dz.fst.bank.entities.*;

/**
 * Factory concrète pour créer des comptes particuliers simples
 */
public class CompteParticulierSimpleFactory implements ICompteFactory {
    
    @Override
    public Compte creerCompte(String numeroCompte, double soldeInitial, Client client) {
        if (!valider(numeroCompte, soldeInitial)) {
            throw new IllegalArgumentException("Données invalides pour le compte");
        }
        
        if (!(client instanceof ClientParticulier)) {
            throw new IllegalArgumentException("Ce type de compte nécessite un client particulier");
        }
        
        CompteParticulierSimple compte = new CompteParticulierSimple();
        compte.setNumeroCompte(numeroCompte);
        compte.setSolde(soldeInitial);
        compte.setProprietaire((ClientParticulier) client);
        
        System.out.println("CompteParticulierSimpleFactory: Compte créé - " + numeroCompte);
        return compte;
    }
    
    @Override
    public boolean valider(String numeroCompte, double soldeInitial) {
        if (numeroCompte == null || numeroCompte.trim().isEmpty()) {
            return false;
        }
        if (soldeInitial < 0) {
            return false;
        }
        return true;
    }
}
