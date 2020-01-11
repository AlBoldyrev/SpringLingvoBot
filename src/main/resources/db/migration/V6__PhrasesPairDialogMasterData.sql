INSERT INTO lingvobot.dialogs(dialog_id, dialog_name)
VALUES (11, 'Фразы')
ON CONFLICT DO NOTHING;

CREATE SEQUENCE IF NOT EXISTS lingvobot.lingvobot_phrase_pair_state_sequence START 1;