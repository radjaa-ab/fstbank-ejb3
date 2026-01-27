package dz.fst.bank.test;

import java.util.*;

/**
 * FSTBANK MAIN - Shows complete flow with Database simulation
 * Creates clients, accounts, and displays what's stored in database
 */
public class FSTBankMain {
    
    // Simulate Database Tables
    static class Database {
        static List<ClientRecord> CLIENTS = new ArrayList<>();
        static List<CompteRecord> COMPTES = new ArrayList<>();
        static List<TransactionRecord> TRANSACTIONS = new ArrayList<>();
        static int nextClientId = 1;
        static int nextCompteId = 1;
        static int nextTransactionId = 1;
    }
    
    static class ClientRecord {
        int id;
        String identifiant, nom, prenom, email, type, siret;
        long dateCreation;
        
        ClientRecord(int id, String ident, String nom, String prenom, String email, String type) {
            this.id = id;
            this.identifiant = ident;
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.type = type;
            this.dateCreation = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            String str = String.format("│ ID: %-3d │ %s │ %s %s │ Email: %-25s │ Type: %-12s │",
                id, identifiant, nom, prenom, email, type);
            if (siret != null) {
                str += " SIRET: " + siret;
            }
            return str;
        }
    }
    
    static class CompteRecord {
        int id;
        String numeroCompte;
        double solde;
        String type;
        int clientId;
        long dateCreation;
        
        CompteRecord(int id, String numero, double solde, String type, int clientId) {
            this.id = id;
            this.numeroCompte = numero;
            this.solde = solde;
            this.type = type;
            this.clientId = clientId;
            this.dateCreation = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            return String.format("│ ID: %-3d │ %s │ Solde: %10.2f€ │ Type: %-20s │ Owner ID: %d │",
                id, numeroCompte, solde, type, clientId);
        }
    }
    
    static class TransactionRecord {
        int id;
        double montant;
        String type;
        String statut;
        int compteSourceId;
        int compteDestId;
        long dateTransaction;
        
