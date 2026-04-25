$ErrorActionPreference = 'Stop'

$gradleVersion = "8.7"
$gradleZip = "gradle-$gradleVersion-bin.zip"
$gradleUrl = "https://services.gradle.org/distributions/$gradleZip"
$localGradleDir = "$PSScriptRoot\.gradle-local"
$extractDir = "$localGradleDir\gradle-$gradleVersion"

if (-not (Test-Path $localGradleDir)) {
    New-Item -ItemType Directory -Path $localGradleDir | Out-Null
}

if (-not (Test-Path "$localGradleDir\$gradleZip")) {
    Write-Host "Downloading Gradle $gradleVersion..."
    Invoke-WebRequest -Uri $gradleUrl -OutFile "$localGradleDir\$gradleZip"
}

if (-not (Test-Path $extractDir)) {
    Write-Host "Extracting Gradle..."
    Expand-Archive -Path "$localGradleDir\$gradleZip" -DestinationPath $localGradleDir -Force
}

$gradleBin = "$extractDir\bin\gradle.bat"

Write-Host "Building APK..."
Set-Location $PSScriptRoot
& $gradleBin assembleDebug

if ($LASTEXITCODE -ne 0) {
    Write-Error "Gradle build failed with exit code $LASTEXITCODE"
    exit $LASTEXITCODE
}

Write-Host "Build successful."
Copy-Item -Path "$PSScriptRoot\app\build\outputs\apk\debug\app-debug.apk" -Destination "$PSScriptRoot\app-debug.apk" -Force
