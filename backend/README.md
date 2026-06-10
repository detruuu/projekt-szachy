# ♟️ Chess Tournament Planner - REST API

Backendowe REST API do zarządzania turniejami szachowymi w systemie szwajcarskim. Automatyzuje parowanie rund zgodnie z przepisami FIDE, oblicza zmiany ratingu ELO po każdym turnieju oraz udostępnia statystyki zawodników i historię ratingów.

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

- **Zarządzanie turniejami** — tworzenie, edycja i usuwanie turniejów; konfiguracja liczby rund, kontroli czasu i wariantu szwajcarskiego
- **Algorytm szwajcarski** — automatyczne parowanie rund zgodnie z FIDE: grupowanie punktowe, balansowanie kolorów (białe/czarne), unikanie powtórzeń przeciwników, przydział bye
- **Mecze** — rejestracja wyników partii (1-0, 0-1, ½-½, walkower), automatyczna aktualizacja tabeli po każdej rundzie
- **System ELO** — przeliczanie ratingu po zamknięciu turnieju wzorem FIDE (ΔR = K × (Wynik − Oczekiwany)), trwała historia zmian
- **Zawodnicy** — rejestracja, aktualny rating ELO, tytuł FIDE, przynależność federacyjna
- **Statystyki** — tabela krzyżowa, współczynniki tiebreak (Buchholz, Sonneborn-Berger), historia ELO per zawodnik
- **Role użytkowników** — `ADMIN` (pełny dostęp), `ARBITER` (zarządzanie turniejem), `PLAYER` (tylko odczyt własnych danych), `VIEWER` (publiczny odczyt)

---

## Struktura repozytorium

```
chess-tournament-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/chess/
│   │   │   ├── config/               # Konfiguracja Spring Security, Swagger, CORS
│   │   │   ├── controller/           # Kontrolery REST (@RestController)
│   │   │   ├── dto/                  # Obiekty żądań i odpowiedzi (Request/Response DTO)
│   │   │   ├── exception/            # Globalna obsługa wyjątków (@ControllerAdvice)
│   │   │   ├── mapper/               # Mapowanie encja <-> DTO
│   │   │   ├── model/                # Encje JPA
│   │   │   │   ├── Player.java
│   │   │   │   ├── Tournament.java
│   │   │   │   ├── Round.java
│   │   │   │   ├── Pairing.java
│   │   │   │   ├── GameResult.java
│   │   │   │   └── EloHistory.java
│   │   │   ├── repository/           # Interfejsy Spring Data JPA
│   │   │   ├── service/              # Logika biznesowa
│   │   │   │   ├── SwissPairingService.java   # Silnik parowania szwajcarskiego
│   │   │   │   ├── EloCalculationService.java # Obliczenia ratingu ELO
│   │   │   │   ├── TournamentService.java
│   │   │   │   ├── PlayerService.java
│   │   │   │   └── StandingsService.java      # Tabele, tiebreaki
│   │   │   └── ChessApplication.java
│   │   └── resources/
│   │       ├── db/migration/         # Skrypty Flyway (V1__init.sql, V2__seed.sql, ...)
│   │       └── application.yml       # Konfiguracja aplikacji
│   └── test/
│       └── java/com/example/chess/
│           ├── controller/           # Testy integracyjne kontrolerów
│           └── service/              # Testy jednostkowe (parowanie, ELO)
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
git clone https://github.com/twoj-user/chess-tournament-api.git
cd chess-tournament-api
```

### 2. Konfiguracja bazy danych

#### Opcja A — lokalna instancja MySQL

```sql
CREATE DATABASE chess_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'chess_user'@'localhost' IDENTIFIED BY 'chess_pass';
GRANT ALL PRIVILEGES ON chess_db.* TO 'chess_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Opcja B — Docker (zalecane)

```bash
docker-compose up -d mysql
```

### 3. Konfiguracja zmiennych środowiskowych

```bash
cp .env.example .env
```

```env
# .env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=chess_db
DB_USERNAME=chess_user
DB_PASSWORD=chess_pass
JWT_SECRET=zmien-na-losowy-string-min-32-znaki
JWT_EXPIRATION_MS=86400000
DEFAULT_ELO_K_FACTOR=20
INITIAL_ELO_RATING=1000
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
java -jar target/chess-tournament-api-*.jar
```

Aplikacja startuje na `http://localhost:8080`.

### 6. Uruchomienie całego środowiska przez Docker Compose

```bash
docker-compose up --build
```

---

## Dokumentacja API

Po uruchomieniu Swagger UI dostępny jest pod adresem:

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
# Uwierzytelnianie
POST    /api/auth/register                        # Rejestracja konta
POST    /api/auth/login                           # Logowanie (zwraca JWT)

