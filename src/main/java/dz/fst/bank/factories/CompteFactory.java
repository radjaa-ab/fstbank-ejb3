package dz.fst.bank.factories;

import dz.fst.bank.entities.*;

public class CompteFactory {
    
    public static Compte creerCompte(String type, String numeroCompte, Client... proprietaires) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de compte ne peut pas être null");
        }
        
        if (proprietaires == null || proprietaires.length == 0) {
            throw new IllegalArgumentException("Au moins un propriétaire est requis");
        }
        
        switch (type.toUpperCase()) {
            case "PARTICULIER_SIMPLE":
                if (proprietaires.length > 1) {
                    throw new IllegalArgumentException(
                        "Un compte particulier simple ne peut avoir qu'un seul propriétaire"
                    );
                }
                return creerCompteParticulierSimple(numeroCompte, proprietaires[0]);
            
            case "PARTICULIER_PARTAGE":
                return creerCompteParticulierPartage(numeroCompte, proprietaires);
            
            case "PROFESSIONNEL":
                if (!(proprietaires[0] instanceof ClientProfessionnel)) {
                    throw new IllegalArgumentException(
                        "Un compte professionnel requiert un client professionnel"
                    );
                }
                return creerCompteProfessionnel(numeroCompte, (ClientProfessionnel) proprietaires[0]);
            
            default:
                throw new IllegalArgumentException("Type de compte inconnu: " + type);
        }
    }
    
    public static CompteParticulierSimple creerCompteParticulierSimple(String numeroCompte, 
                                                                        Client proprietaire) {
        if (numeroCompte == null || proprietaire == null) {
            throw new IllegalArgumentException("Numéro de compte et propriétaire requis");
        }
        
        CompteParticulierSimple compte = new CompteParticulierSimple(numeroCompte, proprietaire);
        System.out.println("CompteFactory: Compte particulier simple créé - " + numeroCompte);
        return compte;
    }
    
    public static CompteParticulierPartage creerCompteParticulierPartage(String numeroCompte, 
                                                                         Client... proprietaires) {
        if (numeroCompte == null || proprietaires == null || proprietaires.length == 0) {
            throw new IllegalArgumentException("Numéro de compte et propriétaires requis");
        }
        
        if (proprietaires.length > 10) {
            throw new IllegalArgumentException(
                "Un compte partagé ne peut avoir plus de 10 propriétaires"
            );
        }
        
        CompteParticulierPartage compte = new CompteParticulierPartage(numeroCompte);
        for (Client proprietaire : proprietaires) {
            compte.ajouterProprietaire(proprietaire);
        }
        
        System.out.println("CompteFactory: Compte particulier partagé créé - " + 
                         numeroCompte + " avec " + proprietaires.length + " propriétaires");
        return compte;
    }
    
    public static CompteProfessionnel creerCompteProfessionnel(String numeroCompte, 
                                                               ClientProfessionnel proprietaire) {
        if (numeroCompte == null || proprietaire == null) {
            throw new IllegalArgumentException("Numéro de compte et propriétaire requis");
        }
        
        CompteProfessionnel compte = new CompteProfessionnel(numeroCompte, proprietaire);
        System.out.println("CompteFactory: Compte professionnel créé - " + numeroCompte);
        return compte;
    }
}
