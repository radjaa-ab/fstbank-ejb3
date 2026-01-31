# 🚀 FSTBANK - ONE-COMMAND DEPLOYMENT

## Quick Deploy (Choose ONE Option)

### Option 1: PowerShell (Recommended - Full Automation)
```powershell
cd C:\Users\DELL\Desktop\adla project
powershell -ExecutionPolicy Bypass -File deploy.ps1
```

### Option 2: Batch Script (Windows Command Prompt)
```bash
cd C:\Users\DELL\Desktop\adla project
setup-and-deploy.bat
```

### Option 3: Manual Step-by-Step
Follow: `DEPLOYMENT_STEPS.md`

---

## What These Scripts Do

### PowerShell Script (`deploy.ps1`)
1. ✅ Checks/downloads Maven (if needed)
2. ✅ Checks/downloads WildFly 20 (if needed)
3. ✅ Creates WildFly admin user
4. ✅ Builds application with Maven
5. ✅ Deploys EAR to WildFly
6. ✅ Prompts to start WildFly server
7. ✅ Displays access URLs

### Batch Script (`setup-and-deploy.bat`)
- Same as above but for Windows Command Prompt
- Fully automated

---

## Expected Output

```
╔════════════════════════════════════════════════════════════╗
║   FSTBANK - Automated Deployment for WildFly              ║
╚════════════════════════════════════════════════════════════╝

[*] Step 1: Checking Maven
[✓] Maven found

[*] Step 2: Checking WildFly
[✓] WildFly found

[*] Step 3: Setting up WildFly Admin User
[✓] Admin user created

[*] Step 4: Building FSTBANK Application
[✓] Application built successfully

[*] Step 5: Deploying to WildFly
[✓] Application deployed

╔════════════════════════════════════════════════════════════╗
║   DEPLOYMENT COMPLETE!                                    ║
╚════════════════════════════════════════════════════════════╝

Next Steps:
1. Start WildFly:
   cd 'C:\Users\DELL\wildfly-20.0.0.Final\bin'
   .\standalone.bat

2. Wait for server to start (look for 'started' message)

3. Access Dashboard:
   http://localhost:8080/fstbank
```

---

## After Deployment

### ✅ Server Running?
```
WildFly Full 20.0.0.Final (WildFly Core 12.0.0.Final) started
```

### ✅ Access Dashboard
```
http://localhost:8080/fstbank
```

### ✅ Test REST API
```
http://localhost:8080/fstbank/api/dashboard/status
```

### ✅ Admin Console
```
http://localhost:9990
Username: admin
Password: admin123
```

---

## Troubleshooting

### "PowerShell script execution disabled"
```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope CurrentUser
```

### "Maven not found"
Script will download automatically. Restart terminal after installation.

### "WildFly not starting"
1. Check logs: `C:\Users\DELL\wildfly-20.0.0.Final\standalone\log\server.log`
2. Ensure port 8080 is free
3. Check Java is installed: `java -version`

### "Dashboard not loading"
1. Wait 10 seconds for full startup
2. Refresh browser (Ctrl+F5)
3. Check WildFly logs

---

## What You Get

### 🏦 Dashboard
- Real-time statistics
- Client management
- Account monitoring
- Transaction history

### 💻 REST API
- `/api/dashboard/status` - Server status
- `/api/dashboard/stats` - Statistics
- `/api/dashboard/clients` - Client list
- `/api/dashboard/accounts` - Account list
- `/api/dashboard/transactions` - Transaction history

### 🔧 Backend
- Stateful EJB Session Beans
- JPA/Hibernate persistence
- H2 embedded database
- Design patterns (Factory, Strategy, Observer)

---

## System Requirements

- Java 11+ ✅ (You have Java 17)
- 2GB RAM minimum
- 500MB disk space
- Windows/Linux/Mac

---

## Ready?

**Run one of these commands now:**

### PowerShell (Recommended):
```powershell
cd C:\Users\DELL\Desktop\adla project; powershell -ExecutionPolicy Bypass -File deploy.ps1
```

### Batch:
```bash
cd C:\Users\DELL\Desktop\adla project && setup-and-deploy.bat
```

---

**Your dashboard will be live in 5 minutes! 🎉**
