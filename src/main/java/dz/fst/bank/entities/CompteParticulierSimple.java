package dz.fst.bank.entities;

import javax.persistence.*;

@Entity
@Table(name = "COMPTES_PARTICULIERS_SIMPLES")
@DiscriminatorValue("PARTICULIER_SIMPLE")
public class CompteParticulierSimple extends Compte {
    
    @Column(name = "DECOUVERT_AUTORISE")
    private double decouvertAutorise;
    
    // Constructeurs
    public CompteParticulierSimple() {
        super();
        this.decouvertAutorise = 0.0;
    }
    
    public CompteParticulierSimple(String numeroCompte, Client proprietaire) {
        super(numeroCompte);
        this.decouvertAutorise = 0.0;
        if (proprietaire != null) {
            this.getProprietaires().add(proprietaire);
        }
    }
    
    @Override
    public String getTypeCompte() {
        return "PARTICULIER_SIMPLE";
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
    
    // Getters et Setters
    public double getDecouvertAutorise() { return decouvertAutorise; }
    public void setDecouvertAutorise(double decouvertAutorise) { 
        this.decouvertAutorise = decouvertAutorise; 
    }
    
    @Override
    public String toString() {
        return "CompteParticulierSimple{" +
                "id=" + getId() +
                ", numeroCompte='" + getNumeroCompte() + '\'' +
                ", solde=" + getSolde() +
                ", decouvertAutorise=" + decouvertAutorise +
                '}';
    }
}
