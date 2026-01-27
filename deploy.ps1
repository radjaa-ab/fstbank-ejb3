# FSTBANK - Automated Deployment Script (PowerShell)
# This script handles complete deployment setup

param(
    [switch]$SkipMaven = $false,
    [switch]$SkipWildFly = $false,
    [switch]$OnlyDeploy = $false
)

$ErrorActionPreference = "Stop"

# Colors
$Green = "Green"
$Red = "Red"
$Yellow = "Yellow"

function Write-Success { Write-Host "[✓] $args" -ForegroundColor $Green }
function Write-Error-Custom { Write-Host "[✗] $args" -ForegroundColor $Red }
function Write-Info { Write-Host "[*] $args" -ForegroundColor $Yellow }

Clear-Host
Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║   FSTBANK - Automated Deployment for WildFly              ║" -ForegroundColor Magenta
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Magenta
Write-Host ""

$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$UserProfile = $env:USERPROFILE
$MavenHome = "$UserProfile\maven-3.8.6"
$WildFlyHome = "$UserProfile\wildfly-20.0.0.Final"

# ============================================================
# 1. Check/Setup Maven
# ============================================================
if (-not $SkipMaven -and -not $OnlyDeploy) {
    Write-Info "Step 1: Checking Maven"
    
    $MavenCmd = Get-Command mvn -ErrorAction SilentlyContinue
    if (-not $MavenCmd) {
        Write-Info "Maven not found. Would you like to download? (Y/n)"
        $Response = Read-Host
        if ($Response -ne "n") {
            Write-Info "Downloading Maven 3.8.6..."
            $TempFile = "$env:TEMP\maven.zip"
            
            try {
                [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
                Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip" `
                    -OutFile $TempFile -UseBasicParsing
                
                Write-Info "Extracting Maven..."
                Expand-Archive -Path $TempFile -DestinationPath $UserProfile -Force
                Rename-Item -Path "$UserProfile\apache-maven-3.8.6" -NewName "maven-3.8.6" -Force
                Remove-Item $TempFile
                
                $env:PATH = "$MavenHome\bin;$env:PATH"
                Write-Success "Maven installed at: $MavenHome"
            } catch {
                Write-Error-Custom "Failed to download Maven: $_"
                Write-Info "Please download manually from: https://maven.apache.org/download.cgi"
                exit 1
            }
        }
    } else {
        Write-Success "Maven found"
    }
}

# ============================================================
# 2. Check/Setup WildFly
# ============================================================
if (-not $SkipWildFly -and -not $OnlyDeploy) {
    Write-Info "Step 2: Checking WildFly"
    
    if (-not (Test-Path "$WildFlyHome\bin\standalone.bat")) {
        Write-Info "WildFly not found. Would you like to download? (Y/n)"
        $Response = Read-Host
        if ($Response -ne "n") {
            Write-Info "Downloading WildFly 20.0.0.Final..."
            $TempFile = "$env:TEMP\wildfly.zip"
            
            try {
                [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
                Invoke-WebRequest -Uri "https://github.com/wildfly/wildfly/releases/download/20.0.0.Final/wildfly-20.0.0.Final.zip" `
                    -OutFile $TempFile -UseBasicParsing
                
                Write-Info "Extracting WildFly..."
                Expand-Archive -Path $TempFile -DestinationPath $UserProfile -Force
                Remove-Item $TempFile
                
                Write-Success "WildFly installed at: $WildFlyHome"
            } catch {
                Write-Error-Custom "Failed to download WildFly: $_"
                Write-Info "Please download manually from: https://wildfly.org/downloads/"
                exit 1
            }
        }
    } else {
        Write-Success "WildFly found"
    }
}

# ============================================================
# 3. Create WildFly Admin User
# ============================================================
if (-not $OnlyDeploy) {
    Write-Info "Step 3: Setting up WildFly Admin User"
    
    $UsersFile = "$WildFlyHome\standalone\configuration\application-users.properties"
    if (-not (Test-Path $UsersFile)) {
        Write-Info "Creating admin user (admin:admin123)..."
        "admin=admin123" | Out-File -FilePath $UsersFile -Encoding ASCII -Force
        Write-Success "Admin user created"
    } else {
        Write-Success "Admin user already exists"
    }
}

# ============================================================
# 4. Build Application
# ============================================================
if (-not $OnlyDeploy) {
    Write-Info "Step 4: Building FSTBANK Application"
    Write-Host ""
    
    Set-Location $ProjectRoot
    & mvn clean package -DskipTests -q
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Application built successfully"
    } else {
        Write-Error-Custom "Build failed!"
        exit 1
    }
}

# ============================================================
# 5. Deploy to WildFly
# ============================================================
Write-Info "Step 5: Deploying to WildFly"

$EarFile = Get-ChildItem -Path "$ProjectRoot\target" -Filter "*.ear" -ErrorAction SilentlyContinue | Select-Object -First 1

if (-not $EarFile) {
    Write-Error-Custom "No EAR file found in target directory"
    Write-Info "Make sure to run the build step first"
    exit 1
}

$DeployDir = "$WildFlyHome\standalone\deployments"
if (-not (Test-Path $DeployDir)) {
    Write-Error-Custom "WildFly deployment directory not found: $DeployDir"
    exit 1
}

Write-Info "Copying $($EarFile.Name) to WildFly deployments..."
Copy-Item -Path $EarFile.FullName -Destination $DeployDir -Force
Write-Success "Application deployed"

# ============================================================
# 6. Summary
# ============================================================
Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║   DEPLOYMENT COMPLETE!                                    ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. Start WildFly:" -ForegroundColor Cyan
Write-Host "   cd '$WildFlyHome\bin'" -ForegroundColor White
Write-Host "   .\standalone.bat" -ForegroundColor White
Write-Host ""
Write-Host "2. Wait for server to start (look for 'started' message)" -ForegroundColor Cyan
Write-Host ""
Write-Host "3. Access Dashboard:" -ForegroundColor Cyan
Write-Host "   http://localhost:8080/fstbank" -ForegroundColor White
Write-Host ""
Write-Host "4. Admin Console:" -ForegroundColor Cyan
Write-Host "   http://localhost:9990" -ForegroundColor White
Write-Host "   Username: admin" -ForegroundColor White
Write-Host "   Password: admin123" -ForegroundColor White
Write-Host ""
Write-Host "REST API:" -ForegroundColor Cyan
Write-Host "   http://localhost:8080/fstbank/api/dashboard/status" -ForegroundColor White
Write-Host ""

Write-Info "Would you like to start WildFly now? (Y/n)"
$Response = Read-Host
if ($Response -ne "n") {
    Write-Info "Starting WildFly..."
    & "$WildFlyHome\bin\standalone.bat"
}
