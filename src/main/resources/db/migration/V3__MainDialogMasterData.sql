INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (9, 'Привет! Это диалог 1 фраза 1'),
	   (10, 'Привет! Это диалог 1 фраза 2'),
	   (11, 'Привет! Это диалог 1 фраза 3'),
	   (12, 'Привет! Это диалог 1 фраза 4'),
	   (13, 'Привет! Это диалог 1 фраза 5'),
	   (14, 'Привет! Это диалог 1 фраза 6'),
	   (15, 'Привет! Это диалог 1 фраза 7'),
	   (16, 'Привет! Это диалог 1 фраза 8'),
	   (17, 'Привет! Это диалог 1 фраза 9'),
	   (18, 'Привет! Это диалог 1 фраза 10'),
	   (19, 'Привет! Это диалог 1 фраза 11'),
	   (20, 'Привет! Это диалог 1 фраза 12')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (21, 'Привет! Это диалог 2 фраза 1'),
   (22, 'Привет! Это диалог 2 фраза 2'),
   (23, 'Привет! Это диалог 2 фраза 3'),
   (24, 'Привет! Это диалог 2 фраза 4'),
   (25, 'Привет! Это диалог 2 фраза 5'),
   (26, 'Привет! Это диалог 2 фраза 6'),
   (27, 'Привет! Это диалог 2 фраза 7'),
   (28, 'Привет! Это диалог 2 фраза 8'),
   (29, 'Привет! Это диалог 2 фраза 9'),
   (30, 'Привет! Это диалог 2 фраза 10'),
   (31, 'Привет! Это диалог 2 фраза 11'),
   (32, 'Привет! Это диалог 2 фраза 12')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (2, 'Тестовый Диалог №1') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (3, 'Тестовый Диалог №2') ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 2, 9) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 2, 10) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 2, 11) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 4, 2, 12) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 5, 2, 13) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 6, 2, 14) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 7, 2, 15) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 8, 2, 16) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 9, 2, 17) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 10, 2, 18) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 11, 2, 19) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 12, 2, 20) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 3, 21) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 3, 22) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 3, 23) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 4, 3, 24) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 5, 3, 25) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 6, 3, 26) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 7, 3, 27) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 8, 3, 28) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 9, 3, 29) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 10, 3, 30) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 11, 3, 31) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 12, 3, 32) ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (2, 12, 2) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (3, 12, 3) ON CONFLICT DO NOTHING;