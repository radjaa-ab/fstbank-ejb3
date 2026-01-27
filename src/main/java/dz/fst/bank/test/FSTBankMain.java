package dz.fst.bank.test;

import dz.fst.bank.entities.*;
import dz.fst.bank.session.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FSTBankMain {
    
    private static GestionClientBeanRemote gestionClient;
    private static GestionCompteBeanRemote gestionCompte;
    private static GestionTransactionBeanRemote gestionTransaction;
    
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║       FSTBANK - TEST APPLICATION BANCAIRE EJB3        ║");
            System.out.println("╚════════════════════════════════════════════════════════╝\n");
            
            // 1. Initialiser les connexions EJB
            initialiserEJB();
            
            // 2. Test de création de clients
            testCreationClients();
            
            // 3. Test de création de comptes
            testCreationComptes();
            
            // 4. Test des opérations bancaires
            testOperationsBancaires();
            
            // 5. Test des comptes partagés
            testComptesPartages();
            
            // 6. Test de consultation d'historique
            testConsultationHistorique();
            
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║          TESTS TERMINÉS AVEC SUCCÈS !                 ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            
        } catch (Exception e) {
            System.err.println("❌ ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void initialiserEJB() throws NamingException {
        System.out.println("\n━━━━ 1. INITIALISATION DES EJBs ━━━━");
        
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, 
                 "org.jboss.naming.remote.client.InitialContextFactory");
        props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        props.put(Context.SECURITY_PRINCIPAL, "admin");
        props.put(Context.SECURITY_CREDENTIALS, "admin");
        
        Context context = new InitialContext(props);
        
        // Lookup des EJBs
        String appName = "FSTBank";
        String moduleName = "FSTBankEJB";
        
        String gestionClientJNDI = String.format(
            "ejb:%s/%s//%s!%s",
            appName, moduleName, "GestionClientBean",
            GestionClientBeanRemote.class.getName()
        );
        
        String gestionCompteJNDI = String.format(
            "ejb:%s/%s//%s!%s",
            appName, moduleName, "GestionCompteBean",
            GestionCompteBeanRemote.class.getName()
        );
        
        String gestionTransactionJNDI = String.format(
            "ejb:%s/%s//%s!%s",
            appName, moduleName, "GestionTransactionBean",
            GestionTransactionBeanRemote.class.getName()
        );
        
        gestionClient = (GestionClientBeanRemote) context.lookup(gestionClientJNDI);
        gestionCompte = (GestionCompteBeanRemote) context.lookup(gestionCompteJNDI);
        gestionTransaction = (GestionTransactionBeanRemote) context.lookup(gestionTransactionJNDI);
        
        System.out.println("✓ Connexions EJB établies avec succès");
    }
    
    private static void testCreationClients() {
        System.out.println("\n━━━━ 2. TEST CRÉATION DE CLIENTS ━━━━");
        
        try {
            // Créer des clients particuliers
            System.out.println("\n→ Création de clients particuliers...");
            Client client1 = gestionClient.creerClientParticulier(
                "CLIENT001", "BELHADJ", "Ahmed", "ahmed@email.com", "pass123"
            );
            System.out.println("✓ Client créé: " + client1);
            
            Client client2 = gestionClient.creerClientParticulier(
                "CLIENT002", "BENALI", "Fatima", "fatima@email.com", "pass456"
            );
            System.out.println("✓ Client créé: " + client2);
            
            Client client3 = gestionClient.creerClientParticulier(
                "CLIENT003", "KADDOUR", "Sofiane", "sofiane@email.com", "pass789"
            );
            System.out.println("✓ Client créé: " + client3);
            
            // Créer un client professionnel
            System.out.println("\n→ Création d'un client professionnel...");
            Client clientPro = gestionClient.creerClientProfessionnel(
                "ENTREPRISE001", "Tech Solutions SARL", "12345678901234", 
                "contact@techsolutions.com", "passEntreprise"
            );
            System.out.println("✓ Client professionnel créé: " + clientPro);
            
            // Lister tous les clients
            System.out.println("\n→ Liste de tous les clients:");
            List<Client> clients = gestionClient.listerTousLesClients();
            clients.forEach(c -> System.out.println("  • " + c.getNom() + 
                                                   " (" + c.getTypeClient() + ")"));
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création des clients: " + e.getMessage());
        }
    }
    
    private static void testCreationComptes() {
        System.out.println("\n━━━━ 3. TEST CRÉATION DE COMPTES ━━━━");
        
        try {
            // Récupérer les clients
            Client client1 = gestionClient.rechercherClient("CLIENT001");
            Client client2 = gestionClient.rechercherClient("CLIENT002");
            Client clientPro = gestionClient.rechercherClient("ENTREPRISE001");
            
            // Créer un compte particulier simple
            System.out.println("\n→ Création d'un compte particulier simple...");
            Compte compte1 = gestionCompte.creerCompteParticulierSimple(client1.getId());
            System.out.println("✓ Compte créé: " + compte1.getNumeroCompte() + 
                             " (Solde: " + compte1.getSolde() + " DA)");
            
            // Créer un autre compte particulier simple
            System.out.println("\n→ Création d'un second compte particulier simple...");
            Compte compte2 = gestionCompte.creerCompteParticulierSimple(client2.getId());
            System.out.println("✓ Compte créé: " + compte2.getNumeroCompte() + 
                             " (Solde: " + compte2.getSolde() + " DA)");
            
            // Créer un compte professionnel
            System.out.println("\n→ Création d'un compte professionnel...");
            Compte comptePro = gestionCompte.creerCompteProfessionnel(clientPro.getId());
            System.out.println("✓ Compte professionnel créé: " + comptePro.getNumeroCompte() + 
                             " (Solde: " + comptePro.getSolde() + " DA)");
            
            // Lister les comptes d'un client
            System.out.println("\n→ Comptes du client " + client1.getNom() + ":");
            List<Compte> comptes = gestionCompte.listerComptesClient(client1.getId());
            comptes.forEach(c -> System.out.println("  • " + c.getNumeroCompte() + 
                                                   " - Type: " + c.getTypeCompte()));
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création des comptes: " + e.getMessage());
        }
    }
    
    private static void testOperationsBancaires() {
        System.out.println("\n━━━━ 4. TEST OPÉRATIONS BANCAIRES ━━━━");
        
        try {
            // Récupérer les clients et leurs comptes
            Client client1 = gestionClient.rechercherClient("CLIENT001");
            Client client2 = gestionClient.rechercherClient("CLIENT002");
            
            List<Compte> comptes1 = gestionCompte.listerComptesClient(client1.getId());
            List<Compte> comptes2 = gestionCompte.listerComptesClient(client2.getId());
            
            Compte compte1 = comptes1.get(0);
            Compte compte2 = comptes2.get(0);
            
            // Test 1: Dépôt
            System.out.println("\n→ Test: Dépôt de 10000 DA sur compte " + compte1.getNumeroCompte());
            System.out.println("  Solde avant: " + gestionCompte.consulterSolde(compte1.getId()) + " DA");
            
            boolean depotOk = gestionTransaction.effectuerDepot(compte1.getId(), 10000.0);
            System.out.println("  Résultat: " + (depotOk ? "✓ SUCCÈS" : "❌ ÉCHEC"));
            System.out.println("  Solde après: " + gestionCompte.consulterSolde(compte1.getId()) + " DA");
            
            // Test 2: Dépôt sur le second compte
            System.out.println("\n→ Test: Dépôt de 5000 DA sur compte " + compte2.getNumeroCompte());
            System.out.println("  Solde avant: " + gestionCompte.consulterSolde(compte2.getId()) + " DA");
            
            depotOk = gestionTransaction.effectuerDepot(compte2.getId(), 5000.0);
            System.out.println("  Résultat: " + (depotOk ? "✓ SUCCÈS" : "❌ ÉCHEC"));
            System.out.println("  Solde après: " + gestionCompte.consulterSolde(compte2.getId()) + " DA");
            
            // Test 3: Retrait
            System.out.println("\n→ Test: Retrait de 2000 DA du compte " + compte1.getNumeroCompte());
            System.out.println("  Solde avant: " + gestionCompte.consulterSolde(compte1.getId()) + " DA");
            
            boolean retraitOk = gestionTransaction.effectuerRetrait(compte1.getId(), 2000.0);
            System.out.println("  Résultat: " + (retraitOk ? "✓ SUCCÈS" : "❌ ÉCHEC"));
            System.out.println("  Solde après: " + gestionCompte.consulterSolde(compte1.getId()) + " DA");
            
            // Test 4: Virement
            System.out.println("\n→ Test: Virement de 3000 DA de " + compte1.getNumeroCompte() + 
                             " vers " + compte2.getNumeroCompte());
            System.out.println("  Solde compte source avant: " + 
                             gestionCompte.consulterSolde(compte1.getId()) + " DA");
            System.out.println("  Solde compte destination avant: " + 
                             gestionCompte.consulterSolde(compte2.getId()) + " DA");
            
            boolean virementOk = gestionTransaction.effectuerVirement(
                compte1.getId(), compte2.getId(), 3000.0
            );
            System.out.println("  Résultat: " + (virementOk ? "✓ SUCCÈS" : "❌ ÉCHEC"));
            System.out.println("  Solde compte source après: " + 
                             gestionCompte.consulterSolde(compte1.getId()) + " DA");
            System.out.println("  Solde compte destination après: " + 
                             gestionCompte.consulterSolde(compte2.getId()) + " DA");
            
            // Test 5: Retrait avec solde insuffisant
            System.out.println("\n→ Test: Retrait avec solde insuffisant (20000 DA)");
            System.out.println("  Solde avant: " + gestionCompte.consulterSolde(compte1.getId()) + " DA");
            
            retraitOk = gestionTransaction.effectuerRetrait(compte1.getId(), 20000.0);
            System.out.println("  Résultat: " + (retraitOk ? "✓ SUCCÈS" : "❌ ÉCHEC (ATTENDU)"));
            System.out.println("  Solde après: " + gestionCompte.consulterSolde(compte1.getId()) + " DA");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors des opérations bancaires: " + e.getMessage());
        }
    }
    
    private static void testComptesPartages() {
        System.out.println("\n━━━━ 5. TEST COMPTES PARTAGÉS ━━━━");
        
        try {
            // Récupérer plusieurs clients
            Client client1 = gestionClient.rechercherClient("CLIENT001");
            Client client2 = gestionClient.rechercherClient("CLIENT002");
            Client client3 = gestionClient.rechercherClient("CLIENT003");
            
            // Créer un compte partagé
            System.out.println("\n→ Création d'un compte partagé entre 3 clients...");
            List<Long> proprietairesIds = Arrays.asList(
                client1.getId(), 
                client2.getId(), 
                client3.getId()
            );
            
            Compte comptePartage = gestionCompte.creerCompteParticulierPartage(proprietairesIds);
            System.out.println("✓ Compte partagé créé: " + comptePartage.getNumeroCompte());
            System.out.println("  Nombre de propriétaires: " + 
                             comptePartage.getProprietaires().size());
            
            // Effectuer un dépôt sur le compte partagé
            System.out.println("\n→ Dépôt de 15000 DA sur le compte partagé...");
            boolean depotOk = gestionTransaction.effectuerDepot(comptePartage.getId(), 15000.0);
            System.out.println("  Résultat: " + (depotOk ? "✓ SUCCÈS" : "❌ ÉCHEC"));
            System.out.println("  Nouveau solde: " + 
                             gestionCompte.consulterSolde(comptePartage.getId()) + " DA");
            
            // Test de la limite de 10 propriétaires
            System.out.println("\n→ Test de la limite de 10 propriétaires...");
            try {
                for (int i = 4; i <= 11; i++) {
                    Client nouveauClient = gestionClient.creerClientParticulier(
                        "TEMP" + i, "Nom" + i, "Prenom" + i, 
                        "email" + i + "@test.com", "pass"
                    );
                    gestionCompte.ajouterProprietaire(comptePartage.getId(), nouveauClient.getId());
                    System.out.println("  • Propriétaire " + i + " ajouté");
                }
            } catch (Exception e) {
                System.out.println("  ✓ Limite atteinte comme prévu: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test des comptes partagés: " + e.getMessage());
        }
    }
    
    private static void testConsultationHistorique() {
        System.out.println("\n━━━━ 6. TEST CONSULTATION HISTORIQUE ━━━━");
        
        try {
            // Récupérer un client et son compte
            Client client1 = gestionClient.rechercherClient("CLIENT001");
            List<Compte> comptes = gestionCompte.listerComptesClient(client1.getId());
            
            if (!comptes.isEmpty()) {
                Compte compte = comptes.get(0);
                
                System.out.println("\n→ Historique des transactions du compte " + 
                                 compte.getNumeroCompte() + ":");
                
                List<Transaction> historique = gestionTransaction.consulterHistorique(compte.getId());
                
                if (historique.isEmpty()) {
                    System.out.println("  • Aucune transaction");
                } else {
                    System.out.println("  Nombre total de transactions: " + historique.size());
                    System.out.println("\n  Détails des transactions:");
                    System.out.println("  " + "=".repeat(80));
                    
                    for (Transaction t : historique) {
                        System.out.printf("  | %-19s | %-20s | %10.2f DA | %-10s |%n",
                            t.getDateTransaction(),
                            t.getTypeOperation(),
                            t.getMontant(),
                            t.getStatut()
                        );
                        if (t.getDescription() != null) {
                            System.out.println("    Description: " + t.getDescription());
                        }
                    }
                    System.out.println("  " + "=".repeat(80));
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la consultation de l'historique: " + 
                             e.getMessage());
        }
    }
}
