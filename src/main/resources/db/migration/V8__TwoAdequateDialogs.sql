INSERT INTO lingvobot.dialogs(dialog_id, dialog_name) VALUES (12, 'Harry Potter') ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value, attach) VALUES (54,

'-Are the rumors true, Albus?
- I''m afraid so, professor.The good and the bad.
- And the boy?
- Hagrid is bringing him.
- Is it wise to trust Hagrid with something so important?
- Professor, I would trust Hagrid with my life.',

'video-100714365_456239054') ON CONFLICT DO NOTHING;


INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value, attach) VALUES (55,

'-Has anyone seen a toad?
- A boy named Neville''s lost one.
- No.
-Oh, are you doing sweet tricks? Let''s see, then.
-Sunshine, daisies, butter mellow
-Turn this stupid, fat rat yellow
-Are you sure that''s a real sweet trick? Well, it''s not very good, is it?
-I''ve only tried a few simple ones myself...but they''ve all worked for me.
-For example:
-Oculus Reparo.
-That''s better, isn''t it?
-Holy cricket, you''re Harry Potter!
-I''m Hermione Granger. And you are...?
- I''m Ron Weasley.
- Pleasure.
-You two better change into robes.I expect we''ll be arriving soon.
-You''ve got dirt on your nose. Did you know?
-Just there.',

'video-170362981_456239020') ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 12, 54) ON CONFLICT DO NOTHING;
INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id)	VALUES (nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 12, 55) ON CONFLICT DO NOTHING;

INSERT INTO lingvobot.dialog_max_state(dialog_max_state_id, dialog_max_state_value, dialog_id) VALUES (11, 2, 12) ON CONFLICT DO NOTHING;