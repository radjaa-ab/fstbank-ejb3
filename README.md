# FSTBANK - Implémentation EJB3 Complète

## 📋 Vue d'ensemble

FSTBANK est une application bancaire complète développée avec les technologies **EJB3**, **JPA/Hibernate** et **Design Patterns** avancés.

### Caractéristiques principales

✅ **Factory Pattern** - Création sécurisée des clients et comptes
✅ **Strategy Pattern** - Opérations bancaires flexibles (dépôt, retrait, virement)
✅ **Session Beans** - Gestion métier (clients, comptes, transactions)
✅ **Entity Beans** - Modèle de données persistant
✅ **Transactions JTA** - Gestion transactionnelle distribuée
✅ **Validation métier** - Règles de gestion strictes

---

## 🏗️ Architecture du Projet

```
src/main/java/dz/fst/bank/
├── entities/
│   ├── Client.java (classe abstraite)
│   ├── ClientParticulier.java
│   ├── ClientProfessionnel.java
│   ├── Compte.java (classe abstraite)
│   ├── CompteParticulierSimple.java
│   ├── CompteParticulierPartage.java
│   ├── CompteProfessionnel.java
│   ├── Transaction.java
│   ├── Banquier.java
│   ├── TypeOperation.java (enum)
│   └── StatutTransaction.java (enum)
│
├── session/
│   ├── GestionClientBean.java (@Stateless)
│   ├── GestionClientBeanRemote.java (interface)
│   ├── GestionCompteBean.java (@Stateless)
│   ├── GestionCompteBeanRemote.java (interface)
│   ├── GestionTransactionBean.java (@Stateless)
│   └── GestionTransactionBeanRemote.java (interface)
│
├── factories/
│   ├── ClientFactory.java
│   └── CompteFactory.java
│
├── strategies/
│   ├── StrategyOperation.java (interface)
│   ├── StrategieRetrait.java
│   ├── StrategieVirement.java
│   ├── StrategieDepot.java
│   └── GestionnaireTransaction.java
│
└── test/
    └── FSTBankMain.java
```

---

## 🎯 Design Patterns Utilisés

### 1️⃣ Factory Method Pattern

**Classe : `ClientFactory`**
```java
// Création simplifiée avec validation
ClientParticulier client = ClientFactory.creerClientParticulier(
    "CLI001", "DUPONT", "Jean", "jean@email.com", "password"
);
```

**Classe : `CompteFactory`**
```java
// Création polymorphe de comptes
Compte compte = CompteFactory.creerCompte("PARTICULIER_SIMPLE", "CPT001", client);
```

### 2️⃣ Strategy Pattern

**Interface : `StrategyOperation`**
```java
public interface StrategyOperation {
    boolean valider(Compte compte, double montant);
    boolean executer(Compte compte, double montant);
    TypeOperation getTypeOperation();
}
```

**Implémentations :**
- `StrategieDepot` - Validations et limite de 50 000 DA
- `StrategieRetrait` - Contrôle de solde
- `StrategieVirement` - Validation source + destination

**Gestionnaire : `GestionnaireTransaction`**
```java
GestionnaireTransaction manager = new GestionnaireTransaction(em);
manager.setStrategie(new StrategieDepot(em));
boolean success = manager.effectuerOperation(compte, 5000.0);
```

### 3️⃣ Composite Pattern

**Comptes partagés jusqu'à 10 propriétaires :**
```java
CompteParticulierPartage compte = new CompteParticulierPartage("PARTAGE01");
compte.ajouterProprietaire(client1);
compte.ajouterProprietaire(client2);
compte.ajouterProprietaire(client3);
// Tous les propriétaires accèdent au même solde
```

---

## 🔑 Points Clés de l'Implémentation

### Catégories de Clients
| Type | Classe | Attributs Spécifiques |
|------|--------|----------------------|
| **Particulier** | `ClientParticulier` | Nom, Prénom, Email |
| **Professionnel** | `ClientProfessionnel` | Raison Sociale, SIRET (14 chiffres) |

### Types de Comptes
| Type | Classe | Caractéristiques |
|------|--------|-----------------|
| **Particulier Simple** | `CompteParticulierSimple` | 1 propriétaire uniquement |
| **Particulier Partagé** | `CompteParticulierPartage` | 2-10 propriétaires |
| **Professionnel** | `CompteProfessionnel` | Client professionnel requis |

### Opérations Bancaires
| Opération | Validation | Limite |
|-----------|-----------|--------|
| **Dépôt** | Montant > 0, Compte actif | Max: 50 000 DA |
| **Retrait** | Solde suffisant, Compte actif | - |
| **Virement** | Comptes actifs différents, Solde src | - |

---

## 🔌 Configuration Persistence.xml

```xml
<persistence-unit name="FSTBankPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:/FSTBankDS</jta-data-source>
    
    <!-- Toutes les entités sont déclarées -->
    <class>dz.fst.bank.entities.Client</class>
    ...
    
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        <property name="hibernate.show_sql" value="true"/>
    </properties>
</persistence-unit>
```

---

## 📦 Fichiers de Configuration

