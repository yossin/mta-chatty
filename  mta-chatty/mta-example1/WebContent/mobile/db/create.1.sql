CREATE  TABLE IF NOT EXISTS country (
  country_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  country TEXT NOT NULL ,
  last_update TIMESTAMP NOT NULL default current_timestamp);


CREATE  TABLE IF NOT EXISTS city (
  city_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,
  city TEXT NOT NULL ,
  country_id INTEGER NOT NULL ,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  CONSTRAINT fk_city_country
    FOREIGN KEY (country_id)
    REFERENCES country (country_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
    
CREATE  INDEX idx_fk_country_id ON city (country_id);


CREATE  TABLE IF NOT EXISTS address (
  address_id TEXT NOT NULL PRIMARY KEY ,
  address TEXT NOT NULL ,
  city_id INTEGER NOT NULL ,
  postal_code TEXT NULL DEFAULT NULL ,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  CONSTRAINT fk_address_city
    FOREIGN KEY (city_id)
    REFERENCES city (city_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_address_user
    FOREIGN KEY (address_id)
    REFERENCES 'user' (email)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE INDEX idx_fk_city_id on address (city_id);
CREATE INDEX idx_fk_address_id on address (address_id);



CREATE  TABLE IF NOT EXISTS 'user' (
  email TEXT  NOT NULL PRIMARY KEY,
  name TEXT NOT NULL ,
  picture TEXT NOT NULL DEFAULT 'images/defaultUser.jpg',
  active BOOLEAN NOT NULL DEFAULT TRUE ,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  password TEXT NOT NULL);

  CREATE INDEX idx_last_name ON 'user' (picture);
  
  
  
CREATE  TABLE IF NOT EXISTS buddy_list (
  buddy_id TEXT NOT NULL ,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  owner_email TEXT NOT NULL,
  PRIMARY KEY (owner_email, buddy_id) ,
  CONSTRAINT fk_buddy_list_user1
    FOREIGN KEY (owner_email )
    REFERENCES 'user' (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_buddy_list_user2
    FOREIGN KEY (buddy_id)
    REFERENCES 'user' (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
  
 CREATE INDEX fk_buddy_list_user2_idx ON buddy_list(buddy_id ASC);
 
 
 CREATE  TABLE IF NOT EXISTS 'group' (
  group_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,
  name TEXT NOT NULL ,
  picture TEXT NOT NULL DEFAULT 'images/defaultGroup.jpg',
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  description TEXT NULL);

  
CREATE  TABLE IF NOT EXISTS buddy_message (
  send_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  sender_id TEXT NOT NULL ,
  receiver_id TEXT NOT NULL ,
  message TEXT NOT NULL ,
  is_attachment_path BOOLEAN NOT NULL DEFAULT FALSE ,
  PRIMARY KEY (send_date, receiver_id, sender_id) ,
  CONSTRAINT fk_buddy_message_sender
    FOREIGN KEY (sender_id)
    REFERENCES 'user' (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_buddy_message_receiver
    FOREIGN KEY (receiver_id)
    REFERENCES 'user' (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
  
CREATE INDEX fk_buddy_message_receiver_idx ON buddy_message(receiver_id ASC);
CREATE INDEX fk_buddy_message_sender_idx ON buddy_message(sender_id ASC);


CREATE  TABLE IF NOT EXISTS group_membership (
  member_email TEXT NOT NULL ,
  group_id BOOLEAN UNSIGNED NOT NULL ,
  PRIMARY KEY (member_email, group_id) ,
  CONSTRAINT fk_group_membership_email
    FOREIGN KEY (member_email )
    REFERENCES 'user' (email )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_group_membership_group
    FOREIGN KEY (group_id )
    REFERENCES 'group' (group_id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE INDEX fk_user_has_group_group1_idx ON group_membership(group_id ASC);
CREATE INDEX fk_user_has_group_user1_idx ON group_membership(member_email ASC);


CREATE  TABLE IF NOT EXISTS group_message (
  send_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  sender_id TEXT NOT NULL ,
  receiver_id INTEGER NOT NULL ,
  message TEXT NOT NULL ,
  attachment_path TEXT NULL ,
  PRIMARY KEY (send_date, sender_id, receiver_id) ,
  CONSTRAINT fk_group_message_sender
    FOREIGN KEY (sender_id )
    REFERENCES group_membership (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_group_message_receiver
    FOREIGN KEY (receiver_id )
    REFERENCES group_membership (group_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_group_message_sender_idx ON group_message(sender_id ASC);
CREATE INDEX fk_group_message_receiver_idx ON group_message(receiver_id ASC);




