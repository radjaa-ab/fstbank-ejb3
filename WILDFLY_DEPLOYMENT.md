# FSTBANK - WildFly Deployment Guide

## Prerequisites
- WildFly 20.0.0 or higher (supports EJB 3.1)
- Java 11 or higher
- Maven 3.6+

## Installation Steps

### 1. Download & Setup WildFly

```bash
# Download WildFly 20
wget https://github.com/wildfly/wildfly/releases/download/20.0.0.Final/wildfly-20.0.0.Final.tar.gz

# Extract
tar -xzf wildfly-20.0.0.Final.tar.gz
cd wildfly-20.0.0.Final
```

### 2. Create a User for WildFly Admin

```bash
cd bin
./add-user.sh

# When prompted:
# - Type: a (Application User)
# - Realm: ManagementRealm
# - Username: admin
# - Password: admin123
```

### 3. Start WildFly Server

```bash
cd ~/wildfly-20.0.0.Final/bin
./standalone.sh
```

Server will be available at: `http://localhost:8080`
Admin Console: `http://localhost:9990`

### 4. Build and Package the Application

```bash
cd ~/adla\ project
mvn clean package -DskipTests
```

### 5. Deploy to WildFly

#### Option A: Using WildFly CLI

```bash
# Start CLI
cd ~/wildfly-20.0.0.Final/bin
./jboss-cli.sh --connect

# Deploy
deploy ~/adla\ project/target/fstbank.ear

# Undeploy (if needed)
# undeploy fstbank.ear
```

#### Option B: Copy to deployments directory

```bash
cp ~/adla\ project/target/fstbank.ear ~/wildfly-20.0.0.Final/standalone/deployments/
```

### 6. Access the Application

Dashboard: `http://localhost:8080/fstbank`

## Application Structure

```
fstbank.ear
├── fstbank-ejb.jar (EJB Module)
│   ├── Session Beans (Stateful)
│   │   ├── GestionClientBean
│   │   ├── GestionCompteBean
│   │   ├── GestionTransactionBean
│   │   └── GestionNotificationBean
│   ├── Entities
│   │   ├── Utilisateur
│   │   ├── Client (extends Utilisateur)
│   │   ├── Banquier (extends Utilisateur)
│   │   └── ...
│   ├── Design Patterns
│   │   ├── Factory Pattern
│   │   ├── Strategy Pattern
│   │   └── Observer Pattern
│   └── META-INF/
│       └── persistence.xml
└── fstbank-web.war (Web Module)
    ├── index.html (Dashboard)
    ├── api/dashboard/* (REST endpoints)
    └── WEB-INF/
        └── web.xml
```

## Features

### Dashboard
- **Real-time Statistics**: Total clients, accounts, transactions, balance
- **Client Management**: View all clients (Particulier & Professionnel)
- **Account Monitoring**: View all account types with current balances
- **Transaction History**: View all transactions with status

### Backend (EJB3)
- **Stateful Session Beans**: Maintain state across method calls
- **JPA/Hibernate**: Database persistence
- **Design Patterns**:
  - Factory: Client and Account creation
  - Strategy: Transaction operations
  - Observer: Notification system
- **H2 Database**: Embedded database

## Configuration

### persistence.xml
Located in `src/main/resources/META-INF/persistence.xml`
- Configured for H2 embedded database
- JPA provider: Hibernate
- Auto DDL: create-drop (development)

### web.xml
Located in `src/main/webapp/WEB-INF/web.xml`
- JAX-RS REST endpoints
- Welcome page: index.html

## REST API Endpoints

### Base URL
`http://localhost:8080/fstbank/api/dashboard`

### Endpoints

#### Get Application Status
```
GET /status
Response: { application, version, status, timestamp }
```

#### Get Statistics
```
GET /stats
Response: { totalClients, totalAccounts, totalTransactions, totalBalance, ... }
```

#### Get All Clients
```
GET /clients
Response: [{ id, identifiant, nom, email, type }, ...]
```

#### Get All Accounts
```
GET /accounts
Response: [{ id, numero, solde, type, owner }, ...]
```

#### Get All Transactions
```
GET /transactions
Response: [{ id, montant, type, statut, compte/from/to, date }, ...]
```

## Database Access

H2 Database Console (in-memory):
- Can be accessed via WildFly datasource at: `java:/FSTBankDS`
- Database: `jdbc:h2:mem:fstbankdb`
- Driver: H2 JDBC Driver (included in WildFly)

## Troubleshooting

### Application Not Deploying
1. Check WildFly logs: `tail -f ~/wildfly-20.0.0.Final/standalone/log/server.log`
2. Ensure EJB module has proper manifest
3. Verify web.xml is correctly configured

### REST Endpoints Not Working
1. Check if REST API is enabled in web.xml
2. Verify FSTBankApplication class is in classpath
3. Ensure JAX-RS is available in WildFly

### Database Connection Issues
1. Check H2 datasource configuration
2. Verify persistence.xml settings
3. Check WildFly datasource logs

## Development vs Production

### Development
- H2 in-memory database (lose data on restart)
- Auto DDL enabled (create-drop)
- Hot deployment enabled

### Production
- Use external database (MySQL, PostgreSQL)
- Update persistence.xml with external datasource
- Change DDL strategy to validate
- Disable hot deployment

## Performance Tuning

### Stateful Session Bean Timeout
Edit `jboss-ejb3.xml` to configure timeout:
```xml
<stateful>
    <timeout>30</timeout>  <!-- 30 minutes -->
</stateful>
```

### Connection Pool Settings
Modify datasource in `standalone.xml`:
```xml
<min-pool-size>5</min-pool-size>
<max-pool-size>20</max-pool-size>
```

## Security Considerations

1. Enable HTTPS in production
2. Configure authentication/authorization
3. Use JPA security for data access
4. Implement input validation
5. Configure firewall rules

## Logging

Enable debug logging for development:
```bash
/subsystem=logging/logger=dz.fst.bank:add(level=DEBUG)
```

## Support & Documentation

- WildFly Docs: https://docs.wildfly.org/
- EJB 3.1 Spec: https://download.oracle.com/otn-pub/jcp/ejb-3.1-fr-eval-spec/ejb-3_1-fr-eval-spec.pdf
- JPA Docs: https://jakarta.ee/specifications/persistence/
- H2 Database: https://www.h2database.com/

## License

FSTBANK © 2026
