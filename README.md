# 🎾 Tennis Tournament Planner — REST API

Backendowe REST API do zarządzania turniejami tenisowymi. Obsługuje drabinki turniejowe (single/double elimination), harmonogram meczów, wyniki oraz statystyki zawodników i turniejów.

---

## Stack technologiczny

| Warstwa | Technologia |
|---|---|
| Język | Java 17 |
| Framework | Spring Boot 3.x (Web, Security, Data JPA) |
| ORM | Hibernate 6 |
| Baza danych | MySQL 8 |
| Migracje DB | Flyway |
| Budowanie | Maven |
| Dokumentacja API | Springdoc OpenAPI (Swagger UI) |
| Testy | JUnit 5, Mockito |

---

## Funkcjonalności

- **Zarządzanie turniejami** — tworzenie, edycja, usuwanie turniejów; obsługa formatów single elimination i round-robin
- **Drabinki** — automatyczne generowanie drabinek na podstawie listy zawodników (z losowaniem lub seedowaniem)
- **Mecze** — planowanie harmonogramu, wprowadzanie wyników set po secie, automatyczne awansowanie zwycięzcy
- **Zawodnicy** — rejestracja zawodników, ranking, historia meczów
- **Statystyki** — win rate, średnia liczba setów, historia wyników per zawodnik i per turniej
- **Role użytkowników** — `ADMIN` (pełny dostęp), `REFEREE` (wprowadzanie wyników), `VIEWER` (tylko odczyt)

---

## Struktura repozytorium

```
tennis-tournament-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/tennis/
│   │   │   ├── config/               # Konfiguracja Spring Security, Swagger
│   │   │   ├── controller/           # Kontrolery REST (@RestController)
│   │   │   ├── dto/                  # Obiekty żądań i odpowiedzi (Request/Response DTO)
│   │   │   ├── exception/            # Globalna obsługa wyjątków (@ControllerAdvice)
│   │   │   ├── mapper/               # Mapowanie encja <-> DTO (MapStruct)
│   │   │   ├── model/                # Encje JPA (Player, Tournament, Match, Draw)
│   │   │   ├── repository/           # Interfejsy Spring Data JPA
│   │   │   ├── service/              # Logika biznesowa
│   │   │   │   └── draw/             # Algorytmy generowania drabinek
│   │   │   └── TennisApplication.java
│   │   └── resources/
│   │       ├── db/migration/         # Skrypty Flyway (V1__init.sql, V2__seed.sql, ...)
│   │       └── application.yml       # Konfiguracja aplikacji
│   └── test/
│       └── java/com/example/tennis/
│           ├── controller/           # Testy integracyjne kontrolerów
│           └── service/              # Testy jednostkowe serwisów
├── .env.example                      # Przykładowe zmienne środowiskowe
├── docker-compose.yml                # MySQL + API w kontenerach
├── pom.xml
└── README.md
```

---

## Wymagania wstępne

- **Java 17+**
- **Maven 3.9+**
- **MySQL 8+** (lub Docker)

---

## Instalacja i uruchomienie

### 1. Klonowanie repozytorium

```bash
git clone https://github.com/twoj-user/tennis-tournament-api.git
cd tennis-tournament-api
```

### 2. Konfiguracja bazy danych

#### Opcja A — lokalna instancja MySQL

Utwórz bazę danych i użytkownika:

```sql
CREATE DATABASE tennis_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'tennis_user'@'localhost' IDENTIFIED BY 'tennis_pass';
GRANT ALL PRIVILEGES ON tennis_db.* TO 'tennis_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Opcja B — Docker (zalecane)

```bash
docker-compose up -d mysql
```

### 3. Konfiguracja zmiennych środowiskowych

Skopiuj plik przykładowy i uzupełnij wartości:

```bash
cp .env.example .env
```

```env
# .env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=tennis_db
DB_USERNAME=tennis_user
DB_PASSWORD=tennis_pass
JWT_SECRET=zmien-na-losowy-string-min-32-znaki
JWT_EXPIRATION_MS=86400000
```

### 4. Budowanie projektu

```bash
mvn clean install -DskipTests
```

### 5. Uruchomienie aplikacji

```bash
mvn spring-boot:run
```

Lub z gotowego jara:

```bash
java -jar target/tennis-tournament-api-*.jar
```

Aplikacja startuje na `http://localhost:8080`.

### 6. Uruchomienie całego środowiska przez Docker Compose

```bash
docker-compose up --build
```

---

## Dokumentacja API

Po uruchomieniu aplikacji Swagger UI dostępny jest pod adresem:

```
http://localhost:8080/swagger-ui.html
```

Specyfikacja OpenAPI (JSON):

```
http://localhost:8080/v3/api-docs
```

---

## Główne endpointy

```
# Turnieje
GET     /api/tournaments              # Lista wszystkich turniejów
POST    /api/tournaments              # Utwórz turniej
GET     /api/tournaments/{id}         # Szczegóły turnieju
PUT     /api/tournaments/{id}         # Edytuj turniej
DELETE  /api/tournaments/{id}         # Usuń turniej

# Drabinka
POST    /api/tournaments/{id}/draw/generate   # Generuj drabinkę
GET     /api/tournaments/{id}/draw            # Pobierz drabinkę

# Mecze
GET     /api/tournaments/{id}/matches         # Lista meczów turnieju
PUT     /api/matches/{id}/result              # Wprowadź wynik meczu

# Zawodnicy
GET     /api/players                  # Lista zawodników
POST    /api/players                  # Dodaj zawodnika
GET     /api/players/{id}             # Profil zawodnika
GET     /api/players/{id}/stats       # Statystyki zawodnika

# Uwierzytelnianie
POST    /api/auth/register            # Rejestracja
POST    /api/auth/login               # Logowanie (zwraca JWT)
```

---

## Uruchomienie testów

```bash
# Wszystkie testy
mvn test

# Tylko testy jednostkowe
mvn test -Dgroups="unit"

# Tylko testy integracyjne
mvn test -Dgroups="integration"
```

---

## Schemat bazy danych (uproszczony)

```
players         tournaments       matches
────────        ───────────       ───────
id (PK)         id (PK)           id (PK)
first_name      name              tournament_id (FK)
last_name       format            round_number
ranking         start_date        player1_id (FK)
email           end_date          player2_id (FK)
created_at      status            score_player1
                created_by (FK)   score_player2
                                  winner_id (FK)
                                  scheduled_at
```

---

## Zmienne środowiskowe — pełna lista

| Zmienna | Opis | Wartość domyślna |
|---|---|---|
| `DB_HOST` | Host bazy MySQL | `localhost` |
| `DB_PORT` | Port MySQL | `3306` |
| `DB_NAME` | Nazwa bazy | `tennis_db` |
| `DB_USERNAME` | Użytkownik MySQL | — |
| `DB_PASSWORD` | Hasło MySQL | — |
| `JWT_SECRET` | Klucz do podpisywania tokenów JWT | — |
| `JWT_EXPIRATION_MS` | Czas życia tokenu (ms) | `86400000` |
| `SERVER_PORT` | Port aplikacji | `8080` |

---

## Licencja

MIT
