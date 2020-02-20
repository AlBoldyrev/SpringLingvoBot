CREATE TABLE IF NOT EXISTS lingvobot.subdialogs (
    subdialog_id integer NOT NULL,
    PRIMARY KEY (subdialog_id)
);

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_subdialog_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

ALTER TABLE lingvobot.subdialogs OWNER TO postgres;
ALTER TABLE lingvobot.lingvobot_subdialog_sequence OWNER TO postgres;