# Plateforme GPG — Gestion Distribution Gaz Domestique

Plateforme microservices Spring Boot 3.3.2 + React pour la gestion de la distribution de gaz au Cameroun.

## Démarrage rapide

```bash
# 1. Base de données
psql -U postgres -d gdg_auth -f schema.sql
psql -U postgres -d gdg_auth -f scripts/init-stock.sql

# 2. RabbitMQ (Docker Desktop requis)
docker compose up -d

# 3. Services (dans backend/, un terminal par service)
# Auth:8081 | Agences:8082 | Stock:8083 | Ventes:8084
# Réservations:8085 | Paiement:8086 | Notifications:8087 | Admin:8088 | Gateway:8080

# 4. Tests
powershell -File scripts/test-endpoints.ps1
```

## Documentation API

Voir [docs/RAPPORT_API_ENDPOINTS.md](docs/RAPPORT_API_ENDPOINTS.md) — tous les endpoints, DTOs, flux métier et exemples curl.

## Architecture

- **Gateway** (8080) : point d'entrée unique, JWT, routage
- **Sync REST** : Ventes→Stock, Réservations→Stock/Paiement, Paiement→Réservations
- **Async RabbitMQ** : Stock/Auth/Admin/Paiement/Réservations → Notifications
