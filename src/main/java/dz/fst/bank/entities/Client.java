package dz.fst.bank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CLIENTS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE_CLIENT", discriminatorType = DiscriminatorType.STRING)
public abstract class Client implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "IDENTIFIANT", unique = true, nullable = false, length = 50)
    private String identifiant;
    
    @Column(name = "NOM", nullable = false, length = 100)
    private String nom;
    
    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;
    
    @Column(name = "TELEPHONE", length = 20)
    private String telephone;
    
    @Column(name = "MOT_DE_PASSE", nullable = false)
    private String motDePasse;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_INSCRIPTION")
    private Date dateInscription;
    
    // Relation Many-to-Many avec Compte (car un compte peut être partagé)
    @ManyToMany(mappedBy = "proprietaires", fetch = FetchType.LAZY)
    private List<Compte> comptes;
    
    // Constructeurs
    public Client() {
        this.dateInscription = new Date();
    }
    
    public Client(String identifiant, String nom, String email, String motDePasse) {
        this();
        this.identifiant = identifiant;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    
    // Méthodes du cycle de vie
    @PrePersist
    public void prePersist() {
        if (dateInscription == null) {
            dateInscription = new Date();
        }
        System.out.println("Client " + nom + " va être créé");
    }
    
    @PostPersist
    public void postPersist() {
        System.out.println("Client " + nom + " créé avec succès - ID: " + id);
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public Date getDateInscription() { return dateInscription; }
    public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }
    
    public List<Compte> getComptes() { return comptes; }
    public void setComptes(List<Compte> comptes) { this.comptes = comptes; }
    
    // Méthode abstraite pour le type
    public abstract String getTypeClient();
}
