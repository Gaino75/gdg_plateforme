#trouver tous les dossierqui contiennent un pom.xml
$poms =Get-ChildItem -path . -Recurse -Filter "pom.xml" -Depth 3 |
        Where-Object {$_.FullName -notmatch "\\target\\"}

$result = @()

foreach($pom in $poms){
    $folder = $pom.DirectoryName
    Write-Host "'nCompilation : $folder" -ForegroundColor Cyan
    Push-Location $folder
    mvn clean compile -q
    $success = ($LASTEXITCODE -eq 0)
    Pop-Location

    $result += [PSCustomObject]@{
        Module = $folder
        Statut = @("ECHEC","OK")[[int]$success]
    }

}

Write-Host "'n'n==========RESUME==========" -ForegroundColor Yellow
$result | ForEach-Object {
    if($_.Statut -eq "OK"){
        Write-Host "OK -- $($_.Module)" -ForegroundColor Green
            }else{
                Write-Host "ECHEC -- $($_.Module)" -ForegroundColor Red
            }
}