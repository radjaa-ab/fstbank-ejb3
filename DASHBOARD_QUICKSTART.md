# 🏦 FSTBANK - Dashboard Deployment Guide

Your EJB3 banking application is now ready to deploy with a beautiful web dashboard!

## Quick Start (5 minutes)

### Step 1: Download WildFly 20
```bash
# Linux/Mac
wget https://github.com/wildfly/wildfly/releases/download/20.0.0.Final/wildfly-20.0.0.Final.tar.gz
tar -xzf wildfly-20.0.0.Final.tar.gz

# Windows - Download from:
# https://github.com/wildfly/wildfly/releases/download/20.0.0.Final/wildfly-20.0.0.Final.zip
# Extract to your home directory
```

### Step 2: Create WildFly Admin User
```bash
cd wildfly-20.0.0.Final/bin

# Linux/Mac
./add-user.sh

# Windows
add-user.bat

# When prompted:
# Type: a (Application User)
# Realm: ManagementRealm
# Username: admin
# Password: admin123
```

### Step 3: Start WildFly Server
```bash
# Linux/Mac
cd wildfly-20.0.0.Final/bin
./standalone.sh

# Windows
cd wildfly-20.0.0.Final\bin
standalone.bat
```

Wait for message: `"WildFly Full 20.0.0.Final (WildFly Core 12.0.0.Final) started"`

### Step 4: Deploy FSTBANK

#### On Windows:
```bash
cd c:\Users\DELL\Desktop\adla project
deploy.bat
```

#### On Linux/Mac:
```bash
cd ~/adla\ project
chmod +x deploy.sh
./deploy.sh
```

### Step 5: Open Dashboard
```
http://localhost:8080/fstbank
```

## Dashboard Features

### 📊 Statistics Cards
- **Total Clients**: 3 clients (particulier & professionnel)
- **Total Accounts**: 4 accounts (simple, shared, professional)
- **Total Transactions**: 4 transactions (deposits, withdrawals, transfers)
- **Total Balance**: System-wide balance visualization

### 👥 Clients Section
- View all registered clients
- Display client type (Particulier/Professionnel)
- Contact information (email)

### 💳 Accounts Section
- Real-time account balances
- Account type indicators
- Owner information
- Color-coded account types

### 💰 Transactions Section
- Transaction history
- Transaction types (Depot, Retrait, Virement)
- Transaction status (Validée, Rejetée)
- Amount and date information

## API Endpoints

REST API available at `http://localhost:8080/fstbank/api/dashboard`

### Endpoints:
```
GET /api/dashboard/status      - Application status
GET /api/dashboard/stats       - Statistics
GET /api/dashboard/clients     - All clients
GET /api/dashboard/accounts    - All accounts
GET /api/dashboard/transactions - All transactions
```

## Architecture

```
┌─────────────────────────────────────┐
│        Browser (Dashboard)          │
│   (HTML5 + CSS3 + JavaScript)       │
└────────────┬────────────────────────┘
             │ REST API
             ↓
┌─────────────────────────────────────┐
│         WildFly Server              │
│  ┌─────────────────────────────┐    │
│  │  REST Application Layer     │    │
│  │  DashboardResource.java     │    │
│  └──────────┬──────────────────┘    │
│             │                        │
│  ┌──────────↓──────────────────┐    │
│  │   Session Beans (Stateful)  │    │
│  │  - GestionClientBean        │    │
│  │  - GestionCompteBean        │    │
│  │  - GestionTransactionBean   │    │
│  │  - GestionNotificationBean  │    │
│  └──────────┬──────────────────┘    │
│             │                        │
│  ┌──────────↓──────────────────┐    │
│  │     JPA/Hibernate           │    │
│  │  (Entity Management)        │    │
│  └──────────┬──────────────────┘    │
│             │                        │
│  ┌──────────↓──────────────────┐    │
│  │    H2 Embedded Database    │    │
│  │    (In-Memory JDBC)        │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Directory Structure

```
fstbank/
├── src/main/
│   ├── java/
│   │   └── dz/fst/bank/
│   │       ├── entities/          ← JPA Entities
│   │       ├── session/           ← EJB Session Beans (Stateful)
│   │       ├── factories/         ← Factory Pattern
│   │       ├── strategies/        ← Strategy Pattern
│   │       ├── observers/         ← Observer Pattern
│   │       ├── rest/              ← REST API
│   │       └── test/              ← Test Classes
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml    ← JPA Configuration
│   └── webapp/
│       ├── index.html             ← Dashboard
│       └── WEB-INF/
│           └── web.xml            ← Web Configuration
├── pom.xml                        ← Maven Build
├── deploy.sh                      ← Linux/Mac Deploy
└── deploy.bat                     ← Windows Deploy
```

## Configuration Files

### web.xml
- JAX-RS REST servlet configuration
- Welcome page setup
- URL patterns

### persistence.xml
- JPA provider: Hibernate
- Database: H2 (in-memory)
- DDL: create-drop (development)

### pom.xml
- Maven dependencies
- EAR packaging configuration
- WildFly plugin for deployment

## Features Implemented

### ✅ Design Patterns
- **Factory Pattern**: ClientFactory, CompteFactory
- **Strategy Pattern**: Retrait, Depot, Virement strategies
- **Observer Pattern**: Notification system

### ✅ EJB3
- **Stateful Session Beans**: Maintain client session state
- **Dependency Injection**: @EJB, @PersistenceContext
- **Lifecycle Callbacks**: @PostConstruct, @PreDestroy

### ✅ JPA/Hibernate
- **Entity Mapping**: All entities properly mapped
- **Inheritance**: JOINED strategy for Utilisateur
- **Relationships**: Many-to-Many, One-to-Many associations

### ✅ Web Layer
- **REST API**: JAX-RS endpoints
- **Dashboard**: HTML5 responsive UI
- **Real-time Updates**: JavaScript auto-refresh

## Troubleshooting

### Dashboard not loading?
1. Check WildFly is running: `http://localhost:8080`
2. Check deployment: `http://localhost:9990` (Admin console)
3. Check logs: `wildfly-20.0.0.Final/standalone/log/server.log`

### REST API not working?
1. Try: `http://localhost:8080/fstbank/api/dashboard/status`
2. Check REST endpoints are registered
3. Verify web.xml configuration

### Database issues?
1. Check persistence.xml H2 configuration
2. Verify JDBC datasource in WildFly

## Performance Notes

- **Stateful Beans**: Default timeout is 30 minutes
- **H2 Database**: In-memory, fast but no persistence
- **Dashboard Refresh**: Auto-refresh every 30 seconds

## Production Deployment

For production:
1. Use external database (MySQL, PostgreSQL)
2. Update persistence.xml datasource
3. Enable HTTPS
4. Configure authentication/authorization
5. Optimize session timeout
6. Monitor WildFly logs
7. Set up load balancing if needed

## Next Steps

1. ✅ Deploy to WildFly
2. ✅ Access dashboard at `http://localhost:8080/fstbank`
3. ✅ Test REST API endpoints
4. Add more features (user authentication, database persistence, etc.)
5. Deploy to production with external database

## Support

- **Documentation**: See WILDFLY_DEPLOYMENT.md
- **GitHub**: https://github.com/radjaa-ab/fstbank-ejb3
- **WildFly Docs**: https://docs.wildfly.org/

---

**Enjoy your FSTBANK dashboard! 🎉**
