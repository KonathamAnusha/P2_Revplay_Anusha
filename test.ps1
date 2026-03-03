# RevPlay - Test Script
# Automatically uses JDK 21 to run all tests

$env:JAVA_HOME = "C:\Program Files\Java\jdk21.0.10_7"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Using Java: $(java -version 2>&1 | Select-String 'version')" -ForegroundColor Cyan
Write-Host "Running all tests..." -ForegroundColor Green

.\mvnw.cmd test
