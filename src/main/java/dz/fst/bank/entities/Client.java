package dz.fst.bank.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CLIENTS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE_CLIENT", discriminatorType = DiscriminatorType.STRING)
public abstract class Client extends Utilisateur {
    
    @Column(name = "TELEPHONE", length = 20)
    private String telephone;
    
    // Relation Many-to-Many avec Compte (car un compte peut être partagé)
    @ManyToMany(mappedBy = "proprietaires", fetch = FetchType.LAZY)
    private List<Compte> comptes;
    
    // Constructeurs
    public Client() {
        super();
    }
    
    public Client(String identifiant, String nom, String email, String motDePasse) {
        super(identifiant, nom, email, motDePasse);
    }
    
    public Client(String identifiant, String nom, String email, String telephone, String motDePasse) {
        super(identifiant, nom, email, motDePasse);
        this.telephone = telephone;
    }
    
    // Méthodes du cycle de vie
    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = new Date();
        }
        System.out.println("Client " + nom + " va être créé");
    }
    
    @PostPersist
    public void postPersist() {
        System.out.println("Client " + nom + " créé avec succès - ID: " + id);
    }
    
    // Getters et Setters (héritées de Utilisateur: id, identifiant, nom, email, motDePasse, dateCreation)
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public List<Compte> getComptes() { return comptes; }
    public void setComptes(List<Compte> comptes) { this.comptes = comptes; }
    
    // Méthode abstraite pour le type
    public abstract String getTypeClient();
}
