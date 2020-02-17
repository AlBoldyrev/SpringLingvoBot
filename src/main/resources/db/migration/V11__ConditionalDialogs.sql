
DROP TYPE IF EXISTS lingvobot.item_state_type CASCADE;

CREATE TYPE lingvobot.item_state_type AS ENUM (
    'PHRASE', 'SUBDIALOG'
);


ALTER TABLE IF EXISTS lingvobot.dialog_state ADD COLUMN IF NOT EXISTS item_state_type lingvobot.item_state_type;

ALTER TABLE IF EXISTS lingvobot.user_dialogs ADD COLUMN IF NOT EXISTS is_main_branch_switched_to_subdialog boolean;