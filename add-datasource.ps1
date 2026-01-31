# Configure H2 datasource for WildFly

$cliScript = @"
embed-server --server-config=standalone.xml
data-source add --name=FSTBankDS --jndi-name=java:/FSTBankDS --driver-name=h2 --connection-url=jdbc:h2:mem:fstbank;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE --user-name=sa --password=sa --use-java-context=true --enabled=true
stop-embedded-server
"@

$scriptPath = "C:\Users\DELL\FSTBANK-Setup\wildfly-new\wildfly-20.0.0.Final\bin\add-ds.cli"
Set-Content -Path $scriptPath -Value $cliScript

Write-Host "Running WildFly CLI to add datasource..."
cd "C:\Users\DELL\FSTBANK-Setup\wildfly-new\wildfly-20.0.0.Final\bin"
.\jboss-cli.bat --file=add-ds.cli

Write-Host "Datasource configuration complete"
