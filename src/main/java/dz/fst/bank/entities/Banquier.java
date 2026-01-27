package dz.fst.bank.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BANQUIERS")
public class Banquier extends Utilisateur {
    
    @Column(name = "PRENOM", nullable = false, length = 100)
    private String prenom;
    
    @Column(name = "MATRICULE", unique = true, length = 20)
    private String matricule;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_EMBAUCHE")
    private Date dateEmbauche;
    
    // Constructeurs
    public Banquier() {
        super();
        this.dateEmbauche = new Date();
    }
    
    public Banquier(String identifiant, String nom, String prenom, 
                   String email, String motDePasse) {
        super(identifiant, nom, email, motDePasse);
        this.prenom = prenom;
        this.dateEmbauche = new Date();
    }
    
    // Getters et Setters (héritées de Utilisateur: id, identifiant, nom, email, motDePasse, dateCreation)
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public Date getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(Date dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    
    @Override
    public String toString() {
        return "Banquier{" +
                "id=" + id +
                ", identifiant='" + identifiant + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
