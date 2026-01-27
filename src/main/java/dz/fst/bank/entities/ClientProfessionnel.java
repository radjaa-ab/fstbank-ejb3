package dz.fst.bank.entities;

import javax.persistence.*;

@Entity
@Table(name = "CLIENTS_PROFESSIONNELS")
@DiscriminatorValue("PROFESSIONNEL")
public class ClientProfessionnel extends Client {
    
    @Column(name = "RAISON_SOCIALE", nullable = false, length = 200)
    private String raisonSociale;
    
    @Column(name = "SIRET", unique = true, nullable = false, length = 14)
    private String siret;
    
    @Column(name = "FORME_JURIDIQUE", length = 50)
    private String formeJuridique;
    
    @Column(name = "SECTEUR_ACTIVITE", length = 100)
    private String secteurActivite;
    
    // Constructeurs
    public ClientProfessionnel() {
        super();
    }
    
    public ClientProfessionnel(String identifiant, String raisonSociale, 
                              String siret, String email, String motDePasse) {
        super(identifiant, raisonSociale, email, motDePasse);
        this.raisonSociale = raisonSociale;
        this.siret = siret;
    }
    
    @Override
    public String getTypeClient() {
        return "PROFESSIONNEL";
    }
    
    // Getters et Setters
    public String getRaisonSociale() { return raisonSociale; }
    public void setRaisonSociale(String raisonSociale) { this.raisonSociale = raisonSociale; }
    
    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }
    
    public String getFormeJuridique() { return formeJuridique; }
    public void setFormeJuridique(String formeJuridique) { this.formeJuridique = formeJuridique; }
    
    public String getSecteurActivite() { return secteurActivite; }
    public void setSecteurActivite(String secteurActivite) { this.secteurActivite = secteurActivite; }
    
    @Override
    public String toString() {
        return "ClientProfessionnel{" +
                "id=" + getId() +
                ", raisonSociale='" + raisonSociale + '\'' +
                ", siret='" + siret + '\'' +
                ", identifiant='" + getIdentifiant() + '\'' +
                '}';
    }
}
