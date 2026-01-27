package dz.fst.bank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "COMPTES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE_COMPTE", discriminatorType = DiscriminatorType.STRING)
public abstract class Compte implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "NUMERO_COMPTE", unique = true, nullable = false, length = 20)
    private String numeroCompte;
    
    @Column(name = "SOLDE", nullable = false)
    private double solde;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATION")
    private Date dateCreation;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_MODIFICATION")
    private Date dateModification;
    
    @Column(name = "ACTIF")
    private boolean actif;
    
    // Relation Many-to-Many avec Client
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "COMPTE_PROPRIETAIRES",
        joinColumns = @JoinColumn(name = "COMPTE_ID"),
        inverseJoinColumns = @JoinColumn(name = "CLIENT_ID")
    )
    private List<Client> proprietaires;
    
    // Relation One-to-Many avec Transaction
    @OneToMany(mappedBy = "compteSource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    
    // Constructeurs
    public Compte() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
        this.actif = true;
        this.solde = 0.0;
        this.proprietaires = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
    
    public Compte(String numeroCompte) {
        this();
        this.numeroCompte = numeroCompte;
    }
    
    // Méthodes métier
    public void crediter(double montant) {
        if (montant > 0) {
            this.solde += montant;
            this.dateModification = new Date();
        }
    }
    
    public boolean debiter(double montant) {
        if (montant > 0 && this.solde >= montant) {
            this.solde -= montant;
            this.dateModification = new Date();
            return true;
        }
        return false;
    }
    
    public boolean peutEffectuerOperation(String identifiantClient) {
        return proprietaires.stream()
                .anyMatch(client -> client.getIdentifiant().equals(identifiantClient));
    }
    
    public void ajouterProprietaire(Client client) {
        if (!proprietaires.contains(client)) {
            proprietaires.add(client);
        }
    }
    
    public void retirerProprietaire(Client client) {
        proprietaires.remove(client);
    }
    
    // Callbacks du cycle de vie
    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = new Date();
        }
        dateModification = new Date();
        System.out.println("Compte " + numeroCompte + " va être créé");
    }
    
    @PostPersist
    public void postPersist() {
        System.out.println("Compte " + numeroCompte + " créé avec succès - ID: " + id);
    }
    
    @PreUpdate
    public void preUpdate() {
        dateModification = new Date();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }
    
    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }
    
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    
    public Date getDateModification() { return dateModification; }
    public void setDateModification(Date dateModification) { this.dateModification = dateModification; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public List<Client> getProprietaires() { return proprietaires; }
    public void setProprietaires(List<Client> proprietaires) { this.proprietaires = proprietaires; }
    
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    
    // Méthode abstraite pour le type
    public abstract String getTypeCompte();
}
