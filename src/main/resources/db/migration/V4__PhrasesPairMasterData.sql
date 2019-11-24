INSERT INTO lingvobot.dialogs(dialog_id, dialog_name)
VALUES (4, 'Фразы')
ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.phrase_pairs(phrase_pair_id, phrase_answer, phrase_question)
VALUES (1, 'Hello', 'Привет'),
       (2, 'By', 'Пока'),
       (2, 'You are welcome', 'Пожалуйста'),
       (2, 'Thank you', 'Спасибо')
ON CONFLICT DO NOTHING;


CREATE TABLE IF NOT EXISTS lingvobot.phrase_pair_state
(
  phrase_pair_state_id integer NOT NULL,
  phrase_pair_id       integer,
  user_id              integer,
  PRIMARY KEY (phrase_pair_state_id),
  FOREIGN KEY (phrase_pair_id) REFERENCES lingvobot.phrase_pairs (phrase_pair_id),
  FOREIGN KEY (user_id) REFERENCES lingvobot.users (user_id)
);




