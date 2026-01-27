package dz.fst.bank.entities;

import javax.persistence.*;

@Entity
@Table(name = "COMPTES_PROFESSIONNELS")
@DiscriminatorValue("PROFESSIONNEL")
public class CompteProfessionnel extends Compte {
    
    @Column(name = "SIRET_ENTREPRISE", length = 14)
    private String siretEntreprise;
    
    @Column(name = "PLAFOND_TRANSACTION")
    private double plafondTransaction;
    
    // Constructeurs
    public CompteProfessionnel() {
        super();
        this.plafondTransaction = 100000.0; // Plafond par défaut
    }
    
    public CompteProfessionnel(String numeroCompte, ClientProfessionnel proprietaire) {
        super(numeroCompte);
        this.plafondTransaction = 100000.0;
        if (proprietaire != null) {
            this.getProprietaires().add(proprietaire);
            this.siretEntreprise = proprietaire.getSiret();
        }
    }
    
    @Override
    public String getTypeCompte() {
        return "PROFESSIONNEL";
    }
    
    @Override
    public boolean debiter(double montant) {
        if (montant > plafondTransaction) {
            System.out.println("Montant dépasse le plafond autorisé: " + plafondTransaction);
            return false;
        }
        return super.debiter(montant);
    }
    
    // Getters et Setters
    public String getSiretEntreprise() { return siretEntreprise; }
    public void setSiretEntreprise(String siretEntreprise) { 
        this.siretEntreprise = siretEntreprise; 
    }
    
    public double getPlafondTransaction() { return plafondTransaction; }
    public void setPlafondTransaction(double plafondTransaction) { 
        this.plafondTransaction = plafondTransaction; 
    }
    
    @Override
    public String toString() {
        return "CompteProfessionnel{" +
                "id=" + getId() +
                ", numeroCompte='" + getNumeroCompte() + '\'' +
                ", solde=" + getSolde() +
                ", plafondTransaction=" + plafondTransaction +
                '}';
    }
}
