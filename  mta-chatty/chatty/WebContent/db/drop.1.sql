-- ---------DROP IF EXISTS STATEMENTS ----------------
DROP INDEX IF EXISTS idx_fk_country_id;
DROP INDEX IF EXISTS idx_fk_city_id;
DROP INDEX IF EXISTS idx_fk_address_id;
DROP INDEX IF EXISTS idx_user_name;
DROP INDEX IF EXISTS fk_buddy_list_user2_idx;
DROP INDEX IF EXISTS fk_buddy_message_receiver_idx;
DROP INDEX IF EXISTS fk_buddy_message_sender_idx;
DROP INDEX IF EXISTS fk_user_has_group_group1_idx;
DROP INDEX IF EXISTS fk_user_has_group_user1_idx;
DROP INDEX IF EXISTS fk_group_message_sender_idx;
DROP INDEX IF EXISTS fk_group_message_receiver_idx;

DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS 'user';
DROP TABLE IF EXISTS buddy_list;
DROP TABLE IF EXISTS 'group';
DROP TABLE IF EXISTS buddy_message;
DROP TABLE IF EXISTS group_membership;
DROP TABLE IF EXISTS group_message;
