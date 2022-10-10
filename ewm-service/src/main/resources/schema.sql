drop table IF EXISTS Compilations_events;
drop table IF EXISTS Users;
drop table IF EXISTS Categories;
drop table IF EXISTS Location;
drop table IF EXISTS Events;
drop table IF EXISTS Requests;
drop table IF EXISTS Compilations;

CREATE TABLE IF NOT EXISTS Users
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name varchar(50),
    email     varchar(50)                             not null,
    UNIQUE (email),

    constraint USERS_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         varchar(500),
    category_id        integer,
    confirmed_requests integer,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        varchar,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       integer,
    location_id        integer,
    paid               boolean,
    participant_limit  integer,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean,
    state_id           varchar,
    title              varchar(150),
    views              integer,

    constraint Events_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar(50),

    constraint Categories_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Location
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    latitude  float,
    longitude float,

    constraint Location_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     integer,
    requester_id integer,
    status       varchar(50),

    constraint Requests_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    events integer,
    pinned boolean,
    title  varchar(200),

    constraint COMPILATIONS_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Compilations_events
(
    compilation_id integer,
    event_id       integer,

--     constraint COMPILATIONS_ID_FK
--         foreign key (compilation_id) references Compilations,
--
--     constraint EVENTS_ID_FK
--         foreign key (event_id) references Events,

    CONSTRAINT pk_compilation_event PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT FK_EVENT FOREIGN KEY (compilation_id) REFERENCES Compilations (id) ON DELETE CASCADE,
    CONSTRAINT FK_COMPILATION FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE CASCADE

);