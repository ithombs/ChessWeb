-- Database: chessWeb

DROP DATABASE "chessWeb";
CREATE DATABASE "chessWeb"
    WITH 
    OWNER = ian
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_GB.UTF-8'
    LC_CTYPE = 'en_GB.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
    
    

-- Table: public.chess_game

DROP TABLE public.chess_game;
CREATE TABLE public.chess_game
(
    gameid integer NOT NULL DEFAULT nextval('chess_game_gameid_seq'::regclass),
    gamedate timestamp without time zone,
    playerblack integer,
    playerwhite integer,
    winner integer,
    CONSTRAINT chess_game_pkey PRIMARY KEY (gameid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.chess_game
    OWNER to ian;
    
    
    
    
-- Table: public.chess_move_list

DROP TABLE public.chess_move_list;
CREATE TABLE public.chess_move_list
(
    gameid integer NOT NULL,
    movenumber integer NOT NULL,
    move character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT chess_move_list_pkey PRIMARY KEY (gameid, movenumber),
    CONSTRAINT chess_move_list_gameid_fkey FOREIGN KEY (gameid)
        REFERENCES public.chess_game (gameid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.chess_move_list
    OWNER to ian;
    
    
    
-- Table: public.user_roles

DROP TABLE public.user_roles;
CREATE TABLE public.user_roles
(
    userid integer NOT NULL,
    role character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (userid, role),
    CONSTRAINT user_roles_userid_fkey FOREIGN KEY (userid)
        REFERENCES public.users (userid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_roles
    OWNER to ian;
    
    
-- Table: public.users

DROP TABLE public.users;
CREATE TABLE public.users
(
    userid integer NOT NULL DEFAULT nextval('users_userid_seq'::regclass),
    username character varying(50) COLLATE pg_catalog."default",
    email character varying(50) COLLATE pg_catalog."default",
    password character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (userid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to ian;
    
-- Table: public.activity_audit

DROP TABLE public.activity_audit;
CREATE TABLE public.activity_audit
(
    id bigint NOT NULL DEFAULT nextval('activity_audit_id_seq'::regclass),
    "startTime" timestamp without time zone,
    "endTime" timestamp without time zone,
    "activityName" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT activity_audit_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.activity_audit
    OWNER to ian;