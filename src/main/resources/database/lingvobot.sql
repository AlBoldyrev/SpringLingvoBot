INSERT INTO lingvobot.dialog_phrase(dialog_phrase_id, dialog_phrase_value)
VALUES (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Привет! Совсем скоро я всё расскажу, для начала позволю себе спросить, общаемся на ты или на вы?'),
--Клавиатурка с выбором "Ты", "Вы".
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Отлично!
       Итак, почему мы учим английский в школе десять лет, но выходим без знаний? Заучиваем днями и годами слова и правила, но как только, нам выпадает случай что-то сказать на английском, то ''ээ'', наша первая реакция?
       Ответ на самом деле есть и вот он: невозможно выучить язык без практики и тем более, если не практиковать каждый день. Можно и по пять минут в день, главное, делать это как можно чаще. Поэтому и появился я.
	   Как я работаю, мы совсем скоро узнаем, но для начала скажу, что мы можем учить английский где угодно, дома или по пути на работу или в школу (или на скучной лекции в *ссылка на университет).
       Поскольку я нахожусь на версии *1.02 то мы будем практиковать только письменную речь, но разработчики обещали мне, что дальше — больше. Жду-недождусь этого момента:)
	   Итак, время пришло, для начала давай попробуем!
       Как переводится на английский фраза: Привет, меня зовут Майк, рад с тобой познакомиться!'),
	   --Подождать ответ пользователя
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Конечно, этот пример был несложным. Наш вариант: Hello, I''m Mike, nice to meet you!
       А как переводится: Давай встретимся в семь?'),
	   --Подождать ответ пользователя
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Let''s meet at seven?
       Что вы скажете если предлагаете человеку сесть?'),
	   --Подождать ответ пользователя
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Please, take a seat.
       Что ж, кажется механику вы поняли! Она совсем не сложная, но очень мощная, потому что в основе её лежит регулярная практика целых фраз, потому что практикуясь в этом, вам будет гораздо лечге и говорить и писать на английском!
	   На данный момент, во мне есть три основных типа сложности, вы всегда сможете их изменить но попробуйте выбрать какой-нибудь.'),
	   --Клавиатурка с выбором уровня сложности
	   --(nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Отлично! Нам потребуется не более пяти минут на первое занятие.'),
	   --(nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Это было здорово!'),
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'И последнее, я буду напоминать тебе о занятиях так часто как скажешь, пожалуйста выбери частоту наших занятий:'),
	   --Клавиатурка с количеством занятий в день (до 5)
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'И желаемое время! Я могу писать как в течение дня, так и утром или вечером в какое-то время.'),
	   --Клавиатурка с выбором времени 'Утром', 'Вечером', 'Днём'
	   (nextval('lingvobot.lingvobot_dialog_phrase_sequence'), 'Ну вот и всё! Увидимся завтра:)');

INSERT INTO lingvobot.dialog_state(dialog_state_id, state, dialog_id, dialog_phrase_id) VALUES
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 1, 1, 1),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 2, 1, 2),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 3, 1, 3),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 4, 1, 4),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 5, 1, 5),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 6, 1, 6),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 7, 1, 7),
	(nextval('lingvobot.lingvobot_dialog_state_sequence'), 8, 1, 8);


