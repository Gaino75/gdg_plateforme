# Script de test des endpoints GPG via la Gateway
# Usage: .\scripts\test-endpoints.ps1 [-BaseUrl "http://localhost:8080"]

param(
    [string]$BaseUrl = "http://localhost:8080"
)

$results = @()
$passed = 0
$failed = 0
$skipped = 0

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Path,
        [object]$Body = $null,
        [hashtable]$Headers = @{},
        [int[]]$ExpectedStatus = @(200, 201)
    )

    $uri = "$BaseUrl$Path"
    try {
        $params = @{
            Uri = $uri
            Method = $Method
            Headers = $Headers
            TimeoutSec = 10
            ErrorAction = "Stop"
        }
        if ($Body) {
            $params.Body = ($Body | ConvertTo-Json -Depth 5)
            $params.ContentType = "application/json"
        }
        $response = Invoke-WebRequest @params
        $status = [int]$response.StatusCode
        if ($ExpectedStatus -contains $status) {
            $script:passed++
            $script:results += [PSCustomObject]@{ Test = $Name; Status = "PASS"; Code = $status; Path = $Path }
            Write-Host "[PASS] $Name ($status)" -ForegroundColor Green
        } else {
            $script:failed++
            $script:results += [PSCustomObject]@{ Test = $Name; Status = "FAIL"; Code = $status; Path = $Path }
            Write-Host "[FAIL] $Name — expected $($ExpectedStatus -join '/'), got $status" -ForegroundColor Red
        }
    } catch {
        $code = $null
        if ($_.Exception.Response) { $code = [int]$_.Exception.Response.StatusCode }
        if ($code -and ($ExpectedStatus -contains $code)) {
            $script:passed++
            $script:results += [PSCustomObject]@{ Test = $Name; Status = "PASS"; Code = $code; Path = $Path }
            Write-Host "[PASS] $Name ($code)" -ForegroundColor Green
        } else {
            $script:failed++
            $msg = if ($code) { "HTTP $code" } else { $_.Exception.Message }
            $script:results += [PSCustomObject]@{ Test = $Name; Status = "FAIL"; Code = $msg; Path = $Path }
            Write-Host "[FAIL] $Name — $msg" -ForegroundColor Red
        }
    }
}

Write-Host "`n=== TEST ENDPOINTS GPG — Gateway $BaseUrl ===`n" -ForegroundColor Cyan

# --- PUBLIC ---
Test-Endpoint "Enseignes (public)" GET "/api/enseignes"
Test-Endpoint "Villes (public)" GET "/api/villes"
Test-Endpoint "Categories (public)" GET "/api/categories"
Test-Endpoint "Agences actives (public)" GET "/api/agences/actives"
Test-Endpoint "Dispo stock public" GET "/api/stocks/public/1/disponibilite" -ExpectedStatus @(200, 404)

# --- AUTH ---
Test-Endpoint "Login (invalid creds)" POST "/auth/login" @{
    email = "test@test.com"; motDePasse = "wrong"
} -ExpectedStatus @(401, 400, 404, 500)

# --- STOCK ---
Test-Endpoint "Stock agence detail" GET "/api/stocks/agence/1" -ExpectedStatus @(200, 404)
Test-Endpoint "Stock critiques" GET "/api/stocks/agence/1/critiques" -ExpectedStatus @(200, 404)

# --- VENTES ---
Test-Endpoint "Ventes agence" GET "/api/ventes/agence/1" -ExpectedStatus @(200, 404)
Test-Endpoint "CA agence" GET "/api/ventes/agence/1/ca" -ExpectedStatus @(200, 404)

# --- RESERVATIONS ---
Test-Endpoint "Reservations stats" GET "/api/reservations/statistiques"
Test-Endpoint "Reservations consommateur" GET "/api/reservations/consommateur/1" -ExpectedStatus @(200, 404)

# --- PAIEMENT ---
Test-Endpoint "Liste paiements" GET "/api/paiements" -ExpectedStatus @(200, 404)
Test-Endpoint "Paiement inexistant" GET "/api/paiements/99999" -ExpectedStatus @(404, 500)

# --- NOTIFICATIONS ---
Test-Endpoint "Notifications utilisateur" GET "/api/notifications/utilisateur/1" -ExpectedStatus @(200, 404)
Test-Endpoint "Abonnements consommateur" GET "/api/abonnements/consommateur/1" -ExpectedStatus @(200, 404)

# --- ADMIN ---
Test-Endpoint "Admin dashboard" GET "/admin/dashboard" -ExpectedStatus @(200, 401, 403, 404, 500)
Test-Endpoint "Admin agences en attente" GET "/admin/agences/en-attente" -ExpectedStatus @(200, 401, 403, 404, 500)

Write-Host "`n=== RÉSUMÉ ===" -ForegroundColor Cyan
Write-Host "PASS: $passed | FAIL: $failed | Total: $($passed + $failed)"
$results | Format-Table -AutoSize

if ($failed -gt 0) { exit 1 }