        TransactionRecord(int id, double montant, String type, String statut, int src, int dst) {
            this.id = id;
            this.montant = montant;
            this.type = type;
            this.statut = statut;
            this.compteSourceId = src;
            this.compteDestId = dst;
            this.dateTransaction = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            String dst = compteDestId > 0 ? String.valueOf(compteDestId) : "N/A";
            return String.format("│ ID: %-3d │ %10.2f€ │ %-15s │ %-10s │ From: %d → To: %s │",
                id, montant, type, statut, compteSourceId, dst);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("         FSTBANK - APPLICATION BANCAIRE EJB3 - SIMULATEUR BASE DE DONNÉES");
        System.out.println("=".repeat(100) + "\n");
        
        try {
            // STEP 1: Create Clients
            createClients();
            
            // STEP 2: Create Accounts
            createAccounts();
            
            // STEP 3: Perform Transactions
            performTransactions();
            
            // STEP 4: Display Database Contents
            displayDatabase();
            
            System.out.println("\n" + "=".repeat(100));
            System.out.println("                    ✅ ALL OPERATIONS COMPLETED SUCCESSFULLY!");
            System.out.println("=".repeat(100) + "\n");
            
        } catch (Exception e) {
            System.out.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createClients() {
        System.out.println("STEP 1: CREATING CLIENTS");
        System.out.println("-".repeat(100));
        
        // Client Particulier 1
        System.out.println("Creating ClientParticulier: Jean Dupont");
        ClientRecord c1 = new ClientRecord(Database.nextClientId++, "C001", "Dupont", "Jean", "jean@gmail.com", "PARTICULIER");
        Database.CLIENTS.add(c1);
        System.out.println("  ✓ Created and stored in DATABASE");
        
        // Client Particulier 2
        System.out.println("Creating ClientParticulier: Marie Martin");
        ClientRecord c2 = new ClientRecord(Database.nextClientId++, "C002", "Martin", "Marie", "marie@gmail.com", "PARTICULIER");
        Database.CLIENTS.add(c2);
        System.out.println("  ✓ Created and stored in DATABASE");
        
        // Client Professionnel
        System.out.println("Creating ClientProfessionnel: TechCorp SA");
        ClientRecord c3 = new ClientRecord(Database.nextClientId++, "CP001", "TechCorp", "SA", "contact@techcorp.dz", "PROFESSIONNEL");
        c3.siret = "12345678901234";
        Database.CLIENTS.add(c3);
        System.out.println("  ✓ Created and stored in DATABASE (SIRET: 12345678901234)");
        
        System.out.println("✅ Total clients created: " + Database.CLIENTS.size() + "\n");
    }
    
    private static void createAccounts() {
        System.out.println("STEP 2: CREATING ACCOUNTS");
        System.out.println("-".repeat(100));
        
        // Account for Client 1
        System.out.println("Creating CompteParticulierSimple for Jean Dupont");
        CompteRecord acc1 = new CompteRecord(Database.nextCompteId++, "ACC001", 5000.0, "PARTICULIER_SIMPLE", 1);
        Database.COMPTES.add(acc1);
        System.out.println("  ✓ Account ACC001 created with balance: 5000€");
        
        // Shared Account for Client 1 & 2
        System.out.println("Creating CompteParticulierPartage for Jean & Marie");
        CompteRecord acc2 = new CompteRecord(Database.nextCompteId++, "ACC002", 15000.0, "PARTICULIER_PARTAGE", 1);
        Database.COMPTES.add(acc2);
        System.out.println("  ✓ Account ACC002 created with balance: 15000€ (2 owners: C001, C002)");
        
        // Account for Client 2
        System.out.println("Creating CompteParticulierSimple for Marie Martin");
        CompteRecord acc3 = new CompteRecord(Database.nextCompteId++, "ACC003", 8000.0, "PARTICULIER_SIMPLE", 2);
        Database.COMPTES.add(acc3);
        System.out.println("  ✓ Account ACC003 created with balance: 8000€");
        
        // Professional Account
        System.out.println("Creating CompteProfessionnel for TechCorp");
        CompteRecord acc4 = new CompteRecord(Database.nextCompteId++, "ACC004", 100000.0, "PROFESSIONNEL", 3);
        Database.COMPTES.add(acc4);
        System.out.println("  ✓ Account ACC004 created with balance: 100000€");
        
        System.out.println("✅ Total accounts created: " + Database.COMPTES.size() + "\n");
    }
    
    private static void performTransactions() {
        System.out.println("STEP 3: PERFORMING TRANSACTIONS");
        System.out.println("-".repeat(100));
        
        // Transaction 1: Deposit
        System.out.println("Transaction 1: DEPOT of 2000€ to ACC001");
        Database.COMPTES.get(0).solde += 2000;
        TransactionRecord t1 = new TransactionRecord(Database.nextTransactionId++, 2000, "DEPOT", "VALIDEE", 1, -1);
        Database.TRANSACTIONS.add(t1);
        System.out.println("  ✓ 2000€ deposited | New balance ACC001: " + Database.COMPTES.get(0).solde + "€");
        
        // Transaction 2: Withdrawal
        System.out.println("Transaction 2: RETRAIT of 1000€ from ACC003");
        Database.COMPTES.get(2).solde -= 1000;
        TransactionRecord t2 = new TransactionRecord(Database.nextTransactionId++, 1000, "RETRAIT", "VALIDEE", 3, -1);
        Database.TRANSACTIONS.add(t2);
        System.out.println("  ✓ 1000€ withdrawn | New balance ACC003: " + Database.COMPTES.get(2).solde + "€");
        
        // Transaction 3: Transfer
        System.out.println("Transaction 3: VIREMENT of 5000€ from ACC001 to ACC004");
        Database.COMPTES.get(0).solde -= 5000;
        Database.COMPTES.get(3).solde += 5000;
        TransactionRecord t3 = new TransactionRecord(Database.nextTransactionId++, 5000, "VIREMENT", "VALIDEE", 1, 4);
        Database.TRANSACTIONS.add(t3);
        System.out.println("  ✓ 5000€ transferred | New balance ACC001: " + Database.COMPTES.get(0).solde + "€ | ACC004: " + Database.COMPTES.get(3).solde + "€");
        
        // Transaction 4: Failed Withdrawal (insufficient balance)
        System.out.println("Transaction 4: RETRAIT of 10000€ from ACC003 (INSUFFICIENT BALANCE)");
        TransactionRecord t4 = new TransactionRecord(Database.nextTransactionId++, 10000, "RETRAIT", "REJETEE", 3, -1);
        Database.TRANSACTIONS.add(t4);
        System.out.println("  ✗ Transaction REJECTED | Balance ACC003: " + Database.COMPTES.get(2).solde + "€ < 10000€");
        
        System.out.println("✅ Total transactions: " + Database.TRANSACTIONS.size() + "\n");
    }
    
    private static void displayDatabase() {
        System.out.println("STEP 4: DATABASE CONTENTS");
        System.out.println("-".repeat(100));
        
        // Display CLIENTS table
        System.out.println("\n📋 TABLE: CLIENTS");
        System.out.println("┌" + "─".repeat(98) + "┐");
        System.out.println("│ Records: " + Database.CLIENTS.size());
        System.out.println("├" + "─".repeat(98) + "┤");
        for (ClientRecord c : Database.CLIENTS) {
            System.out.println(c.toString());
        }
        System.out.println("└" + "─".repeat(98) + "┘");
        
        // Display COMPTES table
        System.out.println("\n📋 TABLE: COMPTES");
        System.out.println("┌" + "─".repeat(98) + "┐");
        System.out.println("│ Records: " + Database.COMPTES.size());
        System.out.println("├" + "─".repeat(98) + "┤");
        for (CompteRecord c : Database.COMPTES) {
            System.out.println(c.toString());
        }
        System.out.println("└" + "─".repeat(98) + "┘");
        
        // Display TRANSACTIONS table
        System.out.println("\n📋 TABLE: TRANSACTIONS");
        System.out.println("┌" + "─".repeat(98) + "┐");
        System.out.println("│ Records: " + Database.TRANSACTIONS.size());
        System.out.println("├" + "─".repeat(98) + "┤");
        for (TransactionRecord t : Database.TRANSACTIONS) {
            System.out.println(t.toString());
        }
        System.out.println("└" + "─".repeat(98) + "┘");
        
        // Display Summary
        System.out.println("\n📊 DATABASE SUMMARY");
        System.out.println("┌" + "─".repeat(98) + "┐");
        System.out.println("│ Total Clients:          " + Database.CLIENTS.size());
        System.out.println("│ Total Accounts:         " + Database.COMPTES.size());
        System.out.println("│ Total Transactions:     " + Database.TRANSACTIONS.size());
        
        double totalBalance = Database.COMPTES.stream().mapToDouble(c -> c.solde).sum();
        System.out.println("│ Total System Balance:   " + String.format("%.2f", totalBalance) + "€");
        
        long validTransactions = Database.TRANSACTIONS.stream().filter(t -> t.statut.equals("VALIDEE")).count();
        long rejectedTransactions = Database.TRANSACTIONS.stream().filter(t -> t.statut.equals("REJETEE")).count();
        System.out.println("│ Validated Transactions: " + validTransactions);
        System.out.println("│ Rejected Transactions:  " + rejectedTransactions);
        System.out.println("└" + "─".repeat(98) + "┘");
    }
}
