# PokeTrade

Pet project: a Pokémon trading exchange between trainers.

Trainers post listings of their Pokémon (for sale or trade), browse other listings,
and complete deals.

## Tech Stack

**Current:**
- Java 21, Spring Boot 3
- Spring Data JPA, Hibernate
- PostgreSQL, Liquibase
- Maven, Docker Compose

**Planned:**
- MapStruct (DTO mapping)
- Spring Security + JWT
- S3-compatible storage (MinIO locally) for Pokémon images
- Apache Kafka (notifications)
- React + TypeScript
- JUnit 5, Testcontainers, Playwright

## Status

Actively under development.

- [x] Project bootstrap
- [x] Database schema (trainer, pokemon, listing, trade_offer)
- [ ] REST API for listings
- [ ] REST API for trade offers
- [ ] Authentication (JWT)
- [ ] Image upload (S3 / MinIO)
- [ ] Notifications (Kafka)
- [ ] Frontend
- [ ] Tests (unit + integration + E2E)

## Getting Started

### Requirements

- JDK 21
- Docker Desktop
- Maven

### Steps

```bash
git clone https://github.com/Y-Pazniak/poke-trade.git
cd poke-trade
docker compose up -d
cd backend
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.
PostgreSQL runs on `localhost:5432` (db: `poke_trade`, user/pass: `poke`/`poke`).

## Project Structure

```
poke-trade/
├── backend/              Spring Boot application
├── docker-compose.yml    Local infrastructure (PostgreSQL, later MinIO)
└── README.md
```

## Roadmap

- [x] Data model and Liquibase migrations
- [ ] Listings CRUD
- [ ] Offers and trade flow
- [ ] Authentication (JWT)
- [ ] S3 / MinIO integration for Pokémon images
- [ ] Kafka-based notifications
- [ ] React frontend
- [ ] E2E tests
- [ ] CI via GitHub Actions

## License

MIT