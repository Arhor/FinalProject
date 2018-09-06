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
  `password` CHAR(128) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'password',
  `first_name` VARCHAR(35) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'first name',
  `last_name` VARCHAR(35) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'last name',
  `role` ENUM('admin', 'client') CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT 'client' COMMENT 'user role',
  `lang` ENUM('ru', 'en') CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT 'ru' COMMENT 'user language',
  `available` BIT(1) NOT NULL DEFAULT 1,
  UNIQUE INDEX `email_users_UNIQUE` (`email` ASC),
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'users';


-- -----------------------------------------------------
-- Table `admission_committee`.`enrollees`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`enrollees` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`enrollees` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'enrollee id',
  `country` VARCHAR(55) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'enrollee country',
  `city` VARCHAR(55) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT 'enrollee city',
  `school_certificate` TINYINT UNSIGNED NOT NULL COMMENT 'average value of school certificate',
  `users_id` INT UNSIGNED NOT NULL COMMENT 'users_id',
  `available` BIT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `fk_enrollees_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_enrollees_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `admission_committee`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'enrollees';


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
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_faculties_has_subjects_subjects1`
    FOREIGN KEY (`subjects_id`)
    REFERENCES `admission_committee`.`subjects` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
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
  `available` BIT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`enrollees_id`, `subjects_id`),
  INDEX `fk_enrollees_has_subjects_subjects1_idx` (`subjects_id` ASC),
  INDEX `fk_enrollees_has_subjects_enrollees1_idx` (`enrollees_id` ASC),
  CONSTRAINT `fk_enrollees_has_subjects_enrollees1`
    FOREIGN KEY (`enrollees_id`)
    REFERENCES `admission_committee`.`enrollees` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_enrollees_has_subjects_subjects1`
    FOREIGN KEY (`subjects_id`)
    REFERENCES `admission_committee`.`subjects` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Один абитуриент сдаёт экзамен по нескольким предметам\nПо одному предмету экзамен сдают несколько абитуриентов\nКаждая пара \'абитуриент-предмет\' характеризуется ценкой по предмету для конкретного абитуриента';


-- -----------------------------------------------------
-- Table `admission_committee`.`admission_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `admission_committee`.`admission_list` ;

CREATE TABLE IF NOT EXISTS `admission_committee`.`admission_list` (
  `enrollees_id` INT UNSIGNED NOT NULL,
  `faculties_id` TINYINT UNSIGNED NOT NULL,
  `is_passed` BIT(1) NOT NULL DEFAULT 0,
  `available` BIT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`enrollees_id`, `faculties_id`),
  INDEX `fk_enrollees_has_faculties_faculties1_idx` (`faculties_id` ASC),
  INDEX `fk_enrollees_has_faculties_enrollees1_idx` (`enrollees_id` ASC),
  CONSTRAINT `fk_enrollees_has_faculties_enrollees1`
    FOREIGN KEY (`enrollees_id`)
    REFERENCES `admission_committee`.`enrollees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_enrollees_has_faculties_faculties1`
    FOREIGN KEY (`faculties_id`)
    REFERENCES `admission_committee`.`faculties` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `admission_committee`.`faculties`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (201, 'Факультет компьютерного проектирования', 'Faculty of computer-aided design', 113, 20);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (202, 'Факультет информационных технологий и управления', 'Faculty of information technologies and control', 170, 15);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (203, 'Факультет радиотехники и электроники', 'Faculty of radioengineering and electronics', 149, 17);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (204, 'Факультет компьютерных систем и сетей', 'Faculty of computer systems and networks', 250, 10);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (205, 'Факультет инфокоммуникаций', 'Faculty of infocommunications', 97, 10);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (206, 'Инженерно-экономический факультет', 'Faculty of engineering and economics', 180, 30);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (207, 'Факультет инновационного непрерывного образования', 'Faculty of innovative lifelong learning', 50, 20);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_total`, `seats_budget`) VALUES (208, 'Военный факультет', 'Military faculty', 123, 50);

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`subjects`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (101, 'Русский язык', 'Russian language');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (102, 'Физика', 'Physics');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (103, 'Математика', 'Math');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (104, 'Химия', 'Chemistry');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (105, 'Биология', 'Biology');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (106, 'Иностранный язык', 'Foreign language');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (107, 'Всемирная история', 'The world history');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (108, 'Обществоведение', 'Social studies');
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`) VALUES (109, 'География', 'Geography');

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`faculties_has_subjects`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (201, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (201, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (201, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (202, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (202, 106);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (202, 108);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (203, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (203, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (203, 104);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (204, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (204, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (204, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (205, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (205, 106);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (205, 107);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (206, 106);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (206, 108);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (206, 109);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (207, 105);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (207, 106);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (207, 107);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (208, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (208, 104);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (208, 109);

COMMIT;

