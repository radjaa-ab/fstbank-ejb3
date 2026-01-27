package dz.fst.bank.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLIENTS_PARTICULIERS")
@DiscriminatorValue("PARTICULIER")
public class ClientParticulier extends Client {
    
    @Column(name = "PRENOM", nullable = false, length = 100)
    private String prenom;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_NAISSANCE")
    private Date dateNaissance;
    
    @Column(name = "ADRESSE", length = 255)
    private String adresse;
    
    @Column(name = "CIN", unique = true, length = 20)
    private String cin;
    
    // Constructeurs
    public ClientParticulier() {
        super();
    }
    
    public ClientParticulier(String identifiant, String nom, String prenom, 
                            String email, String motDePasse) {
        super(identifiant, nom, email, motDePasse);
        this.prenom = prenom;
    }
    
    @Override
    public String getTypeClient() {
        return "PARTICULIER";
    }
    
    // Getters et Setters
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    
    @Override
    public String toString() {
        return "ClientParticulier{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + prenom + '\'' +
                ", identifiant='" + getIdentifiant() + '\'' +
                '}';
    }
}
