CREATE TABLE IF NOT EXISTS students (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    points INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS players (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    elo_rating INT,
    fide_title VARCHAR(64),
    federation VARCHAR(64),
    created_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tournaments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    format VARCHAR(64),
    status VARCHAR(64),
    rounds_total INT,
    time_control VARCHAR(64),
    k_factor INT,
    start_date DATE,
    created_by BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rounds (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tournament_id BIGINT,
    round_number INT,
    status VARCHAR(64),
    generated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_round_tournament FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

CREATE TABLE IF NOT EXISTS pairings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    round_id BIGINT,
    player_white BIGINT,
    player_black BIGINT,
    board_number INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_pairing_round FOREIGN KEY (round_id) REFERENCES rounds (id),
    CONSTRAINT fk_pairing_white FOREIGN KEY (player_white) REFERENCES players (id),
    CONSTRAINT fk_pairing_black FOREIGN KEY (player_black) REFERENCES players (id)
);

CREATE TABLE IF NOT EXISTS game_results (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pairing_id BIGINT,
    result VARCHAR(32),
    forfeit BOOLEAN,
    played_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_result_pairing FOREIGN KEY (pairing_id) REFERENCES pairings (id)
);

CREATE TABLE IF NOT EXISTS elo_history (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT,
    tournament_id BIGINT,
    rating_before INT,
    rating_after INT,
    delta INT,
    recorded_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_elo_player FOREIGN KEY (player_id) REFERENCES players (id),
    CONSTRAINT fk_elo_tournament FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);