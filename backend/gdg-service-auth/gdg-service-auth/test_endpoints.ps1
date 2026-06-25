# PowerShell script to test all the gdg-service-auth endpoints

$baseUrl = "http://localhost:8081/auth"
$uniqueId = Get-Random -Minimum 1000 -Maximum 9999
$email = "testuser$uniqueId@example.com"
$password = "SecretPassword123!"

Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "Starting Authentication Service Endpoints Test" -ForegroundColor Cyan
Write-Host "Target Email: $email" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

# 1. Register a new user
Write-Host "`n[1/10] Testing Register Endpoint (/register)..." -ForegroundColor Yellow
$registerBody = @{
    nom = "Doe"
    prenom = "John"
    email = $email
    motDePasse = $password
    role = "CONSOMMATEUR"
    telephone = "0612345678"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/register" -Method Post -Body $registerBody -ContentType "application/json"
    Write-Host "Register Success!" -ForegroundColor Green
    Write-Host ($registerResponse | ConvertTo-Json) -ForegroundColor Gray
} catch {
    Write-Error "Register Failed: $_"
    exit 1
}

# 2. Verify Email
Write-Host "`n[2/10] Testing Verify Email Endpoint (/verify-email)..." -ForegroundColor Yellow
$verifyBody = @{
    token = $email
} | ConvertTo-Json

try {
    $verifyResponse = Invoke-RestMethod -Uri "$baseUrl/verify-email" -Method Post -Body $verifyBody -ContentType "application/json"
    Write-Host "Verify Email Success!" -ForegroundColor Green
    Write-Host ($verifyResponse | ConvertTo-Json) -ForegroundColor Gray
} catch {
    Write-Error "Verify Email Failed: $_"
    exit 1
}

# 3. Login
Write-Host "`n[3/10] Testing Login Endpoint (/login)..." -ForegroundColor Yellow
$loginBody = @{
    email = $email
    motDePasse = $password
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "Login Success!" -ForegroundColor Green
    Write-Host ($loginResponse | ConvertTo-Json) -ForegroundColor Gray
    $jwtToken = $loginResponse.token
} catch {
    Write-Error "Login Failed: $_"
    exit 1
}

# 4. Validate Token
Write-Host "`n[4/10] Testing Validate Token Endpoint (/validate)..." -ForegroundColor Yellow
try {
    $validateResponse = Invoke-RestMethod -Uri "$baseUrl/validate" -Method Get -Headers @{ Authorization = "Bearer $jwtToken" }
    Write-Host "Validate Token Response: $validateResponse" -ForegroundColor Green
} catch {
    Write-Error "Validate Token Failed: $_"
}

# 5. Refresh Token
Write-Host "`n[5/10] Testing Refresh Token Endpoint (/refresh-token)..." -ForegroundColor Yellow
$refreshBody = @{
    refreshToken = $jwtToken
} | ConvertTo-Json

try {
    $refreshResponse = Invoke-RestMethod -Uri "$baseUrl/refresh-token" -Method Post -Body $refreshBody -ContentType "application/json"
    Write-Host "Refresh Token Success!" -ForegroundColor Green
    Write-Host ($refreshResponse | ConvertTo-Json) -ForegroundColor Gray
    $newJwtToken = $refreshResponse.token
} catch {
    Write-Error "Refresh Token Failed: $_"
}

# 6. Forgot Password (to get a reset token)
Write-Host "`n[6/10] Testing Forgot Password Endpoint (/forgot-password)..." -ForegroundColor Yellow
$forgotBody = @{
    email = $email
} | ConvertTo-Json

try {
    $forgotResponse = Invoke-RestMethod -Uri "$baseUrl/forgot-password" -Method Post -Body $forgotBody -ContentType "application/json"
    Write-Host "Forgot Password Success!" -ForegroundColor Green
    Write-Host ($forgotResponse | ConvertTo-Json) -ForegroundColor Gray
    if ($forgotResponse.message -match "Token de reset : (.*)") {
        $resetToken = $Matches[1].Trim()
        Write-Host "Extracted Reset Token: $resetToken" -ForegroundColor Green
    } else {
        Write-Warning "Could not extract reset token."
    }
} catch {
    Write-Error "Forgot Password Failed: $_"
}

