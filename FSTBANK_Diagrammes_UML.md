# 📊 DIAGRAMMES UML - PROJET FSTBANK

## Application de Gestion Bancaire - Architecture EJB3 3-TIERS

---

## 📑 TABLE DES MATIÈRES

1. [Diagramme de Classes - Entités](#1-diagramme-de-classes---entités)
2. [Diagramme d'Architecture 3-Tiers](#2-diagramme-darchitecture-3-tiers)
3. [Diagramme Factory Pattern](#3-diagramme-factory-pattern)
4. [Diagramme Strategy Pattern](#4-diagramme-strategy-pattern)
5. [Diagramme Observer Pattern](#5-diagramme-observer-pattern)
6. [Diagramme de Séquence - Créer Client](#6-diagramme-de-séquence---créer-client)
7. [Diagramme de Séquence - Effectuer Retrait](#7-diagramme-de-séquence---effectuer-retrait)
8. [Diagramme de Séquence - Notification Client](#8-diagramme-de-séquence---notification-client)
9. [Diagramme Entité-Relation](#9-diagramme-entité-relation)
10. [Diagramme de Déploiement](#10-diagramme-de-déploiement)

---

## 1. DIAGRAMME DE CLASSES - ENTITÉS

### Description
Ce diagramme présente la structure complète des entités du système FSTBank, incluant les hiérarchies d'héritage pour les clients et les comptes, ainsi que les relations JPA entre les entités.

```mermaid
classDiagram
    %% Classes abstraites
    class Client {
        <<abstract>>
        -Long id
        -String identifiant
        -String nom
        -String prenom
        -String email
        -Date dateCreation
        +getId() Long
        +getIdentifiant() String
        +getNom() String
        +getPrenom() String
        +getEmail() String
    }

    class Compte {
        <<abstract>>
        -Long id
        -String numeroCompte
        -double solde
        -Date dateCreation
        -List~ObservateurCompte~ observateurs
        +getId() Long
        +getNumeroCompte() String
        +getSolde() double
        +crediter(montant) void
        +debiter(montant) void
        +ajouterObservateur(obs) void
        +retirerObservateur(obs) void
        +notifierObservateurs(msg) void
    }

    %% Héritage Client
    class ClientParticulier {
        +ClientParticulier()
        +toString() String
    }

    class ClientProfessionnel {
        -String siret
        +ClientProfessionnel()
        +getSiret() String
        +setSiret(siret) void
        +toString() String
    }

    %% Héritage Compte
    class CompteParticulierSimple {
        -Client proprietaire
        +CompteParticulierSimple()
        +getProprietaire() Client
        +setProprietaire(client) void
    }

    class CompteParticulierPartage {
        -List~Client~ proprietaires
        +CompteParticulierPartage()
        +getProprietaires() List~Client~
        +ajouterProprietaire(client) void
        +retirerProprietaire(client) void
    }

    class CompteProfessionnel {
        -ClientProfessionnel proprietaire
        +CompteProfessionnel()
        +getProprietaire() ClientProfessionnel
        +setProprietaire(client) void
    }

    %% Transaction
    class Transaction {
        -Long id
        -double montant
        -TypeOperation typeOperation
        -StatutTransaction statut
        -Compte compteSource
        -Compte compteDestination
        -Date dateTransaction
        +Transaction()
        +getId() Long
        +getMontant() double
        +getTypeOperation() TypeOperation
        +getStatut() StatutTransaction
        +valider() void
        +rejeter() void
        +annuler() void
    }

    %% Enums
    class TypeOperation {
        <<enumeration>>
        RETRAIT
        DEPOT
        VIREMENT
        VIREMENT_INTERNATIONAL
    }

    class StatutTransaction {
        <<enumeration>>
        EN_COURS
        VALIDEE
        REJETEE
        ANNULEE
    }

    %% Banquier
    class Banquier {
        -Long id
        -String identifiant
        -String nom
        -String prenom
        -String email
        -String motDePasse
        -String matricule
        -Date dateEmbauche
        +Banquier()
        +getId() Long
        +getMatricule() String
        +authentifier(password) boolean
    }

    %% Interfaces Observer Pattern
    class ObservableCompte {
        <<interface>>
        +ajouterObservateur(obs) void
        +retirerObservateur(obs) void
        +notifierObservateurs(msg) void
    }

    class ObservateurCompte {
        <<interface>>
        +mettreAJour(message) void
    }

    class NotificationClient {
        -Client client
        -List~String~ notifications
        -boolean notificationsActives
        +NotificationClient(client)
        +mettreAJour(message) void
        +activerNotifications() void
        +desactiverNotifications() void
        +afficherHistoriqueNotifications() void
    }

    %% Relations d'héritage
    Client <|-- ClientParticulier
    Client <|-- ClientProfessionnel
    Compte <|-- CompteParticulierSimple
    Compte <|-- CompteParticulierPartage
    Compte <|-- CompteProfessionnel

    %% Implémentation interfaces
    Compte ..|> ObservableCompte
    NotificationClient ..|> ObservateurCompte

    %% Relations JPA
    CompteParticulierSimple "1" --> "1" Client : proprietaire
    CompteParticulierPartage "1" --> "*" Client : proprietaires (max 10)
    CompteProfessionnel "1" --> "1" ClientProfessionnel : proprietaire
    
    Transaction "*" --> "1" Compte : compteSource
    Transaction "*" --> "0..1" Compte : compteDestination
    Transaction --> TypeOperation
    Transaction --> StatutTransaction
    
    Compte "1" --> "*" ObservateurCompte : observateurs
    NotificationClient --> Client

    %% Style
    style Client fill:#4A90E2,stroke:#2E5C8A,color:#fff
    style Compte fill:#4A90E2,stroke:#2E5C8A,color:#fff
    style Transaction fill:#4A90E2,stroke:#2E5C8A,color:#fff
    style Banquier fill:#4A90E2,stroke:#2E5C8A,color:#fff
    style ObservableCompte fill:#E74C3C,stroke:#C0392B,color:#fff
    style ObservateurCompte fill:#E74C3C,stroke:#C0392B,color:#fff
    style TypeOperation fill:#F39C12,stroke:#D68910,color:#fff
    style StatutTransaction fill:#F39C12,stroke:#D68910,color:#fff
```

### Légende
- 🔵 **Bleu** : Classes entités (@Entity)
- 🔴 **Rouge** : Interfaces
- 🟠 **Orange** : Énumérations (Enums)
- **Flèches pleines** : Héritage
- **Flèches pointillées** : Implémentation d'interface
- **Flèches simples** : Associations JPA

---

## 2. DIAGRAMME D'ARCHITECTURE 3-TIERS

### Description
Ce diagramme illustre l'architecture complète de l'application FSTBank en trois couches distinctes : présentation, logique métier (EJB Session Beans), et persistance (JPA/H2).

```mermaid
graph TB
    subgraph "TIER 3 - PRESENTATION LAYER"
        CLIENT[FSTBankMain<br/>Test Client]
        REMOTE1["@Remote<br/>GestionClientRemote"]
        REMOTE2["@Remote<br/>GestionCompteRemote"]
        REMOTE3["@Remote<br/>GestionTransactionRemote"]
        REMOTE4["@Remote<br/>GestionNotificationRemote"]
    end

    subgraph "WILDFLY SERVER"
        subgraph "TIER 2 - BUSINESS LOGIC (EJB Container)"
            BEAN1["@Stateless<br/>GestionClientBean"]
            BEAN2["@Stateless<br/>GestionCompteBean"]
            BEAN3["@Stateless<br/>GestionTransactionBean"]
            BEAN4["@Stateless<br/>GestionNotificationBean"]
            
            subgraph "Design Patterns"
                FACTORY1[ClientFactory]
                FACTORY2[CompteFactory]
                MANAGER[GestionnaireTransaction<br/>Strategy Pattern]
            end
        end

        subgraph "JPA / HIBERNATE"
            EM[EntityManager]
            PERSISTENCE[persistence.xml]
        end
    end

    subgraph "TIER 1 - DATABASE LAYER"
        DB[(H2 Database<br/>jdbc:h2:mem:fstbankdb)]
        
        subgraph "Tables"
            T1[CLIENTS]
            T2[COMPTES]
            T3[COMPTES_PARTAGES]
            T4[TRANSACTIONS]
            T5[BANQUIERS]
        end
    end

    %% Connexions Tier 3 -> Tier 2
    CLIENT -->|JNDI Lookup| REMOTE1
    CLIENT -->|JNDI Lookup| REMOTE2
    CLIENT -->|JNDI Lookup| REMOTE3
    CLIENT -->|JNDI Lookup| REMOTE4

    REMOTE1 -.->|implements| BEAN1
    REMOTE2 -.->|implements| BEAN2
    REMOTE3 -.->|implements| BEAN3
    REMOTE4 -.->|implements| BEAN4

    %% Connexions Beans -> Patterns
    BEAN1 -->|uses| FACTORY1
    BEAN2 -->|uses| FACTORY2
    BEAN3 -->|uses| MANAGER

    %% Connexions Beans -> JPA
    BEAN1 -->|@PersistenceContext| EM
    BEAN2 -->|@PersistenceContext| EM
    BEAN3 -->|@PersistenceContext| EM
    BEAN4 -->|@PersistenceContext| EM

    EM -->|configured by| PERSISTENCE

    %% Connexions JPA -> Database
    EM -->|CRUD Operations| DB
    DB --> T1
    DB --> T2
    DB --> T3
    DB --> T4
    DB --> T5

    %% Styles
    style CLIENT fill:#9B59B6,stroke:#6C3483,color:#fff
    style BEAN1 fill:#27AE60,stroke:#1E8449,color:#fff
    style BEAN2 fill:#27AE60,stroke:#1E8449,color:#fff
    style BEAN3 fill:#27AE60,stroke:#1E8449,color:#fff
    style BEAN4 fill:#27AE60,stroke:#1E8449,color:#fff
    style FACTORY1 fill:#F39C12,stroke:#D68910,color:#fff
    style FACTORY2 fill:#F39C12,stroke:#D68910,color:#fff
    style MANAGER fill:#F39C12,stroke:#D68910,color:#fff
    style EM fill:#3498DB,stroke:#2471A3,color:#fff
    style DB fill:#E74C3C,stroke:#C0392B,color:#fff
```

### Composants clés

**TIER 3 - Présentation**
- FSTBankMain : Client de test utilisant JNDI pour accéder aux beans
- Interfaces @Remote : Contrats exposés pour l'accès distant

**TIER 2 - Logique Métier**
- 4 Session Beans @Stateless avec injection @PersistenceContext
- Design Patterns intégrés (Factory, Strategy, Observer)
- Gestion transactionnelle automatique (@TransactionAttribute)

**TIER 1 - Persistance**
- Base H2 en mémoire
- 5 tables relationnelles avec contraintes d'intégrité
- Gestion via JPA/Hibernate

---

## 3. DIAGRAMME FACTORY PATTERN

### Description
Le pattern Factory est utilisé pour créer les clients et les comptes avec validation métier intégrée (SIRET, limites, contraintes).

```mermaid
classDiagram
    class ClientFactory {
        <<Factory>>
        +createClientParticulier(nom, prenom, email) ClientParticulier$
        +createClientProfessionnel(nom, entreprise, siret, email) ClientProfessionnel$
        -validerSiret(siret) boolean$
        -validerEmail(email) boolean$
        -genererIdentifiant() String$
    }

    class CompteFactory {
        <<Factory>>
        +createCompteParticulierSimple(client) CompteParticulierSimple$
        +createCompteParticulierPartage(clients) CompteParticulierPartage$
        +createCompteProfessionnel(client) CompteProfessionnel$
        -genererNumeroCompte() String$
        -validerProprietaires(clients) boolean$
    }

    class Client {
        <<abstract>>
        -Long id
        -String identifiant
        -String nom
        -String prenom
        -String email
        -Date dateCreation
    }

    class ClientParticulier {
        +ClientParticulier()
    }

    class ClientProfessionnel {
        -String siret
        +getSiret() String
    }

    class Compte {
        <<abstract>>
        -Long id
        -String numeroCompte
        -double solde
        -Date dateCreation
    }

    class CompteParticulierSimple {
        -Client proprietaire
    }

    class CompteParticulierPartage {
        -List~Client~ proprietaires
        +MAX_PROPRIETAIRES = 10
    }

    class CompteProfessionnel {
        -ClientProfessionnel proprietaire
    }

    %% Relations Factory -> Products
    ClientFactory ..> ClientParticulier : <<creates>>
    ClientFactory ..> ClientProfessionnel : <<creates>>
    CompteFactory ..> CompteParticulierSimple : <<creates>>
    CompteFactory ..> CompteParticulierPartage : <<creates>>
    CompteFactory ..> CompteProfessionnel : <<creates>>

    %% Héritage
    Client <|-- ClientParticulier
    Client <|-- ClientProfessionnel
    Compte <|-- CompteParticulierSimple
    Compte <|-- CompteParticulierPartage
    Compte <|-- CompteProfessionnel

    %% Dépendances
    CompteFactory --> Client : uses
    CompteFactory --> ClientProfessionnel : uses

    %% Styles
    style ClientFactory fill:#F39C12,stroke:#D68910,color:#fff
    style CompteFactory fill:#F39C12,stroke:#D68910,color:#fff
    style Client fill:#4A90E2,stroke:#2E5C8A,color:#fff
    style Compte fill:#4A90E2,stroke:#2E5C8A,color:#fff
```

### Règles de validation

**ClientFactory**
- ✅ SIRET : exactement 14 chiffres
- ✅ Email : format valide (regex)
- ✅ Nom/Prénom : non vides
- ✅ Identifiant unique auto-généré

**CompteFactory**
- ✅ Numéro de compte unique (format: FR76 XXXX XXXX XXXX XXXX)
- ✅ Compte partagé : minimum 2, maximum 10 propriétaires
- ✅ Compte professionnel : uniquement pour ClientProfessionnel
- ✅ Solde initial : 0.0€

---

## 4. DIAGRAMME STRATEGY PATTERN

### Description
Le pattern Strategy permet de gérer différents types d'opérations bancaires (retrait, dépôt, virement) avec leurs propres règles de validation et d'exécution.

```mermaid
classDiagram
    class StrategieOperation {
        <<interface>>
        +executer() Transaction
        +valider() boolean
        +getTypeOperation() TypeOperation
    }

    class StrategieRetrait {
        -Compte compte
        -double montant
        +StrategieRetrait(compte, montant)
        +executer() Transaction
        +valider() boolean
        +getTypeOperation() TypeOperation
    }

    class StrategieDepot {
        -Compte compte
        -double montant
        -double MONTANT_MAX = 50000.0
        +StrategieDepot(compte, montant)
        +executer() Transaction
        +valider() boolean
        +getTypeOperation() TypeOperation
    }

    class StrategieVirement {
        -Compte compteSource
        -Compte compteDestination
        -double montant
        +StrategieVirement(source, destination, montant)
        +executer() Transaction
        +valider() boolean
        +getTypeOperation() TypeOperation
    }

    class GestionnaireTransaction {
        -Map~TypeOperation, StrategieOperation~ strategies
        +GestionnaireTransaction()
        +enregistrerStrategie(type, strategie) void
        +executerOperation(type, params) Transaction
        +getStrategie(type) StrategieOperation
    }

    class Transaction {
        -Long id
        -double montant
        -TypeOperation typeOperation
        -StatutTransaction statut
        -Compte compteSource
        -Compte compteDestination
        -Date dateTransaction
    }

    class TypeOperation {
        <<enumeration>>
        RETRAIT
        DEPOT
        VIREMENT
        VIREMENT_INTERNATIONAL
    }

    class StatutTransaction {
        <<enumeration>>
        EN_COURS
        VALIDEE
        REJETEE
        ANNULEE
    }

    %% Implémentation de l'interface
    StrategieOperation <|.. StrategieRetrait : implements
    StrategieOperation <|.. StrategieDepot : implements
    StrategieOperation <|.. StrategieVirement : implements

    %% Utilisation par le gestionnaire
    GestionnaireTransaction o-- StrategieOperation : strategies
    GestionnaireTransaction ..> Transaction : creates
    
    %% Relations
    StrategieOperation --> TypeOperation
    Transaction --> TypeOperation
    Transaction --> StatutTransaction

    %% Styles
    style StrategieOperation fill:#E74C3C,stroke:#C0392B,color:#fff
    style GestionnaireTransaction fill:#27AE60,stroke:#1E8449,color:#fff
    style StrategieRetrait fill:#F39C12,stroke:#D68910,color:#fff
    style StrategieDepot fill:#F39C12,stroke:#D68910,color:#fff
    style StrategieVirement fill:#F39C12,stroke:#D68910,color:#fff
    style Transaction fill:#4A90E2,stroke:#2E5C8A,color:#fff
```

### Règles métier par stratégie

**StrategieRetrait**
- ✅ Vérifier solde suffisant
- ✅ Montant > 0
- ✅ Débiter le compte source
- ✅ Créer transaction avec statut VALIDEE ou REJETEE

**StrategieDepot**
- ✅ Montant > 0 et ≤ 50,000€ (limite anti-blanchiment)
- ✅ Créditer le compte
- ✅ Créer transaction VALIDEE

**StrategieVirement**
- ✅ Solde suffisant sur compte source
- ✅ Montant > 0
- ✅ Débiter source, créditer destination
- ✅ Transaction atomique (tout ou rien)

---

## 5. DIAGRAMME OBSERVER PATTERN

### Description
Le pattern Observer permet aux clients de recevoir des notifications en temps réel sur les opérations effectuées sur leurs comptes (dépôts, retraits, virements).

```mermaid
classDiagram
    class ObservableCompte {
        <<interface>>
        +ajouterObservateur(obs ObservateurCompte) void
        +retirerObservateur(obs ObservateurCompte) void
        +notifierObservateurs(message String) void
    }

    class ObservateurCompte {
        <<interface>>
        +mettreAJour(message String) void
    }

    class Compte {
        <<abstract>>
        -Long id
        -String numeroCompte
        -double solde
        -List~ObservateurCompte~ observateurs
        +Compte()
        +ajouterObservateur(obs) void
        +retirerObservateur(obs) void
        +notifierObservateurs(message) void
        +crediter(montant) void
        +debiter(montant) void
    }

    class CompteParticulierSimple {
        -Client proprietaire
        +CompteParticulierSimple()
    }

    class CompteParticulierPartage {
        -List~Client~ proprietaires
        +CompteParticulierPartage()
    }

    class CompteProfessionnel {
        -ClientProfessionnel proprietaire
        +CompteProfessionnel()
    }

    class NotificationClient {
        -Client client
        -List~String~ notifications
        -boolean notificationsActives
        -Date dateCreation
        +NotificationClient(client)
        +mettreAJour(message) void
        +activerNotifications() void
        +desactiverNotifications() void
        +afficherHistoriqueNotifications() void
        +getNotifications() List~String~
    }

    class Client {
        -Long id
        -String identifiant
        -String nom
        -String prenom
        -String email
    }

    class GestionNotificationBean {
        <<Stateless>>
        +creerNotification(client) NotificationClient
        +activerNotifications(notification) void
        +desactiverNotifications(notification) void
        +afficherHistorique(notification) void
    }

    %% Implémentations
    Compte ..|> ObservableCompte : implements
    NotificationClient ..|> ObservateurCompte : implements

    %% Héritage Compte
    Compte <|-- CompteParticulierSimple
    Compte <|-- CompteParticulierPartage
    Compte <|-- CompteProfessionnel

    %% Relations Observer Pattern
    ObservableCompte "1" --> "*" ObservateurCompte : notifie
    Compte "1" o-- "*" ObservateurCompte : observateurs
    NotificationClient --> Client : client

    %% Bean de gestion
    GestionNotificationBean ..> NotificationClient : manages

    %% Styles
    style ObservableCompte fill:#E74C3C,stroke:#C0392B,color:#fff
    style ObservateurCompte fill:#E74C3C,stroke:#C0392B,color:#fff
    style Compte fill:#4A90E2,stroke:#2E5C8A,color:#fff
    style NotificationClient fill:#F39C12,stroke:#D68910,color:#fff
    style GestionNotificationBean fill:#27AE60,stroke:#1E8449,color:#fff
```

### Flux de notification

1. **Enregistrement** : Le client active les notifications via `GestionNotificationBean`
2. **Ajout observateur** : `NotificationClient` est ajouté à la liste des observateurs du compte
3. **Opération** : Une opération (retrait/dépôt/virement) est effectuée sur le compte
4. **Notification** : Le compte appelle `notifierObservateurs("Retrait de 100€")`
5. **Mise à jour** : Tous les observateurs reçoivent l'alerte via `mettreAJour()`
6. **Historique** : Les notifications sont stockées et consultables

---

## 6. DIAGRAMME DE SÉQUENCE - CRÉER CLIENT

### Description
Ce diagramme illustre le processus complet de création d'un client professionnel, incluant la validation du SIRET et la persistance en base de données.

```mermaid
sequenceDiagram
    actor Banquier
    participant GestionClientBean
    participant ClientFactory
    participant ClientProfessionnel
    participant EntityManager
    participant Database

    Banquier->>GestionClientBean: creerClientProfessionnel(nom, entreprise, siret, email)
    activate GestionClientBean
    
    GestionClientBean->>ClientFactory: createClientProfessionnel(nom, entreprise, siret, email)
    activate ClientFactory
    
    ClientFactory->>ClientFactory: validerSiret(siret)
    Note over ClientFactory: Vérifie 14 chiffres
    
    alt SIRET invalide
        ClientFactory-->>GestionClientBean: throw IllegalArgumentException
        GestionClientBean-->>Banquier: Erreur: SIRET invalide
    else SIRET valide
        ClientFactory->>ClientFactory: validerEmail(email)
        ClientFactory->>ClientFactory: genererIdentifiant()
        
        ClientFactory->>ClientProfessionnel: new ClientProfessionnel()
        activate ClientProfessionnel
        ClientProfessionnel-->>ClientFactory: instance
        deactivate ClientProfessionnel
        
        ClientFactory->>ClientProfessionnel: setNom(nom)
        ClientFactory->>ClientProfessionnel: setPrenom(entreprise)
        ClientFactory->>ClientProfessionnel: setSiret(siret)
        ClientFactory->>ClientProfessionnel: setEmail(email)
        ClientFactory->>ClientProfessionnel: setDateCreation(new Date())
        
        ClientFactory-->>GestionClientBean: clientProfessionnel
        deactivate ClientFactory
        
        GestionClientBean->>EntityManager: persist(clientProfessionnel)
        activate EntityManager
        EntityManager->>Database: INSERT INTO CLIENTS VALUES(...)
        activate Database
        Database-->>EntityManager: OK
        deactivate Database
        EntityManager-->>GestionClientBean: client persisté
        deactivate EntityManager
        
        GestionClientBean-->>Banquier: ClientProfessionnel créé avec succès
    end
    
    deactivate GestionClientBean
```

### Étapes clés

1. **Demande** : Le banquier demande la création d'un client professionnel
2. **Validation SIRET** : La factory vérifie que le SIRET contient exactement 14 chiffres
3. **Validation Email** : Vérification du format email
4. **Génération ID** : Création d'un identifiant unique
5. **Instanciation** : Création de l'objet ClientProfessionnel
6. **Persistance** : Sauvegarde via EntityManager dans la base H2
7. **Confirmation** : Retour au banquier avec succès ou erreur

---

## 7. DIAGRAMME DE SÉQUENCE - EFFECTUER RETRAIT

### Description
Ce diagramme montre le flux complet d'un retrait bancaire, incluant la sélection de la stratégie, la validation, l'exécution, et la notification des observateurs.

```mermaid
sequenceDiagram
    actor Client
    participant GestionTransactionBean
    participant GestionnaireTransaction
    participant StrategieRetrait
    participant Compte
    participant Transaction
    participant NotificationClient
    participant EntityManager
    participant Database

    Client->>GestionTransactionBean: effectuerRetrait(compteId, montant)
    activate GestionTransactionBean
    
    GestionTransactionBean->>GestionTransactionBean: findCompte(compteId)
    
    GestionTransactionBean->>GestionnaireTransaction: executerOperation(RETRAIT, compte, montant)
    activate GestionnaireTransaction
    
    GestionnaireTransaction->>GestionnaireTransaction: getStrategie(RETRAIT)
    GestionnaireTransaction->>StrategieRetrait: new StrategieRetrait(compte, montant)
    activate StrategieRetrait
    
    GestionnaireTransaction->>StrategieRetrait: valider()
    StrategieRetrait->>Compte: getSolde()
    Compte-->>StrategieRetrait: solde actuel
    
    alt Solde insuffisant
        StrategieRetrait-->>GestionnaireTransaction: false
        GestionnaireTransaction->>Transaction: new Transaction(REJETEE)
        Transaction-->>GestionnaireTransaction: transaction rejetée
        GestionnaireTransaction-->>GestionTransactionBean: Transaction REJETEE
        GestionTransactionBean-->>Client: Erreur: Solde insuffisant
    else Solde suffisant
        StrategieRetrait-->>GestionnaireTransaction: true
        
        GestionnaireTransaction->>StrategieRetrait: executer()
        StrategieRetrait->>Compte: debiter(montant)
        activate Compte
        Compte->>Compte: solde -= montant
        
        Compte->>Compte: notifierObservateurs("Retrait de " + montant + "€")
        loop Pour chaque observateur
            Compte->>NotificationClient: mettreAJour("Retrait de X€")
            activate NotificationClient
            NotificationClient->>NotificationClient: notifications.add(message)
            NotificationClient-->>Compte: OK
            deactivate NotificationClient
        end
        
        Compte-->>StrategieRetrait: compte débité
        deactivate Compte
        
        StrategieRetrait->>Transaction: new Transaction(VALIDEE)
        activate Transaction
        Transaction->>Transaction: setMontant(montant)
        Transaction->>Transaction: setTypeOperation(RETRAIT)
        Transaction->>Transaction: setStatut(VALIDEE)
        Transaction->>Transaction: setCompteSource(compte)
        Transaction-->>StrategieRetrait: transaction
        deactivate Transaction
        
        StrategieRetrait-->>GestionnaireTransaction: transaction
        deactivate StrategieRetrait
        
        GestionnaireTransaction-->>GestionTransactionBean: transaction
        deactivate GestionnaireTransaction
        
        GestionTransactionBean->>EntityManager: persist(transaction)
        activate EntityManager
        EntityManager->>Database: INSERT INTO TRANSACTIONS
        activate Database
        Database-->>EntityManager: OK
        deactivate Database
        EntityManager-->>GestionTransactionBean: transaction persistée
        deactivate EntityManager
        
        GestionTransactionBean-->>Client: Retrait effectué avec succès
    end
    
    deactivate GestionTransactionBean
```

### Flux décisionnel

**Validation (StrategieRetrait)**
- ✅ Montant > 0
- ✅ Solde ≥ montant
- ❌ Si échec → Transaction REJETEE

**Exécution**
- Débit du compte
- Création de la transaction
- Notification des observateurs

**Persistance**
- Sauvegarde de la transaction
- Mise à jour du solde en base

---

## 8. DIAGRAMME DE SÉQUENCE - NOTIFICATION CLIENT

### Description
Ce diagramme détaille le mécanisme de notification en temps réel lorsqu'une opération est effectuée sur un compte.

```mermaid
sequenceDiagram
    actor Client
    participant Compte
    participant NotificationClient1 as NotificationClient (Client 1)
    participant NotificationClient2 as NotificationClient (Client 2)
    participant GestionNotificationBean

    Note over Compte: Opération effectuée<br/>(Dépôt de 1000€)
    
    Compte->>Compte: notifierObservateurs("Dépôt de 1000€")
    activate Compte
    
    Note over Compte: Parcours de la liste<br/>des observateurs
    
    loop Pour chaque observateur
        Compte->>NotificationClient1: mettreAJour("Dépôt de 1000€")
        activate NotificationClient1
        
        alt Notifications actives
            NotificationClient1->>NotificationClient1: notifications.add("Dépôt de 1000€")
            NotificationClient1->>NotificationClient1: ajouterTimestamp()
            Note over NotificationClient1: [27/01/2026 14:32]<br/>Dépôt de 1000€
            NotificationClient1-->>Compte: Notification enregistrée
        else Notifications désactivées
            NotificationClient1-->>Compte: Notification ignorée
        end
        deactivate NotificationClient1
        
        Compte->>NotificationClient2: mettreAJour("Dépôt de 1000€")
        activate NotificationClient2
        NotificationClient2->>NotificationClient2: notifications.add("Dépôt de 1000€")
        NotificationClient2-->>Compte: OK
        deactivate NotificationClient2
    end
    
    Compte-->>Compte: Tous les observateurs notifiés
    deactivate Compte
    
    Note over Client: Le client consulte<br/>son historique
    
    Client->>GestionNotificationBean: afficherHistorique(notificationClient)
    activate GestionNotificationBean
    GestionNotificationBean->>NotificationClient1: afficherHistoriqueNotifications()
    activate NotificationClient1
    
    NotificationClient1->>NotificationClient1: getNotifications()
    NotificationClient1-->>GestionNotificationBean: Liste des notifications
    deactivate NotificationClient1
    
    GestionNotificationBean-->>Client: Affichage de l'historique
    deactivate GestionNotificationBean
    
    Note over Client: Historique affiché:<br/>- [27/01/2026 14:32] Dépôt de 1000€<br/>- [27/01/2026 12:15] Retrait de 50€<br/>- [26/01/2026 18:45] Virement reçu 200€
```

### Types de notifications

**Notifications automatiques**
- 💰 Dépôt effectué
- 💸 Retrait effectué
- 🔄 Virement reçu
- 🔄 Virement envoyé
- ⚠️ Solde bas (< 100€)
- ❌ Opération rejetée

**Gestion des notifications**
- `activerNotifications()` : Active les alertes
- `desactiverNotifications()` : Désactive temporairement
- `afficherHistoriqueNotifications()` : Consultation de l'historique
- Conservation illimitée dans la liste

---

## 9. DIAGRAMME ENTITÉ-RELATION

### Description
Ce diagramme ER montre la structure complète de la base de données H2 avec toutes les tables, colonnes, clés primaires/étrangères et contraintes d'intégrité.

```mermaid
erDiagram
    CLIENTS {
        BIGINT ID PK
        VARCHAR IDENTIFIANT UK "Unique"
        VARCHAR NOM "NOT NULL"
        VARCHAR PRENOM "NOT NULL"
        VARCHAR EMAIL UK "NOT NULL, Unique"
        VARCHAR TYPE_CLIENT "PARTICULIER | PROFESSIONNEL"
        VARCHAR SIRET "NULL pour particuliers, 14 chiffres"
        TIMESTAMP DATE_CREATION "DEFAULT CURRENT_TIMESTAMP"
    }

    COMPTES {
        BIGINT ID PK
        VARCHAR NUMERO_COMPTE UK "NOT NULL, Unique, Format FR76..."
        DECIMAL SOLDE "DEFAULT 0.0, CHECK >= 0"
        VARCHAR TYPE_COMPTE "SIMPLE | PARTAGE | PROFESSIONNEL"
        BIGINT CLIENT_ID FK "NULL pour comptes partagés"
        TIMESTAMP DATE_CREATION "DEFAULT CURRENT_TIMESTAMP"
    }

    COMPTES_PARTAGES {
        BIGINT COMPTE_ID FK
        BIGINT CLIENT_ID FK
        TIMESTAMP DATE_AJOUT "DEFAULT CURRENT_TIMESTAMP"
    }

    TRANSACTIONS {
        BIGINT ID PK
        DECIMAL MONTANT "NOT NULL, CHECK > 0"
        VARCHAR TYPE_OPERATION "RETRAIT | DEPOT | VIREMENT | VIREMENT_INTERNATIONAL"
        VARCHAR STATUT "EN_COURS | VALIDEE | REJETEE | ANNULEE"
        BIGINT COMPTE_SOURCE_ID FK "NOT NULL"
        BIGINT COMPTE_DESTINATION_ID FK "NULL pour RETRAIT/DEPOT"
        TIMESTAMP DATE_TRANSACTION "DEFAULT CURRENT_TIMESTAMP"
        VARCHAR DESCRIPTION "Optionnel"
    }

    BANQUIERS {
        BIGINT ID PK
        VARCHAR IDENTIFIANT UK "Unique"
        VARCHAR NOM "NOT NULL"
        VARCHAR PRENOM "NOT NULL"
        VARCHAR EMAIL UK "NOT NULL, Unique"
        VARCHAR MOT_DE_PASSE "NOT NULL, Hashé"
        VARCHAR MATRICULE UK "Unique, Format BNQ-XXXX"
        TIMESTAMP DATE_EMBAUCHE "DEFAULT CURRENT_TIMESTAMP"
    }

    %% Relations JPA
    CLIENTS ||--o{ COMPTES : "possede (1:N)"
    CLIENTS ||--o{ COMPTES_PARTAGES : "copropriete (M:N)"
    COMPTES ||--o{ COMPTES_PARTAGES : "partage (M:N)"
    COMPTES ||--o{ TRANSACTIONS : "source (1:N)"
    COMPTES ||--o{ TRANSACTIONS : "destination (1:N)"
    BANQUIERS ||--o{ CLIENTS : "gere (1:N)"
```

### Contraintes et indexes

**CLIENTS**
- `PK_CLIENTS` : PRIMARY KEY (ID)
- `UK_CLIENT_IDENTIFIANT` : UNIQUE (IDENTIFIANT)
- `UK_CLIENT_EMAIL` : UNIQUE (EMAIL)
- `CHK_CLIENT_TYPE` : TYPE_CLIENT IN ('PARTICULIER', 'PROFESSIONNEL')
- `CHK_SIRET_LENGTH` : SIRET NULL OR LENGTH(SIRET) = 14
- `IDX_CLIENT_EMAIL` : INDEX (EMAIL)

**COMPTES**
- `PK_COMPTES` : PRIMARY KEY (ID)
- `UK_NUMERO_COMPTE` : UNIQUE (NUMERO_COMPTE)
- `CHK_SOLDE_POSITIF` : SOLDE >= 0
- `CHK_TYPE_COMPTE` : TYPE_COMPTE IN ('SIMPLE', 'PARTAGE', 'PROFESSIONNEL')
- `FK_COMPTE_CLIENT` : FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS(ID)
- `IDX_COMPTE_CLIENT` : INDEX (CLIENT_ID)

**COMPTES_PARTAGES**
- `PK_COMPTES_PARTAGES` : PRIMARY KEY (COMPTE_ID, CLIENT_ID)
- `FK_PARTAGE_COMPTE` : FOREIGN KEY (COMPTE_ID) REFERENCES COMPTES(ID) ON DELETE CASCADE
- `FK_PARTAGE_CLIENT` : FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS(ID) ON DELETE CASCADE
- `CHK_MAX_PROPRIETAIRES` : COUNT(*) per COMPTE_ID <= 10

**TRANSACTIONS**
- `PK_TRANSACTIONS` : PRIMARY KEY (ID)
- `CHK_MONTANT_POSITIF` : MONTANT > 0
- `CHK_TYPE_OPERATION` : TYPE_OPERATION IN ('RETRAIT', 'DEPOT', 'VIREMENT', 'VIREMENT_INTERNATIONAL')
- `CHK_STATUT` : STATUT IN ('EN_COURS', 'VALIDEE', 'REJETEE', 'ANNULEE')
- `FK_TRANSACTION_SOURCE` : FOREIGN KEY (COMPTE_SOURCE_ID) REFERENCES COMPTES(ID)
- `FK_TRANSACTION_DEST` : FOREIGN KEY (COMPTE_DESTINATION_ID) REFERENCES COMPTES(ID)
- `IDX_TRANSACTION_DATE` : INDEX (DATE_TRANSACTION)
- `IDX_TRANSACTION_SOURCE` : INDEX (COMPTE_SOURCE_ID)

**BANQUIERS**
- `PK_BANQUIERS` : PRIMARY KEY (ID)
- `UK_BANQUIER_IDENTIFIANT` : UNIQUE (IDENTIFIANT)
- `UK_BANQUIER_EMAIL` : UNIQUE (EMAIL)
- `UK_BANQUIER_MATRICULE` : UNIQUE (MATRICULE)

---

## 10. DIAGRAMME DE DÉPLOIEMENT

### Description
Ce diagramme illustre l'infrastructure technique et le déploiement de l'application FSTBank sur WildFly avec la base H2.

```mermaid
graph TB
    subgraph "POSTE DÉVELOPPEUR"
        IDE[VS Code / IntelliJ IDEA<br/>IDE]
        JDK[JDK 17<br/>Java Development Kit]
        MVN[Maven 3.9+<br/>Build Tool]
        PROJECT[Projet FSTBank<br/>fstbank-ejb.jar]
        
        IDE --> JDK
        IDE --> MVN
        MVN --> PROJECT
    end

    subgraph "WILDFLY APPLICATION SERVER"
        direction TB
        
        subgraph "JBOSS Modules"
            JNDI[JNDI Context<br/>java:jboss/]
            NAMING[Naming Service]
        end
        
        subgraph "EJB CONTAINER"
            BEAN1["GestionClientBean<br/>@Stateless"]
            BEAN2["GestionCompteBean<br/>@Stateless"]
            BEAN3["GestionTransactionBean<br/>@Stateless"]
            BEAN4["GestionNotificationBean<br/>@Stateless"]
            POOL[Bean Instance Pool]
            
            POOL --> BEAN1
            POOL --> BEAN2
            POOL --> BEAN3
            POOL --> BEAN4
        end
        
        subgraph "JPA / HIBERNATE"
            PERSISTENCE[persistence.xml]
            EM[EntityManager Factory]
            HIBERNATE[Hibernate 6.x<br/>ORM Provider]
            
            PERSISTENCE --> EM
            EM --> HIBERNATE
        end
        
        JNDI --> BEAN1
        JNDI --> BEAN2
        JNDI --> BEAN3
        JNDI --> BEAN4
        
        BEAN1 --> EM
        BEAN2 --> EM
        BEAN3 --> EM
        BEAN4 --> EM
    end

    subgraph "H2 DATABASE"
        DB[(H2 Engine<br/>jdbc:h2:mem:fstbankdb)]
        SCHEMA[Schema: PUBLIC]
        TABLES["Tables:<br/>- CLIENTS<br/>- COMPTES<br/>- COMPTES_PARTAGES<br/>- TRANSACTIONS<br/>- BANQUIERS"]
        
        DB --> SCHEMA
        SCHEMA --> TABLES
    end

    subgraph "CLIENT APPLICATION"
        MAIN[FSTBankMain.java<br/>Test Client]
        JNDI_CLIENT[JNDI Lookup<br/>Initial Context]
        
        MAIN --> JNDI_CLIENT
    end

    %% Connexions de déploiement
    PROJECT -->|Deploy| JNDI
    JNDI_CLIENT -->|Remote Lookup| JNDI
    HIBERNATE -->|JDBC Connection| DB
    
    %% Annotations
    Note1[Port: 8080<br/>Management: 9990]
    Note2[Driver: org.h2.Driver<br/>Mode: In-Memory]
    Note3[Protocol: EJB/RMI<br/>Port: 8080]
    
    Note1 -.-> JNDI
    Note2 -.-> DB
    Note3 -.-> JNDI_CLIENT

    %% Styles
    style IDE fill:#9B59B6,stroke:#6C3483,color:#fff
    style PROJECT fill:#3498DB,stroke:#2471A3,color:#fff
    style BEAN1 fill:#27AE60,stroke:#1E8449,color:#fff
    style BEAN2 fill:#27AE60,stroke:#1E8449,color:#fff
    style BEAN3 fill:#27AE60,stroke:#1E8449,color:#fff
    style BEAN4 fill:#27AE60,stroke:#1E8449,color:#fff
    style EM fill:#F39C12,stroke:#D68910,color:#fff
    style DB fill:#E74C3C,stroke:#C0392B,color:#fff
    style MAIN fill:#9B59B6,stroke:#6C3483,color:#fff
```

### Configuration technique

**Environnement de développement**
- **IDE** : VS Code avec Extension Pack for Java / IntelliJ IDEA Ultimate
- **JDK** : OpenJDK 17 LTS
- **Build** : Maven 3.9+
- **Git** : Gestion de version

**WildFly Application Server (v27+)**
- **Ports** :
  - 8080 : HTTP (application)
  - 9990 : Management Console
  - 8009 : AJP Connector
- **Modules** :
  - EJB 3.2+ Container
  - JPA 2.2 / Hibernate 6.x
  - CDI 2.0
  - Bean Validation 2.0
- **Configuration** : standalone.xml
- **Déploiement** : Hot deploy via /deployments ou Management CLI

**H2 Database**
- **Mode** : In-Memory (jdbc:h2:mem:fstbankdb)
- **Alternative** : File-based (jdbc:h2:file:./data/fstbank)
- **Console** : http://localhost:8082 (H2 Console activable)
- **Persistance** : Optionnelle via fichier .mv.db

**Persistence (persistence.xml)**
```xml
<persistence-unit name="fstbank-pu">
    <jta-data-source>java:jboss/datasources/FSTBankDS</jta-data-source>
    <properties>
        <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
    </properties>
</persistence-unit>
```

---

## 📚 RÉSUMÉ DES DESIGN PATTERNS

### 1. Factory Pattern
**Objectif** : Centraliser la création d'objets complexes avec validation métier
- `ClientFactory` : Valide SIRET, email, génère identifiants
- `CompteFactory` : Génère numéros de compte, valide propriétaires

### 2. Strategy Pattern
**Objectif** : Encapsuler différents algorithmes d'opérations bancaires
- `StrategieRetrait` : Validation solde + débit
- `StrategieDepot` : Limite 50k€ + crédit
- `StrategieVirement` : Transaction atomique source→destination

### 3. Observer Pattern
**Objectif** : Notifier les clients en temps réel des opérations sur leurs comptes
- `ObservableCompte` : Interface pour les sujets observés
- `ObservateurCompte` : Interface pour les observateurs
- `NotificationClient` : Implémentation concrète avec historique

---

## 🎨 LÉGENDE GÉNÉRALE

### Couleurs
- 🔵 **Bleu (#4A90E2)** : Classes entités (@Entity)
- 🟢 **Vert (#27AE60)** : Session Beans (@Stateless)
- 🟠 **Orange (#F39C12)** : Design Patterns (Factory, Strategy)
- 🔴 **Rouge (#E74C3C)** : Interfaces
- 🟣 **Violet (#9B59B6)** : Clients / Présentation

### Symboles UML
- **→** : Association (relation simple)
- **◆→** : Composition (contient, cycle de vie lié)
- **◇→** : Agrégation (contient, cycle de vie indépendant)
- **--|>** : Héritage (extends)
- **..|>** : Implémentation (implements)
- **- -** : Dépendance (uses)

### Annotations JPA
- `@Entity` : Classe persistante
- `@OneToMany` : Relation 1 à plusieurs
- `@ManyToMany` : Relation plusieurs à plusieurs
- `@ManyToOne` : Relation plusieurs à 1
- `@JoinTable` : Table de jointure

---

## 📋 CHECKLIST DE VALIDATION

### ✅ Architecture
- [x] Architecture 3-tiers clairement séparée
- [x] Session Beans @Stateless avec @Remote interfaces
- [x] Entités JPA avec relations correctes
- [x] Base de données H2 configurée

### ✅ Design Patterns
- [x] Factory Pattern : ClientFactory + CompteFactory
- [x] Strategy Pattern : GestionnaireTransaction + 3 stratégies
- [x] Observer Pattern : Compte observable + NotificationClient

### ✅ Fonctionnalités métier
- [x] Gestion clients (particuliers + professionnels)
- [x] Gestion comptes (simples + partagés + professionnels)
- [x] Gestion transactions (retrait + dépôt + virement)
- [x] Système de notifications en temps réel

### ✅ Qualité du code
- [x] Validation métier (SIRET, email, solde)
- [x] Gestion des erreurs (exceptions métier)
- [x] Transactions JPA (@Transactional)
- [x] Tests d'intégration (FSTBankMain)

---

## 📖 DOCUMENTATION TECHNIQUE

### Technologies utilisées
- **Java** : JDK 17 LTS
- **Jakarta EE** : EJB 3.2, JPA 2.2
- **Serveur** : WildFly 27+
- **ORM** : Hibernate 6.x
- **Base de données** : H2 Database (in-memory)
- **Build** : Maven 3.9+

### Structure du projet
```
fstbank/
├── src/main/java/
│   ├── entities/
│   │   ├── Client.java (abstract)
│   │   ├── ClientParticulier.java
│   │   ├── ClientProfessionnel.java
│   │   ├── Compte.java (abstract)
│   │   ├── CompteParticulierSimple.java
│   │   ├── CompteParticulierPartage.java
│   │   ├── CompteProfessionnel.java
│   │   ├── Transaction.java
│   │   └── Banquier.java
│   ├── session/
│   │   ├── GestionClientBean.java
│   │   ├── GestionCompteBean.java
│   │   ├── GestionTransactionBean.java
│   │   └── GestionNotificationBean.java
│   ├── remote/
│   │   ├── GestionClientRemote.java
│   │   ├── GestionCompteRemote.java
│   │   ├── GestionTransactionRemote.java
│   │   └── GestionNotificationRemote.java
│   ├── factories/
│   │   ├── ClientFactory.java
│   │   └── CompteFactory.java
│   ├── strategies/
│   │   ├── StrategieOperation.java
│   │   ├── StrategieRetrait.java
│   │   ├── StrategieDepot.java
│   │   ├── StrategieVirement.java
│   │   └── GestionnaireTransaction.java
│   └── observers/
│       ├── ObservableCompte.java
│       ├── ObservateurCompte.java
│       └── NotificationClient.java
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml
├── src/test/java/
│   └── FSTBankMain.java
└── pom.xml
```

---

## 🚀 COMMANDES DE DÉPLOIEMENT

### Build du projet
```bash
mvn clean package
```

### Déploiement sur WildFly
```bash
# Démarrer WildFly
cd $WILDFLY_HOME/bin
./standalone.sh

# Déployer l'application
cp target/fstbank-ejb.jar $WILDFLY_HOME/standalone/deployments/
```

### Accès à l'application
```bash
# Console Management
http://localhost:9990

# H2 Console (si activée)
http://localhost:8082
```

### Exécution du client de test
```bash
mvn exec:java -Dexec.mainClass="test.FSTBankMain"
```

---

## 📞 SUPPORT

Pour toute question sur l'architecture ou l'implémentation :
- Consulter la documentation Jakarta EE : https://jakarta.ee/
- Documentation WildFly : https://docs.wildfly.org/
- Documentation Hibernate : https://hibernate.org/orm/documentation/

---

**Document généré le 27 janvier 2026**  
**Version** : 1.0  
**Projet** : FSTBank - Application de gestion bancaire  
**Architecture** : EJB3 3-Tiers avec Design Patterns
