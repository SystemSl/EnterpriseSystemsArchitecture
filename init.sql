CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    nickname VARCHAR(10) NOT NULL,
    level INT NOT NULL,
    character_class VARCHAR(20) NOT NULL,
    race VARCHAR(20) NOT NULL,
    guild_id INT
);

CREATE TABLE guilds (
    id SERIAL PRIMARY KEY,
    leader_id INT NOT NULL,
    name VARCHAR(30) NOT NULL,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    description VARCHAR(150),
    rating INT DEFAULT 0,
    CONSTRAINT fk_guild_leader FOREIGN KEY (leader_id)
        REFERENCES players(id) ON DELETE CASCADE
);

ALTER TABLE players
ADD CONSTRAINT fk_player_guild FOREIGN KEY (guild_id)
    REFERENCES guilds(id) ON DELETE SET NULL;