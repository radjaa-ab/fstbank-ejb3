# 📤 GITHUB UPLOAD GUIDE

## Step 1: Initialize Git

```powershell
cd "C:\Users\DELL\Desktop\adla project"
git init
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## Step 2: Create .gitignore

```powershell
# Create .gitignore file with:
target/
bin/
.idea/
.vscode/
*.class
*.jar
*.log
*.db
.DS_Store
```

## Step 3: Add & Commit

```powershell
git add .
git commit -m "Initial: FSTBANK EJB3 - Factory, Strategy, Observer patterns"
```

## Step 4: Create GitHub Repository

1. Go to https://github.com/new
2. Name: `fstbank-ejb3`
3. Description: "Banking application with EJB3 and Design Patterns"
4. Choose: Public
5. Click "Create repository"

## Step 5: Push to GitHub

```powershell
git remote add origin https://github.com/YOUR-USERNAME/fstbank-ejb3.git
git branch -M main
git push -u origin main
```

**Enter your GitHub credentials when prompted**

---

## ✅ Done!

Your project will be at: `https://github.com/YOUR-USERNAME/fstbank-ejb3`
