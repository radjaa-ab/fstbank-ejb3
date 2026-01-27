package dz.fst.bank.rest;

import dz.fst.bank.entities.Client;
import dz.fst.bank.entities.Compte;
import dz.fst.bank.entities.Transaction;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * REST API pour le Dashboard FSTBANK
 */
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource {
    
    @GET
    @Path("/status")
    public Response getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("application", "FSTBANK");
        status.put("version", "1.0.0");
        status.put("status", "RUNNING");
        status.put("timestamp", System.currentTimeMillis());
        return Response.ok(status).build();
    }
    
    @GET
    @Path("/stats")
    public Response getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalClients", 3);
        stats.put("totalAccounts", 4);
        stats.put("totalTransactions", 4);
        stats.put("totalBalance", 129000.00);
        stats.put("validatedTransactions", 3);
        stats.put("rejectedTransactions", 1);
        stats.put("successRate", 75.0);
        return Response.ok(stats).build();
    }
    
    @GET
    @Path("/clients")
    public Response getClients() {
        List<Map<String, Object>> clients = new ArrayList<>();
        
        Map<String, Object> c1 = new LinkedHashMap<>();
        c1.put("id", 1);
        c1.put("identifiant", "C001");
        c1.put("nom", "Dupont");
        c1.put("prenom", "Jean");
        c1.put("email", "jean@gmail.com");
        c1.put("type", "PARTICULIER");
        clients.add(c1);
        
        Map<String, Object> c2 = new LinkedHashMap<>();
        c2.put("id", 2);
        c2.put("identifiant", "C002");
        c2.put("nom", "Martin");
        c2.put("prenom", "Marie");
        c2.put("email", "marie@gmail.com");
        c2.put("type", "PARTICULIER");
        clients.add(c2);
        
        Map<String, Object> c3 = new LinkedHashMap<>();
        c3.put("id", 3);
        c3.put("identifiant", "CP001");
        c3.put("nom", "TechCorp");
        c3.put("prenom", "SA");
        c3.put("email", "contact@techcorp.dz");
        c3.put("type", "PROFESSIONNEL");
        c3.put("siret", "12345678901234");
        clients.add(c3);
        
        return Response.ok(clients).build();
    }
    
    @GET
    @Path("/accounts")
    public Response getAccounts() {
        List<Map<String, Object>> accounts = new ArrayList<>();
        
        Map<String, Object> a1 = new LinkedHashMap<>();
        a1.put("id", 1);
        a1.put("numero", "ACC001");
        a1.put("solde", 7000.00);
        a1.put("type", "PARTICULIER_SIMPLE");
        a1.put("owner", "Dupont Jean");
        accounts.add(a1);
        
        Map<String, Object> a2 = new LinkedHashMap<>();
        a2.put("id", 2);
        a2.put("numero", "ACC002");
        a2.put("solde", 15000.00);
        a2.put("type", "PARTICULIER_PARTAGE");
        a2.put("owners", "Dupont Jean, Martin Marie");
        accounts.add(a2);
        
        Map<String, Object> a3 = new LinkedHashMap<>();
        a3.put("id", 3);
        a3.put("numero", "ACC003");
        a3.put("solde", 7000.00);
        a3.put("type", "PARTICULIER_SIMPLE");
        a3.put("owner", "Martin Marie");
        accounts.add(a3);
        
        Map<String, Object> a4 = new LinkedHashMap<>();
        a4.put("id", 4);
        a4.put("numero", "ACC004");
        a4.put("solde", 105000.00);
        a4.put("type", "PROFESSIONNEL");
        a4.put("owner", "TechCorp SA");
        accounts.add(a4);
        
        return Response.ok(accounts).build();
    }
    
    @GET
    @Path("/transactions")
    public Response getTransactions() {
        List<Map<String, Object>> transactions = new ArrayList<>();
        
        Map<String, Object> t1 = new LinkedHashMap<>();
        t1.put("id", 1);
        t1.put("montant", 2000.00);
        t1.put("type", "DEPOT");
        t1.put("statut", "VALIDEE");
        t1.put("compte", "ACC001");
        t1.put("date", "2026-01-27 10:30:00");
        transactions.add(t1);
        
        Map<String, Object> t2 = new LinkedHashMap<>();
        t2.put("id", 2);
        t2.put("montant", 1000.00);
        t2.put("type", "RETRAIT");
        t2.put("statut", "VALIDEE");
        t2.put("compte", "ACC003");
        t2.put("date", "2026-01-27 10:35:00");
        transactions.add(t2);
        
        Map<String, Object> t3 = new LinkedHashMap<>();
        t3.put("id", 3);
        t3.put("montant", 5000.00);
        t3.put("type", "VIREMENT");
        t3.put("statut", "VALIDEE");
        t3.put("from", "ACC001");
        t3.put("to", "ACC004");
        t3.put("date", "2026-01-27 10:40:00");
        transactions.add(t3);
        
        Map<String, Object> t4 = new LinkedHashMap<>();
        t4.put("id", 4);
        t4.put("montant", 10000.00);
        t4.put("type", "RETRAIT");
        t4.put("statut", "REJETEE");
        t4.put("compte", "ACC003");
        t4.put("raison", "Solde insuffisant");
        t4.put("date", "2026-01-27 10:45:00");
        transactions.add(t4);
        
        return Response.ok(transactions).build();
    }
}
