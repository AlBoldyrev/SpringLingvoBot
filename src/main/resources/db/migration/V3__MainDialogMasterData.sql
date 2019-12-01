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
VALUES (46, 'Привет! Это диалог 4 фраза 1'),
   (47, 'Привет! Это диалог 4 фраза 2'),
   (48, 'Привет! Это диалог 4 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (49, 'Привет! Это диалог 5 фраза 1'),
	   (50, 'Привет! Это диалог 5 фраза 2'),
	   (51, 'Привет! Это диалог 5 фраза 3')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (52, 'Привет! Это диалог 6 фраза 1'),
   (53, 'Привет! Это диалог 6 фраза 2'),
   (54, 'Привет! Это диалог 6 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (55, 'Привет! Это диалог 7 фраза 1'),
	   (56, 'Привет! Это диалог 7 фраза 2'),
	   (57, 'Привет! Это диалог 7 фраза 3')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (58, 'Привет! Это диалог 8 фраза 1'),
   (59, 'Привет! Это диалог 8 фраза 2'),
   (60, 'Привет! Это диалог 8 фраза 3')
   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (61, 'Привет! Это диалог 9 фраза 1'),
	   (62, 'Привет! Это диалог 9 фраза 2'),
	   (63, 'Привет! Это диалог 9 фраза 3')
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

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 4, 46) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 4, 47) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 4, 48) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 5, 49) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 5, 50) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 5, 51) ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 6, 52) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 6, 53) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 6, 54) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 7, 55) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 7, 56) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 7, 57) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 8, 58) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 8, 59) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 8, 60) ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 9, 61) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 9, 62) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 9, 63) ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (2, 12, 2) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (3, 12, 3) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (4, 3, 4) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (5, 3, 5) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (6, 3, 6) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (7, 3, 4) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (8, 3, 5) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (9, 3, 6) ON CONFLICT DO NOTHING;