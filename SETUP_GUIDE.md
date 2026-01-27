# 📥 GUIDE D'INSTALLATION - Extensions VS Code & JDK

## 🔧 ÉTAPE 1 : EXTENSIONS VS CODE NÉCESSAIRES

### Extensions à installer

1. **Extension Pack for Java** (Microsoft)
   - ID: `vscjava.vscode-java-pack`
   - Contient: Language Support, Debugger, Test Runner, Maven, etc.

2. **Spring Boot Extension Pack** (Microsoft)
   - ID: `vmware.vscode-boot-dev-pack`
   - Pour support EJB et serveurs applicatifs

3. **Extension XML** (Red Hat)
   - ID: `redhat.vscode-xml`
   - Pour éditer persistence.xml et autres fichiers XML

4. **SQL Tools** (mtxr)
   - ID: `mtxr.sqltools`
   - Pour explorer la base de données

5. **REST Client** (Huachao Mao)
   - ID: `humao.rest-client`
   - Pour tester les EJBs

### Installation rapide

**Via ligne de commande:**
```powershell
# Ouvrir PowerShell dans le dossier du projet
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-boot-dev-pack
code --install-extension redhat.vscode-xml
code --install-extension mtxr.sqltools
code --install-extension humao.rest-client
```

**Ou manuellement dans VS Code:**
1. Cliquer sur l'icône Extensions (Ctrl+Shift+X)
2. Chercher chaque extension par ID
3. Cliquer "Install"

---

## ☕ ÉTAPE 2 : TÉLÉCHARGER RED HAT JDK (OPENJDK)

### ⚠️ Le navigateur ne fonctionne pas? Utilisez la ligne de commande!

### Option 1 : Via PowerShell (Recommandé)

**Télécharger OpenJDK 17 (Compatible EJB3)**

```powershell
# Créer dossier pour JDK
mkdir C:\jdk
cd C:\jdk

# Télécharger Red Hat OpenJDK via curl
curl -L "https://developers.redhat.com/download-manager/file/openjdk-17.0.5_windows-x64_bin.zip" `
     -o openjdk-17.zip

# Ou via wget
wget "https://developers.redhat.com/download-manager/file/openjdk-17.0.5_windows-x64_bin.zip" `
     -OutFile openjdk-17.zip

# Extraire l'archive
Expand-Archive -Path openjdk-17.zip -DestinationPath C:\jdk

# Vérifier l'installation
C:\jdk\jdk-17.0.5\bin\java.exe -version
```

### Option 2 : Télécharger depuis AdoptOpenJDK (Alternative)

```powershell
# AdoptOpenJDK (gratuit, pas d'inscription)
curl -L "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.5+8/OpenJDK17U-jdk_x64_windows_hotspot_17.0.5_8.zip" `
     -o openjdk-17-adoptium.zip

Expand-Archive -Path openjdk-17-adoptium.zip -DestinationPath C:\jdk
```

### Option 3 : Utiliser Chocolatey (Automatisé)

```powershell
# Installer Chocolatey si pas encore fait
Set-ExecutionPolicy Bypass -Scope Process -Force
iex ((New-Object System.Net.ServicePointManager).SecurityProtocol = 3072; iex(New-Object Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Installer OpenJDK
choco install openjdk17 -y

# Vérifier
java -version
```

---

## 🔑 ÉTAPE 3 : CONFIGURER LES VARIABLES D'ENVIRONNEMENT

### Sur Windows

**Via PowerShell (Admin):**
```powershell
# Définir JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\jdk\jdk-17.0.5", "User")

# Ajouter Java au PATH
$path = [Environment]::GetEnvironmentVariable("PATH", "User")
$newPath = $path + ";C:\jdk\jdk-17.0.5\bin"
[Environment]::SetEnvironmentVariable("PATH", $newPath, "User")

# Vérifier
java -version
javac -version
```

**Ou via GUI:**
1. Appuyer sur `Win + Pause/Break`
2. Cliquer "Paramètres système avancés"
3. Bouton "Variables d'environnement"
4. Ajouter:
   - `JAVA_HOME` = `C:\jdk\jdk-17.0.5`
   - Ajouter `%JAVA_HOME%\bin` au PATH

---

## 🔥 ÉTAPE 4 : TÉLÉCHARGER JBOSS/WILDFLY

### Via PowerShell

```powershell
# Créer dossier
mkdir C:\wildfly
cd C:\wildfly

# Télécharger WildFly 20 (Compatible EJB3)
curl -L "https://github.com/wildfly/wildfly/releases/download/20.0.1.Final/wildfly-20.0.1.Final.zip" `
     -o wildfly-20.zip

# Extraire
Expand-Archive -Path wildfly-20.zip -DestinationPath C:\wildfly

# Vérifier
C:\wildfly\wildfly-20.0.1.Final\bin\standalone.bat --version
```

### Configurer la variable d'environnement

```powershell
[Environment]::SetEnvironmentVariable("JBOSS_HOME", "C:\wildfly\wildfly-20.0.1.Final", "User")
$path = [Environment]::GetEnvironmentVariable("PATH", "User")
$newPath = $path + ";C:\wildfly\wildfly-20.0.1.Final\bin"
[Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
```

---

## 🗄️ ÉTAPE 5 : BASE DE DONNÉES H2

H2 est déjà incluse avec Hibernate. Pas d'installation supplémentaire nécessaire!

Pour accéder à la console H2:
```
http://localhost:8080/h2-console
```

---

## ✅ VÉRIFIER L'INSTALLATION

```powershell
# Vérifier Java
java -version
javac -version

# Vérifier Maven (si utilisé)
mvn -version

# Vérifier JBoss
cd $env:JBOSS_HOME\bin
.\standalone.bat --version
```

---

## 🚀 DÉMARRER LE DÉVELOPPEMENT

### Démarrer JBoss

```powershell
$env:JBOSS_HOME\bin\standalone.bat
```

L'application est accessible à:
- Console: `http://localhost:9990`
- Utilisateur: `admin`
- Mot de passe: `admin`

### Compiler le projet

```bash
javac -d target/classes -cp "lib/*" src/main/java/dz/fst/bank/**/*.java
```

### Déployer

```bash
# Copier le JAR vers JBoss
copy FSTBankEJB.jar $env:JBOSS_HOME\standalone\deployments\
```

---

## 📋 TROUBLESHOOTING

| Problème | Solution |
|----------|----------|
| `java: command not found` | Ajouter `%JAVA_HOME%\bin` au PATH |
| `JBOSS_HOME not recognized` | Relancer PowerShell après la configuration |
| `WildFly won't start` | Vérifier que le port 8080 est libre |
| `H2 database error` | Vérifier les permissions sur le dossier |

---

## 📚 RESSOURCES ADDITIONNELLES

- [OpenJDK Official](https://openjdk.java.net/)
- [WildFly Documentation](https://docs.wildfly.org/)
- [Hibernate ORM](https://hibernate.org/)
- [H2 Database](https://www.h2database.com/)

**Version:** 1.0.0  
**Date:** 27 Janvier 2026
