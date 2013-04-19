-- -----------------------------------------------------
-- Table country
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS country (
  country_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  country TEXT NOT NULL ,
  last_update TIMESTAMP NOT NULL default current_timestamp);


CREATE  TABLE IF NOT EXISTS city (
  `city_id` INTEGER NOT NULL PRIMARY KEYAUTOINCREMENT ,
  `city` VARCHAR(50) NOT NULL ,
  `country_id` SMALLINT UNSIGNED NOT NULL ,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  INDEX `idx_fk_country_id` (`country_id` ASC) ,
  CONSTRAINT `fk_city_country`
    FOREIGN KEY (`country_id` )
    REFERENCES `sakila`.`country` (`country_id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
  