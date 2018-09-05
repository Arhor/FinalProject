-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema admission_committee
-- -----------------------------------------------------
-- База данных для сопровождения работы приёмной коммиссии.
-- 
-- Пользователь авторизуется в системе используя свой e-mail как логин. Каждому пользователю соответствует одна из двух ролей  (администратор, абитуриент).
-- Абитуриент регистрируется на один из факультетов, каждый из которых имеет фиксированный план набора.
-- Абитруриент вводит название своих страны и города, а также баллы по предметам и аттестату, на основании которых система определяет - принят ли абитуриент или нет.
-- Администратор может менять содержимое таблиц, связанных со списком факультетов и предметов относящихся к ним.
DROP SCHEMA IF EXISTS `admission_committee` ;

-- -----------------------------------------------------
-- Schema admission_committee
--
-- База данных для сопровождения работы приёмной коммиссии.
-- 
-- Пользователь авторизуется в системе используя свой e-mail как логин. Каждому пользователю соответствует одна из двух ролей  (администратор, абитуриент).
-- Абитуриент регистрируется на один из факультетов, каждый из которых имеет фиксированный план набора.
-- Абитруриент вводит название своих страны и города, а также баллы по предметам и аттестату, на основании которых система определяет - принят ли абитуриент или нет.
-- Администратор может менять содержимое таблиц, связанных со списком факультетов и предметов относящихся к ним.
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `admission_committee` DEFAULT CHARACTER SET utf8 ;
USE `admission_committee` ;

