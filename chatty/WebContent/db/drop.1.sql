-- ---------DROP STATEMENTS ----------------
DROP INDEX idx_fk_country_id;
DROP INDEX idx_fk_city_id;
DROP INDEX idx_fk_address_id;
DROP INDEX idx_last_name;
DROP INDEX fk_buddy_list_user2_idx;
DROP INDEX fk_buddy_message_receiver_idx;
DROP INDEX fk_buddy_message_sender_idx;
DROP INDEX fk_user_has_group_group1_idx;
DROP INDEX fk_user_has_group_user1_idx;
DROP INDEX fk_group_message_sender_idx;
DROP INDEX fk_group_message_receiver_idx;

DROP TABLE country;
DROP TABLE city;
DROP TABLE address;
DROP TABLE 'user';
DROP TABLE buddy_list;
DROP TABLE 'group';
DROP TABLE buddy_message;
DROP TABLE group_membership;
DROP TABLE group_message;


