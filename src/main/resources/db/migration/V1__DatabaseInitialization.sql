--
-- PostgreSQL database dump
--

-- Dumped from database version 11.2
-- Dumped by pg_dump version 11.2

-- Started on 2020-05-24 17:31:38

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;


CREATE SCHEMA IF NOT EXISTS lingvobot ;

ALTER SCHEMA lingvobot OWNER TO postgres;

CREATE SEQUENCE lingvobot.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS lingvobot.dialogs (
    dialog_id integer NOT NULL,
    dialog_name character varying(255)
);

ALTER TABLE lingvobot.hibernate_sequence OWNER TO postgres;

CREATE TABLE IF NOT EXISTS lingvobot.messages (
    message_id integer NOT NULL,
    message_value character varying(255),
    user_id integer
);


CREATE TABLE IF NOT EXISTS lingvobot.nodes (
    node_id integer NOT NULL,
    node_key integer,
    node_value character varying(255),
    dialog_id integer
);

ALTER TABLE lingvobot.nodes OWNER TO postgres;

CREATE TABLE IF NOT EXISTS lingvobot.nodesnext (
    id integer NOT NULL,
    keyboard_value character varying(255),
    next_node integer,
    node_id integer,
    dialog_id integer
);

CREATE TABLE IF NOT EXISTS lingvobot.phrases (
    phrase_id integer NOT NULL,
    difficulty integer,
    phrase_one_side character varying(255),
    phrase_other_side character varying(255)
);

CREATE TABLE IF NOT EXISTS lingvobot.settings (
    settings_id integer NOT NULL,
    difficulty_level integer,
    is_premium boolean,
    lessons_per_day integer,
    part_of_the_day character varying(255),
    user_pronoun character varying(255)
);

CREATE TABLE IF NOT EXISTS lingvobot.user_dialogs (
    user_dialog_id integer NOT NULL,
    is_cancelled boolean,
    is_finished boolean,
    node_id integer,
    dialog_id integer,
    user_id integer
);

CREATE TABLE IF NOT EXISTS lingvobot.user_phrases (
    user_phrase_id integer NOT NULL,
    is_finished boolean,
    is_one_side boolean,
    phrase_id integer,
    user_id integer
);

CREATE TABLE IF NOT EXISTS lingvobot.users (
    user_id integer NOT NULL,
    level integer,
    page integer,
    user_name character varying(255),
    user_vk_id integer,
    settings_id integer
);

ALTER TABLE ONLY lingvobot.dialogs
    ADD CONSTRAINT dialogs_pkey PRIMARY KEY (dialog_id);


ALTER TABLE ONLY lingvobot.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (message_id);


ALTER TABLE ONLY lingvobot.nodes
    ADD CONSTRAINT nodes_pkey PRIMARY KEY (node_id);

ALTER TABLE ONLY lingvobot.nodesnext
    ADD CONSTRAINT nodesnext_pkey PRIMARY KEY (id);

ALTER TABLE ONLY lingvobot.phrases
    ADD CONSTRAINT phrases_pkey PRIMARY KEY (phrase_id);

ALTER TABLE ONLY lingvobot.settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (settings_id);

ALTER TABLE ONLY lingvobot.dialogs
    ADD CONSTRAINT uk_5w87mq02s9l90gppcsqvxkcl0 UNIQUE (dialog_name);

ALTER TABLE ONLY lingvobot.user_dialogs
    ADD CONSTRAINT user_dialogs_pkey PRIMARY KEY (user_dialog_id);

ALTER TABLE ONLY lingvobot.user_phrases
    ADD CONSTRAINT user_phrases_pkey PRIMARY KEY (user_phrase_id);

ALTER TABLE ONLY lingvobot.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);

ALTER TABLE ONLY lingvobot.user_phrases
    ADD CONSTRAINT fk106wc9waufyqqt2o2ar7h17st FOREIGN KEY (phrase_id) REFERENCES lingvobot.phrases(phrase_id);

ALTER TABLE ONLY lingvobot.user_dialogs
    ADD CONSTRAINT fk1hol1xmkevv6wx89xx0b371rt FOREIGN KEY (user_id) REFERENCES lingvobot.users(user_id);

ALTER TABLE ONLY lingvobot.nodesnext
    ADD CONSTRAINT fk590t2c7s9t2t05hanjy7akinp FOREIGN KEY (dialog_id) REFERENCES lingvobot.dialogs(dialog_id);

ALTER TABLE ONLY lingvobot.nodes
    ADD CONSTRAINT fk59qicwhufec0ue1d58n5ayjle FOREIGN KEY (dialog_id) REFERENCES lingvobot.dialogs(dialog_id);

ALTER TABLE ONLY lingvobot.user_phrases
    ADD CONSTRAINT fk7y8s5jde1wix7rl3ahsr4ew50 FOREIGN KEY (user_id) REFERENCES lingvobot.users(user_id);

ALTER TABLE ONLY lingvobot.users
    ADD CONSTRAINT fkkq8mvg6xlyeewn7xnf0usloiq FOREIGN KEY (settings_id) REFERENCES lingvobot.settings(settings_id);

ALTER TABLE ONLY lingvobot.messages
    ADD CONSTRAINT fkpsmh6clh3csorw43eaodlqvkn FOREIGN KEY (user_id) REFERENCES lingvobot.users(user_id);

ALTER TABLE ONLY lingvobot.user_dialogs
    ADD CONSTRAINT fkqe7ng4gjq7vnmlc4vi18owoeh FOREIGN KEY (dialog_id) REFERENCES lingvobot.dialogs(dialog_id);