### DataSource JBoss (`fstbank-ds.xml`)
Placer dans : `JBOSS_HOME/standalone/deployments/`

```xml
<datasource jndi-name="java:/FSTBankDS" pool-name="FSTBankPool">
    <connection-url>jdbc:h2:mem:fstbankdb;DB_CLOSE_DELAY=-1</connection-url>
    <driver>h2</driver>
    <pool>
        <min-pool-size>5</min-pool-size>
        <max-pool-size>20</max-pool-size>
    </pool>
</datasource>
```

---

## 🚀 Instructions de Déploiement

### Étape 1 : Prérequis
```bash
# Installer JBoss/WildFly 20+
export JBOSS_HOME=/path/to/wildfly
export PATH=$PATH:$JBOSS_HOME/bin

# Créer utilisateur admin
cd $JBOSS_HOME/bin
./add-user.sh
# Username: admin / Password: admin
```

### Étape 2 : Compilation
```bash
# Compiler le projet
javac -d target/classes -cp "lib/*" src/main/java/dz/fst/bank/**/*.java

# Créer l'archive EJB
jar cvf FSTBankEJB.jar -C target/classes dz/fst/bank/entities \
    -C target/classes dz/fst/bank/session \
    -C target/classes dz/fst/bank/factories \
    -C target/classes dz/fst/bank/strategies \
    -C src/main/resources META-INF
```

### Étape 3 : Déploiement
```bash
# Démarrer JBoss
$JBOSS_HOME/bin/standalone.sh &

# Déployer l'application
cp FSTBankEJB.jar $JBOSS_HOME/standalone/deployments/

# Vérifier
# http://localhost:9990
# Se connecter avec admin/admin
```

### Étape 4 : Exécuter les tests
```bash
# Compiler le client
javac -cp "lib/*:FSTBankEJB.jar" src/main/java/dz/fst/bank/test/FSTBankMain.java

# Exécuter
java -cp "lib/*:FSTBankEJB.jar:target/classes" dz.fst.bank.test.FSTBankMain
```

---

## 🧪 Cas de Test Inclus

Le `FSTBankMain` teste automatiquement :

1. ✅ **Création de clients** (particuliers et professionnels)
2. ✅ **Création de comptes** (tous types)
3. ✅ **Dépôts** avec limite de 50 000 DA
4. ✅ **Retraits** avec vérification de solde
5. ✅ **Virements** entre comptes
6. ✅ **Comptes partagés** avec limite de 10 propriétaires
7. ✅ **Historique des transactions**
8. ✅ **Validation et rejet** des opérations invalides

### Exemple de sortie :
```
╔════════════════════════════════════════════════════════╗
║       FSTBANK - TEST APPLICATION BANCAIRE EJB3        ║
╚════════════════════════════════════════════════════════╝

━━━━ 2. TEST CRÉATION DE CLIENTS ━━━━
→ Création de clients particuliers...
✓ Client créé: BELHADJ Ahmed (CLIENT001)

→ Dépôt de 10000 DA sur compte CPT-20260127-001
  Solde avant: 0.00 DA
  ✓ Résultat: SUCCÈS
  Solde après: 10000.00 DA
```

---

## 📚 Dépendances Maven

```xml
<!-- EJB et JPA -->
<dependency>
    <groupId>javax.ejb</groupId>
    <artifactId>javax.ejb-api</artifactId>
    <version>3.2</version>
</dependency>

<!-- JPA/Hibernate -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.4.0.Final</version>
</dependency>

<!-- Base de données (développement) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.200</version>
</dependency>
```

---

## 🔐 Sécurité

- ✅ Validation stricte de tous les montants
- ✅ Vérification des soldes avant opération
- ✅ Contrôle d'état des comptes (actif/inactif)
- ✅ Transactions ACID avec JTA
- ✅ Authentification EJB

---

## 📝 Notes Importantes

1. **SIRET** : Doit contenir exactement 14 caractères
2. **Limite de propriétaires** : Max 10 pour comptes partagés
3. **Montant max dépôt** : 50 000 DA
4. **Solde initial** : 0.00 DA pour tous les nouveaux comptes
5. **Transactions** : Automatiquement persistées en base

---

## 🛠️ Dépannage

| Problème | Solution |
|----------|----------|
| `java.lang.ClassNotFoundException` | Ajouter les JARs dans le classpath |
| `JNDI lookup échoue` | Vérifier que l'EJB est déployé (console JBoss) |
| `DataSource not found` | Placer le `fstbank-ds.xml` dans `deployments/` |
| `Erreur de persistence` | Vérifier que Hibernate peut créer les tables |

---

## ✨ Fonctionnalités Futures

- [ ] Web Service SOAP pour opérations bancaires
- [ ] Interface Swing pour client bancaire
- [ ] Rapports PDF des transactions
- [ ] Notification par email
- [ ] Authentification LDAP
- [ ] Cache distribué (Infinispan)
- [ ] Microservices (Spring Cloud)

---

**Version :** 1.0.0  
**Date :** 27 Janvier 2026  
**Auteur :** FSTBANK Development Team  
**License :** MIT
