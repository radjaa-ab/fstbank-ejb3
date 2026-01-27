package dz.fst.bank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Classe abstraite Utilisateur - Base commune pour Client et Banquier
 * Evite la répétition des propriétés communes
 */
@Entity
@Table(name = "UTILISATEURS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE_UTILISATEUR", discriminatorType = DiscriminatorType.STRING)
public abstract class Utilisateur implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(name = "IDENTIFIANT", unique = true, nullable = false, length = 50)
    protected String identifiant;
    
    @Column(name = "NOM", nullable = false, length = 100)
    protected String nom;
    
    @Column(name = "EMAIL", nullable = false, length = 100)
    protected String email;
    
    @Column(name = "MOT_DE_PASSE", nullable = false)
    protected String motDePasse;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATION")
    protected Date dateCreation;
    
    // Constructeurs
    public Utilisateur() {
        this.dateCreation = new Date();
    }
    
    public Utilisateur(String identifiant, String nom, String email, String motDePasse) {
        this();
        this.identifiant = identifiant;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIdentifiant() {
        return identifiant;
    }
    
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
