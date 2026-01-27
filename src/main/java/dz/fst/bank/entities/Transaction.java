package dz.fst.bank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TRANSACTION", nullable = false)
    private Date dateTransaction;
    
    @Column(name = "MONTANT", nullable = false)
    private double montant;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_OPERATION", nullable = false)
    private TypeOperation typeOperation;
    
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUT", nullable = false)
    private StatutTransaction statut;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPTE_SOURCE_ID", nullable = false)
    private Compte compteSource;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPTE_DESTINATION_ID")
    private Compte compteDestination;
    
    // Constructeurs
    public Transaction() {
        this.dateTransaction = new Date();
        this.statut = StatutTransaction.EN_COURS;
    }
    
    public Transaction(Compte compteSource, double montant, TypeOperation typeOperation) {
        this();
        this.compteSource = compteSource;
        this.montant = montant;
        this.typeOperation = typeOperation;
    }
    
    @PrePersist
    public void prePersist() {
        if (dateTransaction == null) {
            dateTransaction = new Date();
        }
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Date getDateTransaction() { return dateTransaction; }
    public void setDateTransaction(Date dateTransaction) { 
        this.dateTransaction = dateTransaction; 
    }
    
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    
    public TypeOperation getTypeOperation() { return typeOperation; }
    public void setTypeOperation(TypeOperation typeOperation) { 
        this.typeOperation = typeOperation; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public StatutTransaction getStatut() { return statut; }
    public void setStatut(StatutTransaction statut) { this.statut = statut; }
    
    public Compte getCompteSource() { return compteSource; }
    public void setCompteSource(Compte compteSource) { this.compteSource = compteSource; }
    
    public Compte getCompteDestination() { return compteDestination; }
    public void setCompteDestination(Compte compteDestination) { 
        this.compteDestination = compteDestination; 
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + dateTransaction +
                ", montant=" + montant +
                ", type=" + typeOperation +
                ", statut=" + statut +
                '}';
    }
}
