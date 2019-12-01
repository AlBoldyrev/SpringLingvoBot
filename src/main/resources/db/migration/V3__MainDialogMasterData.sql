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

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (33, 'Привет! Это диалог 3 фраза 1'),
   (34, 'Привет! Это диалог 3 фраза 2'),
   (35, 'Привет! Это диалог 3 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (36, 'Привет! Это диалог 4 фраза 1'),
   (37, 'Привет! Это диалог 4 фраза 2'),
   (38, 'Привет! Это диалог 4 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (39, 'Привет! Это диалог 5 фраза 1'),
	   (40, 'Привет! Это диалог 5 фраза 2'),
	   (41, 'Привет! Это диалог 5 фраза 3')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (42, 'Привет! Это диалог 6 фраза 1'),
   (43, 'Привет! Это диалог 6 фраза 2'),
   (44, 'Привет! Это диалог 6 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (45, 'Привет! Это диалог 7 фраза 1'),
	   (46, 'Привет! Это диалог 7 фраза 2'),
	   (47, 'Привет! Это диалог 7 фраза 3')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (48, 'Привет! Это диалог 8 фраза 1'),
   (49, 'Привет! Это диалог 8 фраза 2'),
   (50, 'Привет! Это диалог 8 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (51, 'Привет! Это диалог 9 фраза 1'),
	   (52, 'Привет! Это диалог 9 фраза 2'),
	   (53, 'Привет! Это диалог 9 фраза 3')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (2, 'Тестовый Диалог №1') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (3, 'Тестовый Диалог №2') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (4, 'Тестовый Диалог №3') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (5, 'Тестовый Диалог №4') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (6, 'Тестовый Диалог №5') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (7, 'Тестовый Диалог №6') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (8, 'Тестовый Диалог №7') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (9, 'Тестовый Диалог №8') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (10, 'Тестовый Диалог №9') ON CONFLICT DO NOTHING;

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

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 4, 33) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 4, 34) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 4, 35) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 5, 36) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 5, 37) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 5, 38) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 6, 39) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 6, 40) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 6, 41) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 7, 42) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 7, 43) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 7, 44) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 8, 45) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 8, 46) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 8, 47) ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 9, 48) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 9, 49) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 9, 50) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 10, 51) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 10, 52) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 10, 53) ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (2, 12, 2) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (3, 12, 3) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (4, 3, 4) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (5, 3, 5) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (6, 3, 6) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (7, 3, 7) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (8, 3, 8) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (9, 3, 9) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (10, 3, 10) ON CONFLICT DO NOTHING;