package dz.fst.bank.entities;

import javax.persistence.*;

@Entity
@Table(name = "COMPTES_PARTICULIERS_PARTAGES")
@DiscriminatorValue("PARTICULIER_PARTAGE")
public class CompteParticulierPartage extends Compte {
    
    @Column(name = "DECOUVERT_AUTORISE")
    private double decouvertAutorise;
    
    @Transient
    private static final int MAX_PROPRIETAIRES = 10;
    
    // Constructeurs
    public CompteParticulierPartage() {
        super();
        this.decouvertAutorise = 0.0;
    }
    
    public CompteParticulierPartage(String numeroCompte) {
        super(numeroCompte);
        this.decouvertAutorise = 0.0;
    }
    
    @Override
    public String getTypeCompte() {
        return "PARTICULIER_PARTAGE";
    }
    
    @Override
    public void ajouterProprietaire(Client client) {
        if (this.getProprietaires().size() >= MAX_PROPRIETAIRES) {
            throw new IllegalStateException(
                "Impossible d'ajouter plus de " + MAX_PROPRIETAIRES + " propriétaires"
            );
        }
        super.ajouterProprietaire(client);
    }
    
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && (this.getSolde() + decouvertAutorise) >= montant) {
            this.setSolde(this.getSolde() - montant);
            this.setDateModification(new java.util.Date());
            return true;
        }
        return false;
    }
    
    public int getNombreProprietaires() {
        return this.getProprietaires().size();
    }
    
    // Getters et Setters
    public double getDecouvertAutorise() { return decouvertAutorise; }
    public void setDecouvertAutorise(double decouvertAutorise) { 
        this.decouvertAutorise = decouvertAutorise; 
    }
    
    public static int getMaxProprietaires() { return MAX_PROPRIETAIRES; }
    
    @Override
    public String toString() {
        return "CompteParticulierPartage{" +
                "id=" + getId() +
                ", numeroCompte='" + getNumeroCompte() + '\'' +
                ", solde=" + getSolde() +
                ", nombreProprietaires=" + getNombreProprietaires() +
                ", decouvertAutorise=" + decouvertAutorise +
                '}';
    }
}
