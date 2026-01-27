package dz.fst.bank.session;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.ClientParticulier;
import dz.fst.bank.entities.ClientProfessionnel;
import javax.ejb.Remote;
import java.util.List;

@Remote
public interface GestionClientBeanRemote {
    
    Client creerClientParticulier(String identifiant, String nom, String prenom, 
                                  String email, String motDePasse);
    
    Client creerClientProfessionnel(String identifiant, String raisonSociale, 
                                    String siret, String email, String motDePasse);
    
    Client rechercherClient(String identifiant);
    
    Client rechercherClientParId(Long id);
    
    List<Client> listerTousLesClients();
    
    void modifierClient(Client client);
    
    void supprimerClient(Long clientId);
    
    boolean authentifierClient(String identifiant, String motDePasse);
}