-- -----------------------------------------------------
-- Table `admission_committee`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`users` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`users` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'user ID',
  `email` VARCHAR(129) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'e-mail - уникален для каждого пользователя, используется как логин',
  `password` CHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'password',
  `first_name` VARCHAR(35) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'first name',
  `last_name` VARCHAR(35) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'last name',
  `role` ENUM('admin', 'client') CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT 'client' COMMENT 'user role',
  `lang` ENUM('ru', 'en') CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT 'ru' COMMENT 'user language',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_users_UNIQUE` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'users';


-- -----------------------------------------------------
-- Table `admission_committee`.`faculties`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`faculties` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`faculties` (
  `id` TINYINT UNSIGNED NOT NULL COMMENT 'id',
  `name_ru` VARCHAR(60) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'faculty\'s name in russian',
  `name_en` VARCHAR(60) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'faculty\'s name in english',
  `seats_total` TINYINT UNSIGNED NOT NULL COMMENT 'Общее кол-во мест на данном факультете',
  `seats_budget` TINYINT UNSIGNED NOT NULL COMMENT 'Кол-во бесплатных мест на данном факультете',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'faculties';


-- -----------------------------------------------------
-- Table `admission_committee`.`enrollees`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`enrollees` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`enrollees` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'enrollee id',
  `country` VARCHAR(55) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'enrollee country',
  `city` VARCHAR(55) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'enrollee city',
  `school_certificate` TINYINT UNSIGNED NOT NULL COMMENT 'average value of school certificate',
  `is_passed` BIT(1) NULL COMMENT 'admission result (passed or not)',
  `users_id` INT UNSIGNED NOT NULL COMMENT 'users_id',
  `faculties_id` TINYINT UNSIGNED NOT NULL COMMENT 'faculties_id',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `fk_enrollees_users1_idx` (`users_id` ASC),
  INDEX `fk_enrollees_faculties1_idx` (`faculties_id` ASC),
  CONSTRAINT `fk_enrollees_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `admission_committee`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_enrollees_faculties1`
    FOREIGN KEY (`faculties_id`)
    REFERENCES `admission_committee`.`faculties` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'enrollees';


-- -----------------------------------------------------
-- Table `admission_committee`.`subjects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`subjects` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`subjects` (
  `id` TINYINT UNSIGNED NOT NULL COMMENT 'id',
  `name_ru` VARCHAR(25) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'subject\'s name in russian',
  `name_en` VARCHAR(25) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'subject\'s name in english',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'subjects';


-- -----------------------------------------------------
-- Table `admission_committee`.`faculties_has_subjects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`faculties_has_subjects` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`faculties_has_subjects` (
  `faculties_id` TINYINT UNSIGNED NOT NULL COMMENT 'faculties_id',
  `subjects_id` TINYINT UNSIGNED NOT NULL COMMENT 'subjects_id',
  PRIMARY KEY (`faculties_id`, `subjects_id`),
  INDEX `fk_faculties_has_subjects_subjects1_idx` (`subjects_id` ASC),
  INDEX `fk_faculties_has_subjects_faculties1_idx` (`faculties_id` ASC),
  CONSTRAINT `fk_faculties_has_subjects_faculties1`
    FOREIGN KEY (`faculties_id`)
    REFERENCES `admission_committee`.`faculties` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_faculties_has_subjects_subjects1`
    FOREIGN KEY (`subjects_id`)
    REFERENCES `admission_committee`.`subjects` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'К одному факультету относится несколько предметов\nОдин предмет относится к нескольким факультетам';


-- -----------------------------------------------------
-- Table `admission_committee`.`enrollees_has_subjects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`enrollees_has_subjects` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`enrollees_has_subjects` (
  `enrollees_id` INT UNSIGNED NOT NULL COMMENT 'enrollees_id',
  `subjects_id` TINYINT UNSIGNED NOT NULL COMMENT 'subjects_id',
  `score` TINYINT UNSIGNED NOT NULL COMMENT 'оценка студента по соотв. предмету',
  PRIMARY KEY (`enrollees_id`, `subjects_id`),
  INDEX `fk_enrollees_has_subjects_subjects1_idx` (`subjects_id` ASC),
  INDEX `fk_enrollees_has_subjects_enrollees1_idx` (`enrollees_id` ASC),
  CONSTRAINT `fk_enrollees_has_subjects_enrollees1`
    FOREIGN KEY (`enrollees_id`)
    REFERENCES `admission_committee`.`enrollees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_enrollees_has_subjects_subjects1`
    FOREIGN KEY (`subjects_id`)
    REFERENCES `admission_committee`.`subjects` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Один абитуриент сдаёт экзамен по нескольким предметам\nПо одному предмету экзамен сдают несколько абитуриентов\nКаждая пара \'абитуриент-предмет\' характеризуется ценкой по предмету для конкретного абитуриента';

USE `admission_committee` ;

-- -----------------------------------------------------
-- Placeholder table for view `admission_committee`.`admission_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `admission_committee`.`admission_list` (`id` INT, `'Имя'` INT, `'Фамилия'` INT, `'Факультет'` INT, `'Общий балл'` INT, `'Зачислен'` INT);

-- -----------------------------------------------------
-- View `admission_committee`.`admission_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`admission_list`;
DROP VIEW IF EXISTS `admission_committee`.`admission_list` ;
USE `admission_committee`;
CREATE  OR REPLACE VIEW `admission_list` AS
	SELECT `enrollees`.`id`, 
		   `users`.`first_name` AS 'Имя',
           `users`.`last_name` AS 'Фамилия', 
           CASE 
				WHEN users.lang = 'ru' THEN faculties.name_ru 
									   ELSE faculties.name_en 
		   END AS 'Факультет' ,
           (`score` + `enrollees`.`school_certificate`) AS 'Общий балл',
           `enrollees`.`is_passed` AS 'Зачислен'
	FROM `enrollees`
		JOIN `users`     ON `users`.`id` = `enrollees`.`users_id`
		JOIN `faculties` ON `enrollees`.`faculties_id` = `faculties`.`id`
		JOIN (
				SELECT `enrollees_has_subjects`.`enrollees_id` ,sum(`score`) as `score`
				FROM `enrollees_has_subjects`
				GROUP BY `enrollees_has_subjects`.`enrollees_id`
			 ) AS `total_score` ON `enrollees`.`id` = `total_score`.`enrollees_id`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;