# 7. Reset Password
if ($resetToken) {
    Write-Host "`n[7/10] Testing Reset Password Endpoint (/reset-password)..." -ForegroundColor Yellow
    $newPassword = "NewSecretPassword123!"
    $resetBody = @{
        token = $resetToken
        newMotDePasse = $newPassword
    } | ConvertTo-Json

    try {
        $resetResponse = Invoke-RestMethod -Uri "$baseUrl/reset-password" -Method Post -Body $resetBody -ContentType "application/json"
        Write-Host "Reset Password Success!" -ForegroundColor Green
        Write-Host ($resetResponse | ConvertTo-Json) -ForegroundColor Gray
        
        # Test login with the new password
        $newLoginBody = @{
            email = $email
            motDePasse = $newPassword
        } | ConvertTo-Json
        $newLoginResponse = Invoke-RestMethod -Uri "$baseUrl/login" -Method Post -Body $newLoginBody -ContentType "application/json"
        Write-Host "Login with new password succeeded!" -ForegroundColor Green
    } catch {
        Write-Error "Reset Password or subsequent login failed: $_"
    }
} else {
    Write-Warning "Skipping Reset Password test."
}

# 8. Get All Users (Admin)
Write-Host "`n[8/10] Testing Admin Get All Users Endpoint (/admin/users)..." -ForegroundColor Yellow
try {
    $usersResponse = Invoke-RestMethod -Uri "$baseUrl/admin/users" -Method Get
    Write-Host "Get All Users Success! Total users in database: $($usersResponse.Count)" -ForegroundColor Green
    
    # Find our created user to get their ID
    $targetUser = $usersResponse | Where-Object { $_.email -eq $email }
    if ($targetUser) {
        $targetUserId = $targetUser.id
        Write-Host "Found test user ID: $targetUserId" -ForegroundColor Green
    } else {
        Write-Error "Test user not found in the users list!"
        exit 1
    }
} catch {
    Write-Error "Get All Users Failed: $_"
    exit 1
}

# 9. Suspend User (Admin)
if ($targetUserId) {
    Write-Host "`n[9/10] Testing Admin Suspend User Endpoint (/admin/users/{id}/suspend)..." -ForegroundColor Yellow
    try {
        $suspendResponse = Invoke-RestMethod -Uri "$baseUrl/admin/users/$targetUserId/suspend" -Method Put
        Write-Host "Suspend User Success!" -ForegroundColor Green
        Write-Host ($suspendResponse | ConvertTo-Json) -ForegroundColor Gray
        Write-Host "User Status is now: $($suspendResponse.statut)" -ForegroundColor Green
    } catch {
        Write-Error "Suspend User Failed: $_"
    }
}

# 10. Delete User (Admin)
if ($targetUserId) {
    Write-Host "`n[10/10] Testing Admin Delete User Endpoint (/admin/users/{id})..." -ForegroundColor Yellow
    try {
        $deleteResponse = Invoke-RestMethod -Uri "$baseUrl/admin/users/$targetUserId" -Method Delete
        Write-Host "Delete User Success! Response: $deleteResponse" -ForegroundColor Green
        
        # Verify the user is deleted
        $usersResponseAfter = Invoke-RestMethod -Uri "$baseUrl/admin/users" -Method Get
        $userCheck = $usersResponseAfter | Where-Object { $_.id -eq $targetUserId }
        if ($null -eq $userCheck) {
            Write-Host "Verification successful: User with ID $targetUserId no longer exists in DB." -ForegroundColor Green
        } else {
            Write-Error "Verification failed: User with ID $targetUserId still exists in DB."
        }
    } catch {
        Write-Error "Delete User Failed: $_"
    }
}

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "Authentication Service Endpoints Test Complete" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
