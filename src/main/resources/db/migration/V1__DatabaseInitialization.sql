CREATE SCHEMA IF NOT EXISTS lingvobot;

ALTER SCHEMA lingvobot OWNER TO postgres;

SET default_tablespace = '';
SET default_with_oids = false;

------------------------------------------------------------------------------------------------------------------------


DROP TYPE IF EXISTS lingvobot.menu_level CASCADE ;
CREATE TYPE lingvobot.menu_level AS ENUM (
    'MAIN',
    'DIALOG',
    'PHRASE'
);

CREATE TABLE IF NOT EXISTS lingvobot.dialogs (
    dialog_id integer NOT NULL,
    dialog_name text,
    PRIMARY KEY (dialog_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.settings (
    settings_id integer NOT NULL,
    difficulty_level integer,
    is_premium boolean,
    lessons_per_day integer,
    part_of_the_day text,
    user_pronoun text,
    PRIMARY KEY (settings_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.dialog_max_state (
    dialog_max_state_id integer NOT NULL,
    dialog_max_state_value integer,
    dialog_id integer,
    PRIMARY KEY (dialog_max_state_id),
    FOREIGN KEY (dialog_id) REFERENCES lingvobot.dialogs(dialog_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.dialog_phrase (
    dialog_phrase_id integer NOT NULL,
    dialog_phrase_value text,
    PRIMARY KEY (dialog_phrase_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.dialog_state (
    dialog_state_id integer NOT NULL,
    state integer,
    dialog_id integer,
    dialog_phrase_id integer,
    PRIMARY KEY (dialog_state_id),
    FOREIGN KEY (dialog_phrase_id) REFERENCES lingvobot.dialog_phrase(dialog_phrase_id),
    FOREIGN KEY (dialog_id) REFERENCES lingvobot.dialogs(dialog_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.users (
    user_id integer NOT NULL,
    user_name text,
    user_vk_id integer,
    settings_id integer,
    PRIMARY KEY (user_id),
    FOREIGN KEY (settings_id) REFERENCES lingvobot.settings(settings_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.messages (
    message_id integer NOT NULL,
    message_value text,
    user_id integer,
    PRIMARY KEY (message_id),
    FOREIGN KEY (user_id) REFERENCES lingvobot.users(user_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.phrase_pairs (
    phrase_pair_id integer NOT NULL,
    phrase_answer text,
    phrase_question text,
    PRIMARY KEY (phrase_pair_id)
);

CREATE TABLE IF NOT EXISTS lingvobot.user_dialogs (
    user_dialog_id integer NOT NULL,
    is_cancelled boolean,
    is_finished boolean,
    state integer,
    dialog_id integer,
    user_id integer,
    PRIMARY KEY (user_dialog_id),
    FOREIGN KEY (user_id) REFERENCES lingvobot.users(user_id),
    FOREIGN KEY (dialog_id) REFERENCES lingvobot.dialogs(dialog_id)
);



CREATE TABLE IF NOT EXISTS lingvobot.menu_stage (
    id integer NOT NULL,
    user_id integer,
    current_dialog_page integer,
    menu_level lingvobot.menu_level,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES lingvobot.users(user_id)
);

------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_dialog_max_state_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_dialog_phrase_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_dialog_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_dialog_state_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_message_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_phrase_pair_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_settings_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_user_dialog_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_user_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_menustage_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

------------------------------------------------------------------------------------------------------------------------

ALTER TABLE lingvobot.dialogs OWNER TO postgres;
ALTER TABLE lingvobot.dialog_state OWNER TO postgres;
ALTER TABLE lingvobot.dialog_phrase OWNER TO postgres;
ALTER TABLE lingvobot.dialog_max_state OWNER TO postgres;
ALTER TABLE lingvobot.messages OWNER TO postgres;
ALTER TABLE lingvobot.phrase_pairs OWNER TO postgres;
ALTER TABLE lingvobot.settings OWNER TO postgres;
ALTER TABLE lingvobot.user_dialogs OWNER TO postgres;
ALTER TABLE lingvobot.users OWNER TO postgres;
ALTER TABLE lingvobot.menu_stage OWNER TO postgres;

ALTER TABLE lingvobot.lingvobot_dialog_max_state_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_dialog_phrase_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_dialog_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_dialog_state_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_message_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_phrase_pair_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_settings_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_user_dialog_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_user_sequence OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_menustage_sequence OWNER TO postgres;

------------------------------------------------------------------------------------------------------------------------