package dz.fst.bank.factories;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.ClientParticulier;
import dz.fst.bank.entities.ClientProfessionnel;

public class ClientFactory {
    
    public static Client creerClient(String type, String identifiant, 
                                     String nom, String email, String motDePasse) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de client ne peut pas être null");
        }
        
        switch (type.toUpperCase()) {
            case "PARTICULIER":
                return creerClientParticulier(identifiant, nom, "", email, motDePasse);
            
            case "PROFESSIONNEL":
                return creerClientProfessionnel(identifiant, nom, "", email, motDePasse);
            
            default:
                throw new IllegalArgumentException("Type de client inconnu: " + type);
        }
    }
    
    public static ClientParticulier creerClientParticulier(String identifiant, String nom, 
                                                           String prenom, String email, 
                                                           String motDePasse) {
        if (identifiant == null || nom == null || email == null || motDePasse == null) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être renseignés");
        }
        
        ClientParticulier client = new ClientParticulier(identifiant, nom, prenom, email, motDePasse);
        System.out.println("ClientFactory: Client particulier créé - " + identifiant);
        return client;
    }
    
    public static ClientProfessionnel creerClientProfessionnel(String identifiant, 
                                                               String raisonSociale, 
                                                               String siret, 
                                                               String email, 
                                                               String motDePasse) {
        if (identifiant == null || raisonSociale == null || siret == null || 
            email == null || motDePasse == null) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être renseignés");
        }
        
        if (siret.length() != 14) {
            throw new IllegalArgumentException("Le SIRET doit contenir 14 caractères");
        }
        
        ClientProfessionnel client = new ClientProfessionnel(
            identifiant, raisonSociale, siret, email, motDePasse
        );
        System.out.println("ClientFactory: Client professionnel créé - " + identifiant);
        return client;
    }
}
