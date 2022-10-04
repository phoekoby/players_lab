-- CREATE DATABASE game;

CREATE TABLE IF NOT EXISTS PLAYER(
    id int8 NOT NULL,
    nickname varchar(45) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS PROGRESS(
    id int8 NOT NULL,
    player_id int8 REFERENCES PLAYER (id)
                        ON DELETE CASCADE,
    resourceId int8,
    score int CHECK(score >= 0),
    max_score int CHECK(max_score >= 0),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ITEM(
    id int8 NOT NULL,
    player_id int8 REFERENCES PLAYER (id)
                        ON DELETE CASCADE,
    resourceId int8,
    count int CHECK(count >= 0),
    level int CHECK(level >= 0),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CURRENCY(
   id int8 NOT NULL,
   player_id int8 REFERENCES PLAYER (id)
                        ON DELETE CASCADE,
    resourceId int8,
    name varchar(64) NOT NULL,
    count int CHECK(count >= 0),
    PRIMARY KEY (id)
);