# Turnieje
GET     /api/tournaments                          # Lista wszystkich turniejów
POST    /api/tournaments                          # Utwórz turniej         [ARBITER]
GET     /api/tournaments/{id}                     # Szczegóły turnieju
PUT     /api/tournaments/{id}                     # Edytuj turniej         [ARBITER]
DELETE  /api/tournaments/{id}                     # Usuń turniej           [ARBITER]
POST    /api/tournaments/{id}/close               # Zamknij i przelicz ELO [ARBITER]

# Rejestracja zawodników
GET     /api/tournaments/{id}/players             # Lista zarejestrowanych
POST    /api/tournaments/{id}/players             # Zarejestruj zawodnika  [ARBITER]
DELETE  /api/tournaments/{id}/players/{playerId}  # Wyrejestruj zawodnika  [ARBITER]

# Rundy i parowania
GET     /api/tournaments/{id}/rounds              # Lista rund turnieju
POST    /api/tournaments/{id}/rounds/generate     # Generuj następną rundę [ARBITER]
GET     /api/tournaments/{id}/rounds/{round}      # Parowania danej rundy
PUT     /api/pairings/{id}/result                 # Wprowadź wynik partii  [ARBITER]

# Tabela wyników
GET     /api/tournaments/{id}/standings           # Aktualna tabela z tiebrakami
GET     /api/tournaments/{id}/crosstable          # Tabela krzyżowa

# Zawodnicy
GET     /api/players                              # Lista zawodników
POST    /api/players                              # Dodaj zawodnika        [ADMIN]
GET     /api/players/{id}                         # Profil zawodnika
GET     /api/players/{id}/elo-history             # Historia ratingu ELO
GET     /api/players/{id}/stats                   # Statystyki zawodnika
```

---

## Logika systemu szwajcarskiego

Silnik parowania (`SwissPairingService`) implementuje następujące zasady:

1. Zawodnicy sortowani malejąco według liczby punktów, następnie według ratingu ELO w obrębie grupy punktowej
2. Parowanie odbywa się od góry tabeli w obrębie każdej grupy punktowej
3. **Zakaz powtórzeń** — żadna para zawodników nie może spotkać się dwukrotnie w tym samym turnieju
4. **Balansowanie kolorów** — każdy zawodnik otrzymuje naprzemiennie białe i czarne; naruszenia minimalizowane
5. **Bye** — przy nieparzystej liczbie zawodników najniżej sklasyfikowany otrzymuje wolny (1 punkt); każdy zawodnik może dostać bye maksymalnie raz
6. Arbiter może ręcznie zamienić dwa parowania przed ich zatwierdzeniem

---

## Wzór ELO (FIDE)

```
Oczekiwany = 1 / (1 + 10^((Rb - Ra) / 400))
Nowy rating = Ra + K × (Wynik - Oczekiwany)
```

| Warunek | Współczynnik K |
|---|---|
| Rating < 2400 | 20 |
| Rating ≥ 2400 | 10 |
| Nowy zawodnik (< 30 partii) | 40 |

Zmiany ELO obliczane są na podstawie ratingów **sprzed turnieju** i aplikowane jednorazowo po jego zamknięciu.

---

## Schemat bazy danych (uproszczony)

```
players              tournaments          rounds
────────             ───────────          ──────
id (PK)              id (PK)              id (PK)
first_name           name                 tournament_id (FK)
last_name            format               round_number
email                status               status
elo_rating           rounds_total         generated_at
fide_title           time_control
federation           k_factor
created_at           start_date
                     created_by (FK)

pairings             game_results         elo_history
────────             ────────────         ───────────
id (PK)              id (PK)              id (PK)
round_id (FK)        pairing_id (FK)      player_id (FK)
player_white (FK)    result               tournament_id (FK)
player_black (FK)    forfeit              rating_before
board_number         played_at            rating_after
                                          delta
                                          recorded_at
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

## Zmienne środowiskowe — pełna lista

| Zmienna | Opis | Wartość domyślna |
|---|---|---|
| `DB_HOST` | Host bazy MySQL | `localhost` |
| `DB_PORT` | Port MySQL | `3306` |
| `DB_NAME` | Nazwa bazy | `chess_db` |
| `DB_USERNAME` | Użytkownik MySQL | — |
| `DB_PASSWORD` | Hasło MySQL | — |
| `JWT_SECRET` | Klucz podpisywania tokenów JWT | — |
| `JWT_EXPIRATION_MS` | Czas życia tokenu (ms) | `86400000` |
| `DEFAULT_ELO_K_FACTOR` | Domyślny współczynnik K | `20` |
| `INITIAL_ELO_RATING` | Startowy rating dla nowych zawodników | `1000` |
| `SERVER_PORT` | Port aplikacji | `8080` |

---

## Licencja

MIT
