CREATE  TABLE IF NOT EXISTS country (
  country_id INTEGER NOT NULL,
  country TEXT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (country_id))
 DEFAULT CHARACTER SET = utf8;


CREATE  TABLE IF NOT EXISTS city (
  city_id INTEGER NOT NULL PRIMARY KEY,
  city TEXT NOT NULL ,
  country_id INTEGER NOT NULL ,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  INDEX idx_fk_country_id  (country_id ASC),
  CONSTRAINT fk_city_country
    FOREIGN KEY (country_id)
    REFERENCES country (country_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
 DEFAULT CHARACTER SET = utf8;


CREATE  TABLE IF NOT EXISTS `user` (
  email VARCHAR(250)  NOT NULL PRIMARY KEY,
  name VARCHAR(250) NOT NULL ,
  picture VARCHAR(250) NOT NULL DEFAULT 'images/defaultUser.jpg',
  active BOOLEAN NOT NULL DEFAULT TRUE ,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  password VARCHAR(250) NOT NULL,
  INDEX idx_user_name (name))
 DEFAULT CHARACTER SET = utf8;

 
CREATE  TABLE IF NOT EXISTS address (
  address_id VARCHAR(250) NOT NULL PRIMARY KEY ,
  address TEXT NOT NULL ,
  city_id INTEGER NOT NULL ,
  postal_code TEXT NULL DEFAULT NULL ,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
 INDEX idx_fk_city_id (city_id),
 INDEX idx_fk_address_id (address_id),
  CONSTRAINT fk_address_city
    FOREIGN KEY (city_id)
    REFERENCES city (city_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_address_user
    FOREIGN KEY (address_id)
    REFERENCES `user` (email)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
 DEFAULT CHARACTER SET = utf8;
    
    
CREATE  TABLE IF NOT EXISTS buddy_list (
  buddy_id VARCHAR(250) NOT NULL ,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  owner_email VARCHAR(250) NOT NULL,
  PRIMARY KEY (owner_email, buddy_id),
  INDEX fk_buddy_list_user2_idx (buddy_id ASC),
  CONSTRAINT fk_buddy_list_user1
    FOREIGN KEY (owner_email )
    REFERENCES `user` (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_buddy_list_user2
    FOREIGN KEY (buddy_id)
    REFERENCES `user` (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
   DEFAULT CHARACTER SET = utf8;

 
 CREATE  TABLE IF NOT EXISTS `group` (
  group_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT ,
  name TEXT NOT NULL ,
  picture VARCHAR(250) NOT NULL DEFAULT 'images/defaultGroup.jpg',
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  description TEXT NULL)
 DEFAULT CHARACTER SET = utf8;
 
 
CREATE  TABLE IF NOT EXISTS buddy_message (
  send_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  sender_id VARCHAR(250) NOT NULL ,
  receiver_id VARCHAR(250) NOT NULL ,
  message TEXT NOT NULL ,
  is_attachment_path BOOLEAN NOT NULL DEFAULT FALSE ,
  PRIMARY KEY (sender_id, receiver_id),
 INDEX fk_buddy_message_receiver_idx (receiver_id ASC),
 INDEX fk_buddy_message_sender_idx (sender_id ASC),
  CONSTRAINT fk_buddy_message_sender
    FOREIGN KEY (sender_id)
    REFERENCES `user` (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_buddy_message_receiver
    FOREIGN KEY (receiver_id)
    REFERENCES `user` (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
   DEFAULT CHARACTER SET = utf8;

 
 CREATE  TABLE IF NOT EXISTS group_membership (
  member_email VARCHAR(250) NOT NULL ,
  group_id INTEGER NOT NULL,
  PRIMARY KEY (member_email, group_id) ,
 INDEX fk_user_has_group_group1_idx (group_id ASC),
 INDEX fk_user_has_group_user1_idx (member_email ASC),
  CONSTRAINT fk_group_membership_email
    FOREIGN KEY (member_email )
    REFERENCES `user` (email )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_group_membership_group
    FOREIGN KEY (group_id )
    REFERENCES `group` (group_id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
   DEFAULT CHARACTER SET = utf8;


CREATE  TABLE IF NOT EXISTS group_message (
  send_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  sender_id VARCHAR(250) NOT NULL ,
  receiver_id INTEGER NOT NULL ,
  message TEXT NOT NULL ,
  is_attachment_path BOOLEAN NOT NULL DEFAULT FALSE ,
  PRIMARY KEY (send_date, sender_id, receiver_id) ,
 INDEX fk_group_message_sender_idx (sender_id ASC),
 INDEX fk_group_message_receiver_idx (receiver_id ASC),
  CONSTRAINT fk_group_message_sender
    FOREIGN KEY (sender_id )
    REFERENCES group_membership (member_email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_group_message_receiver
    FOREIGN KEY (receiver_id )
    REFERENCES group_membership (group_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
   DEFAULT CHARACTER SET = utf8;

 
 
 
 
 