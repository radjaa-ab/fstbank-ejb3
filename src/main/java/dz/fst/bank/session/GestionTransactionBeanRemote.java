package dz.fst.bank.session;

import dz.fst.bank.entities.Transaction;
import javax.ejb.Remote;
import java.util.List;
import java.util.Date;

@Remote
public interface GestionTransactionBeanRemote {
    
    boolean effectuerVirement(Long compteSourceId, Long compteDestId, double montant);
    
    boolean effectuerRetrait(Long compteId, double montant);
    
    boolean effectuerDepot(Long compteId, double montant);
    
    List<Transaction> consulterHistorique(Long compteId);
    
    Transaction consulterTransaction(Long transactionId);

    List<Transaction> listerToutesLesTransactions();
    
    List<Transaction> listerTransactionsParPeriode(Long compteId, Date debut, Date fin);
}
