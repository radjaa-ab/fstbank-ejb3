# FSTBANK - Direct Deployment Guide (Step by Step)

## Manual Deployment Steps

### Step 1: Install Maven (if not already installed)

**Windows:**
1. Download from: https://maven.apache.org/download.cgi
2. Download: `apache-maven-3.8.6-bin.zip`
3. Extract to: `C:\Users\DELL\maven`
4. Add to PATH:
   - Open Environment Variables
   - Add: `C:\Users\DELL\maven\bin`

**Verify:**
```bash
mvn -version
```

---

### Step 2: Download WildFly 20

**Windows:**
1. Download: https://github.com/wildfly/wildfly/releases/download/20.0.0.Final/wildfly-20.0.0.Final.zip
2. Extract to: `C:\Users\DELL\wildfly-20.0.0.Final`

**Verify directory structure:**
```
C:\Users\DELL\wildfly-20.0.0.Final\
├── bin\
├── modules\
├── standalone\
├── README.txt
└── LICENSE.txt
```

---

### Step 3: Create WildFly Admin User

**Windows:**
1. Open PowerShell or CMD
2. Navigate to WildFly:
   ```bash
   cd C:\Users\DELL\wildfly-20.0.0.Final\bin
   ```
3. Run:
   ```bash
   .\add-user.bat
   ```
4. When prompted:
   - Choose: `a` (Application User)
   - Realm: `ManagementRealm`
   - Username: `admin`
   - Password: `admin123`
   - Confirm password

---

### Step 4: Build FSTBANK Application

**Windows:**
1. Open PowerShell or CMD
2. Navigate to project:
   ```bash
   cd C:\Users\DELL\Desktop\adla project
   ```
3. Build:
   ```bash
   mvn clean package -DskipTests
   ```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXs
```

---

### Step 5: Deploy to WildFly

**Option A: Automated Copy**

1. Find the EAR file:
   ```bash
   C:\Users\DELL\Desktop\adla project\target\fstbank-ear-1.0.0.ear
   ```

2. Copy to WildFly:
   ```bash
   copy "C:\Users\DELL\Desktop\adla project\target\fstbank-ear-1.0.0.ear" "C:\Users\DELL\wildfly-20.0.0.Final\standalone\deployments\"
   ```

**Option B: Manual Copy**
- Open File Explorer
- Copy: `C:\Users\DELL\Desktop\adla project\target\fstbank-ear-1.0.0.ear`
- Paste to: `C:\Users\DELL\wildfly-20.0.0.Final\standalone\deployments\`

---

### Step 6: Start WildFly Server

**Windows:**
1. Open PowerShell or CMD
2. Navigate to WildFly:
   ```bash
   cd C:\Users\DELL\wildfly-20.0.0.Final\bin
   ```
3. Start server:
   ```bash
   .\standalone.bat
   ```

**You should see:**
```
WildFly Full 20.0.0.Final (WildFly Core 12.0.0.Final) started
```

---

### Step 7: Access Your Dashboard

Open your browser and go to:

**Dashboard:** http://localhost:8080/fstbank

**Admin Console:** http://localhost:9990

**REST API:** http://localhost:8080/fstbank/api/dashboard/status

---

## Troubleshooting

### "mvn is not recognized"
- Maven is not installed or not in PATH
- Install Maven and restart terminal

### "WildFly directory not found"
- Download WildFly to correct location
- Extract properly with all subdirectories

### "Application won't deploy"
1. Check WildFly logs:
   ```
   C:\Users\DELL\wildfly-20.0.0.Final\standalone\log\server.log
   ```
2. Check if EAR file exists in deployments folder
3. Restart WildFly server

### "Dashboard shows error"
1. Ensure WildFly is fully started (look for "started" message)
2. Wait 5-10 seconds and refresh browser
3. Check browser console for errors (F12)

### "REST API not working"
- Try: http://localhost:8080/fstbank/api/dashboard/status
- If error, check WildFly logs

---

## What Happens During Deployment

1. **Maven Build** - Creates EAR file with:
   - EJB module (Session Beans, Entities)
   - Web module (Dashboard HTML, REST API)
   - Configuration files

2. **Copy to WildFly** - Places EAR in deployment directory

3. **WildFly Detects** - Automatically deploys when started

4. **Application Starts** - EJBs initialize, database created, REST API enabled

5. **Dashboard Available** - Access via browser

---

## Architecture

```
Browser (Port 8080)
       ↓
Dashboard (HTML/CSS/JavaScript)
       ↓
REST API (JAX-RS)
       ↓
Session Beans (Stateful - Stateful)
       ↓
JPA/Hibernate
       ↓
H2 Database (In-Memory)
```

---

## API Endpoints Available

```
GET /api/dashboard/status      - Application status
GET /api/dashboard/stats       - Statistics (clients, accounts, transactions)
GET /api/dashboard/clients     - List of all clients
GET /api/dashboard/accounts    - List of all accounts
GET /api/dashboard/transactions - List of all transactions
```

---

## Dashboard Features

✅ **Real-time Statistics**
- Total Clients: 3
- Total Accounts: 4
- Total Transactions: 4
- Total Balance: €129,000

✅ **Client Management**
- View all clients
- Filter by type (Particulier/Professionnel)
- Contact information

✅ **Account Monitoring**
- View all accounts
- Real-time balances
- Account type indicators

✅ **Transaction History**
- All transactions
- Status (Validated/Rejected)
- Transaction type (Deposit/Withdrawal/Transfer)

---

## Default Test Data

**Clients:**
- Jean Dupont (Particulier)
- Marie Martin (Particulier)
- TechCorp SA (Professionnel)

**Accounts:**
- ACC001 - Simple Account (€7,000)
- ACC002 - Shared Account (€15,000)
- ACC003 - Simple Account (€7,000)
- ACC004 - Professional Account (€105,000)

**Transactions:**
- Deposit: €2,000 ✓
- Withdrawal: €1,000 ✓
- Transfer: €5,000 ✓
- Withdrawal Rejected: €10,000 (insufficient balance) ✗

---

## Next Steps After Deployment

1. ✅ Open dashboard: http://localhost:8080/fstbank
2. ✅ Test REST API endpoints
3. ✅ Explore Admin Console: http://localhost:9990
4. Add database persistence (optional)
5. Add user authentication (optional)
6. Deploy to production (optional)

---

## Performance Tips

- Dashboard auto-refreshes every 30 seconds
- Stateful beans maintain state for 30 minutes
- H2 database is in-memory (fast but data lost on restart)
- For production, use external database

---

## Support

- **Documentation**: See WILDFLY_DEPLOYMENT.md
- **GitHub**: https://github.com/radjaa-ab/fstbank-ejb3
- **WildFly**: https://docs.wildfly.org/

---

**Ready to deploy! Follow the steps above and enjoy your FSTBANK dashboard! 🎉**
