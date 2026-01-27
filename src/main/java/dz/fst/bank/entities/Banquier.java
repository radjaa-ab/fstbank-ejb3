package dz.fst.bank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BANQUIERS")
public class Banquier implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "IDENTIFIANT", unique = true, nullable = false, length = 50)
    private String identifiant;
    
    @Column(name = "NOM", nullable = false, length = 100)
    private String nom;
    
    @Column(name = "PRENOM", nullable = false, length = 100)
    private String prenom;
    
    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "MOT_DE_PASSE", nullable = false)
    private String motDePasse;
    
    @Column(name = "MATRICULE", unique = true, length = 20)
    private String matricule;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_EMBAUCHE")
    private Date dateEmbauche;
    
    // Constructeurs
    public Banquier() {
        this.dateEmbauche = new Date();
    }
    
    public Banquier(String identifiant, String nom, String prenom, 
                   String email, String motDePasse) {
        this();
        this.identifiant = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
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
