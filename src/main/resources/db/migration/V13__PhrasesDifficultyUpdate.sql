ALTER TABLE lingvobot.phrase_pairs
  ADD difficulty SMALLINT;

ALTER SEQUENCE lingvobot.lingvobot_phrase_pair_sequence RESTART WITH 3;

INSERT INTO lingvobot.phrase_pairs(phrase_pair_id, phrase_answer, phrase_question, difficulty)
VALUES
(nextval('lingvobot.lingvobot_phrase_pair_sequence'), 'Чем занимался все это время?', 'What have you been up to?', 2),
(nextval('lingvobot.lingvobot_phrase_pair_sequence'), 'Наоборот', 'On the contrary', 3),
(nextval('lingvobot.lingvobot_phrase_pair_sequence'), 'Как я уже говорил','As I said before', 2),
(nextval('lingvobot.lingvobot_phrase_pair_sequence'), 'Кстати','By the way', 2),
(nextval('lingvobot.lingvobot_phrase_pair_sequence'), 'Не говоря уже о','Not to mention', 3);

update lingvobot.phrase_pairs
set difficulty = 1
where phrase_pair_id in(1,2);