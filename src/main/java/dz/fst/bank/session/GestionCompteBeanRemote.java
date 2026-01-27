package dz.fst.bank.session;

import dz.fst.bank.entities.Compte;
import javax.ejb.Remote;
import java.util.List;

@Remote
public interface GestionCompteBeanRemote {
    
    Compte creerCompteParticulierSimple(Long clientId);
    
    Compte creerCompteParticulierPartage(List<Long> clientsIds);
    
    Compte creerCompteProfessionnel(Long clientId);
    
    Compte rechercherCompte(String numeroCompte);
    
    Compte rechercherCompteParId(Long id);
    
    List<Compte> listerComptesClient(Long clientId);
    
    double consulterSolde(Long compteId);
    
    void ajouterProprietaire(Long compteId, Long clientId);
    
    void retirerProprietaire(Long compteId, Long clientId);
    
    void activerCompte(Long compteId);
    
    void desactiverCompte(Long compteId);
    
    void modifierDecouvert(Long compteId, double montant);
}
