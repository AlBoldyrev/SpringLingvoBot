INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (9, 'Привет! Это фраза 1'),
	   (10, 'Привет! Это фраза 2'),
	   (11, 'Привет! Это фраза 3'),
	   (12, 'Привет! Это фраза 4'),
	   (13, 'Привет! Это фраза 5'),
	   (14, 'Привет! Это фраза 6'),
	   (15, 'Привет! Это фраза 7'),
	   (16, 'Привет! Это фраза 8'),
	   (17, 'Привет! Это фраза 9'),
	   (18, 'Привет! Это фраза 10'),
	   (19, 'Привет! Это фраза 11'),
	   (20, 'Привет! Это фраза 12')
	   ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (2, 'Тестовый Диалог №1') ON CONFLICT DO NOTHING;

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


INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (2, 12, 2) ON CONFLICT DO NOTHING;