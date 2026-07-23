 Write-Host "Nettoyage : fermeture des anciens processus java..." -ForegroundColor Cyan
 Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
 Start-Sleep -Seconds 3
 
 Write-Host "Verification de docker..." -ForegroundColor Cyan
 $dockerReady = $false
 $tentative = 0
 While (-not $dockerReady -and $tentative -lt 15){
       docker version > $null 2>&1
       if($LASTEXITCODE -eq 0){
        $dockerReady = $true
       }else{
        $tentative = $tentative + 1
        Write-Host "Docker pas encore pret,attente....($tentative/15)" -foregroundColor Yellow
        Start-Sleep -Seconds 5
       }
 }
 if (-not $dockerReady){
    Write-Host "Docker Desktop ne repond pas.Ouvre Docker Desktop manuellement puis relance ce script." -ForegroundColor Red
    exit
 }

 Write-Host "Nettoyage :suppression de l'ancien conteneur RabbitMQ s'il existe..." -ForegroundColor Cyan
 
# pour utiliser Rabbit MQ avec docker si on l'a installer avec Docker
docker rm -f gpg-rabbitmq > $null 2>&1

#Demarrage plutoy RabbitMQ sevice Windows avec ces commentees lignes en dessous si on a installer plutot le .exe rabbit
 # write-Host "Docker est pret.Demarrage de RabbitMQ Service Windows...." -ForegroundColor Cyan
# net start RabbitMQ
# Start-Sleep _Seconds 8


  
write-Host "Docker est pret.Demarrage de RabbitMQ et PostgreSQL...." -ForegroundColor Cyan
docker compose -f ..\docker-compose.yml up -d rabbitmq postgres
Start-Sleep -Seconds 8
 
 
 

$services =@(
    
     "gdg-service-admin\gdg-service-admin",
     "gdg-service-agence",
     "gdg-service-auth\gdg-service-auth",
     "gdg-service-notification",
     "gdg-service-paiement",
     "gdg-service-reservations",
     "gdg-service-stock",
     "gdg-service-ventes",
     "gdg-gateway\gateway",
     "gdg-config-server"

)

   
    $i = 0
    foreach ($service in $services){
        $i=$i + 1
        Write-Host "Lancement : $service" -ForegroundColor Yellow
        Start-Process Powershell -ArgumentList "-NoExit", "-Command", "cd '$service';mvn clean spring-boot:run"
        Start-Sleep -Seconds 6
        
    }

    Write-Host "Tous les services sont en cours lancement,chacun dans sa propre fenetre." -FOregroundColor Green