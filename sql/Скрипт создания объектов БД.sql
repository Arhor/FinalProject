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
  `seats_paid` TINYINT UNSIGNED NOT NULL COMMENT 'Общее кол-во мест на данном факультете',
  `seats_budget` TINYINT UNSIGNED NOT NULL COMMENT 'Кол-во бесплатных мест на данном факультете',
  `available` BIT(1) NOT NULL DEFAULT 1,
  `checked` BIT(1) NOT NULL DEFAULT 0,
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
  `available` BIT(1) NOT NULL DEFAULT 1,
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
  `is_passed` ENUM('budget', 'paid', 'none') NOT NULL DEFAULT 'none',
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
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (201, 'Факультет компьютерного проектирования', 'Faculty of computer-aided design', 5, 3, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (202, 'Факультет информационных технологий и управления', 'Faculty of information technologies and control', 4, 2, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (203, 'Факультет радиотехники и электроники', 'Faculty of radioengineering and electronics', 2, 4, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (204, 'Факультет компьютерных систем и сетей', 'Faculty of computer systems and networks', 3, 3, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (205, 'Факультет инфокоммуникаций', 'Faculty of infocommunications', 4, 2, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (206, 'Инженерно-экономический факультет', 'Faculty of engineering and economics', 3, 4, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (207, 'Факультет инновационного непрерывного образования', 'Faculty of innovative lifelong learning', 5, 2, DEFAULT, DEFAULT);
INSERT INTO `admission_committee`.`faculties` (`id`, `name_ru`, `name_en`, `seats_paid`, `seats_budget`, `available`, `checked`) VALUES (208, 'Военный факультет', 'Military faculty', 5, 4, DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`subjects`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`, `available`) VALUES (101, 'Русский язык', 'Russian language', DEFAULT);
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`, `available`) VALUES (102, 'Физика', 'Physics', DEFAULT);
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`, `available`) VALUES (103, 'Математика', 'Math', DEFAULT);
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`, `available`) VALUES (104, 'Химия', 'Chemistry', DEFAULT);
INSERT INTO `admission_committee`.`subjects` (`id`, `name_ru`, `name_en`, `available`) VALUES (105, 'Иностранный язык', 'Foreign language', DEFAULT);

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
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (202, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (202, 104);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (203, 105);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (203, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (203, 104);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (204, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (204, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (204, 104);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (205, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (205, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (205, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (206, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (206, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (206, 104);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (207, 102);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (207, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (207, 105);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (208, 101);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (208, 103);
INSERT INTO `admission_committee`.`faculties_has_subjects` (`faculties_id`, `subjects_id`) VALUES (208, 105);

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.1@gmail.com','BDE16E66E0841AA50EF17532DE834F053CA8C48BC02C147364CD644CCD98878065221341AA16339E86762AE0928DE08FB9BC3AA18F20240F3ADD276DFBB8C4CD','Leo','Brown','admin','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.2@gmail.com','48D12CE112DD725185FE8D99B1C377BDE4D2A44985279816A5D646E24D6992D20D37961B188C6D1D985AD564ABAA466D4934488F6981BCF886D5A481A6170B81','Тимофей','Королёв','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.3@gmail.com','5C7C2B54550E5195ED524BB97C07E33C02715C18C094AC2E12AD6DC169DB6BC03BDC980DE360B527A0F7CC900BA699175648A6CE9BBB3C21C285338114B7666E','Archie','Edwards','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.4@gmail.com','EAEAAD5C4088EB4177AE290EA139B9A078CFD1D5578D1F13A470BE907C9D01899C6F21B12C2097EA1296101E3AFCA7A98B4F96143C39D941A006500A99547177','Антон','Устинов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.5@gmail.com','4A2D7ED1C4E5E329043F1AB9DB5D98702112A250FB9C4146DDF606F9C88239CA141D75ACB9D44249A75DFE347ADA360D189D5E0983449FC8226CDA32AB500C8E','Георгий','Королёв','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.6@gmail.com','D662180579E081C13B4A31DA612B36AC0A94B87E1FB26B818C7197A5AB23C9738B2CFFD218A5C6A8480EED1D30FE596D425EC1D7010ED7768AAFC9A325C4BC30','Samuel','Wright','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.7@gmail.com','22E32F2B1F9C49A90F880FFA5CB7261EC346DA9CB7175195691599E67DB1E61F8C074198EF011FB6C5AC56829DF9F29AD5DE45D6A500015E408E1D78A8D36C6C','Arthur','White','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.8@gmail.com','90A5CE235C6EB072A171293B9DDBFCFB0A02AC8783C51B38A1493538765F13FD6F2B24CBBEBF2AF2818ED18A9311A7190248873C6E4C682E7081F4C91D788666','Max','Smith','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.9@gmail.com','78E42C192B5C5B1F7F6685B88586AB9AC1D92A5A7A15CCF2F45F11C29483D3AE5A6E8C797B5B4EECCBB24F727A84E550653657D954ADC170DCBC376B3FFD55A3','Борис','Абрамов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.10@gmail.com','CB0A8DDE41BFC6917F89030D6BB05DE171BB7593CAEF988B01BF22FD36A1252D2BE5F1690AC50B9A9DE8F2931FE7D6112BDE29EF38F3EEB295C68F9F6F011FB8','Max','Davies','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.11@gmail.com','48E501A0244715B592398E744AF3679B7BDB7B0AF0FEE05145D31C8C2EF9BD2195AC1B3F05B3FCDD287D01411F5E71B6FD67217602E44D8C66020BBD1E7CDD56','Генадий','Мишин','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.12@gmail.com','34151677F3D201B707B2253E4A7D90E3EC0619F73CFF9BB6FAAFE0116A2D71EEF50B1928C33FA379A11DFB0E6D927E470B8C375AC67B5EE5F662D59B77C34D86','Павел','Щербаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.13@gmail.com','A0A7D7635BB68265FD78926EF8272572385D638E8C680E9C6BBDCADA2189ED933E7A383B5CC2A44C6C494C0CD37F7B9DCF7C32395F68CF1267E95F25DDA7671C','Виктор','Щербаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.14@gmail.com','62036FCCA0874C2D042F8A62073CE07BD2C74864E946E7C7ED6F9440744015280C773FB371A59111CD8AEB873D0BBC52A65617F65048C08BAC52B37C199FFFE8','Вячеслав','Хохлов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.15@gmail.com','8E7638501BA13DEEDCFE5A4B40602F13F2A8918854B62F40550132DBD9CCD3413D3D9A363FCBB7ADA985CE2857C170F92852146374A0A9A4CDE1F50E87FF4036','Павел','Федосеев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.16@gmail.com','16462E6B5B4941E1587C2306FF3BCB3FE828B9F580548B6D6FA90291CC00CE1F464CA1B23ECA9BE40EC8F49484E926B8A1A3AE6D809AC4AD5F904C02AB37FD86','Артур','Ермаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.17@gmail.com','B421E301CB47945E33C3712D82019E9E03EAE64B0A52CAB04C09A4D5B827A857C7E2196771CD917AA19C74B6D9D1ECCA10BB61633987407A330D69A4E801B0AC','Сергей','Филиппов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.18@gmail.com','B7C378F9AA815D49CC1B08C9FB23CC7D09CC0390696718296FC7B7FAFBB22496A2206A5E700DA7EF1EC79D82D36CF025AED3A200EF0C5939C51DB5963BF3CECE','Владислав','Федосеев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.19@gmail.com','AFDC11B1E2001003FC2688F37A5778848492DA159D1AAD58ED9A46199AEA08CF50A8E109A1BD54E9A00F2707D55C40AFD685A8668A342B566B9659AC29962148','Роман','Уваров','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.20@gmail.com','AC8C35FE348F951B87BA7C2ECB84817324459EF5E6F9C0A1A2F8EF78E360F825F61533EB936AA5BB31864B4D5ABBA8F654054D83087AE364B19EADD4A164E147','Валентин','Иванов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.21@gmail.com','B569C7960C258BC30325006EC83A3D13F886A7DB468912E5AAE58A18222410540E04C231B405CA87C9ACCB1054844FF037141AD2DC9A826168E15FB16ECBB1B9','Руслан','Уваров','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.22@gmail.com','6A386E03F1A6BC43F6E27DA24759FA72FBB6E9D23E7F1C743E7A7B8C71AF046DB511F56D9194F5D6A2FBFD39C32AC17167E0442CA1E6A94231465C45F8923C07','Виктор','Соболев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.23@gmail.com','885279E5753207F493FB56321988B8C3F1DC93A2863B5E2E8C7066EC5AD32E06456CD4E1D5A748EB8DEB266823D167205E26C7018A219B81C2475A7594115FC7','Archie','Robinson','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.24@gmail.com','4BB66D4027FB90F7C72E805648B04E6CE9EDE975D0105D4F8A1CAB7EE18888F93BC4B65D4974A695837AF074A072B975464CB8AA31F9B6E8CB7F8E8430FC4C2D','Сергей','Соболев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.25@gmail.com','C6B3DFFCFE746071A5934E650D4B225D55661BC7CDD07D2199CBF4969A9B1A0F7B3480689A9DE0F05B28F46E051F69402923B033188E119066F8181150CD3683','Joseph','Walker','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.26@gmail.com','76B3AD036D81A2B46B28CB7B2304FAA9720D5BF60C50DF04147018D64B944410F68E4903171517FD30D0333A55653800995F9830E6EE9676603D5BE8CBCA000B','Freddie','Wright','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.27@gmail.com','E9A62F1A0C005924AE288B71FAF82D1A365BED5663C9E76633FED07AC46E491AD2BF7E17EA9C64BA44B80EBE577994510F936DCC0AF9B24D616632AC35BCF0A7','Вячеслав','Федосеев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.28@gmail.com','0759F640B5717023BB9ED39577531F6F71A0FF55815E53134294AF92D8B2F17BF17A59A491711E5DD5561D128B151993C04BB1BDD211E7F58AB7B32B871D5A9F','Вячеслав','Сысоев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.29@gmail.com','A638F3FDF65DE1B23B2F92163D73CE433242E4374F8DDFDFA8F6D76E1D8BEABA847AB7ACBD22235DBD1C157F1AB66CA9C8B90B11C9DAC3E6545F2F2E5EA8B297','Виктор','Ермаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.30@gmail.com','D5524F684D74DCF8239FE39AA86A9491F7F47060830369F61628EC554B4A4DD105A9385F9AC722423EA1C0B54DE49D940FCA54317DFDC23DDD992F3D6374389D','Артур','Дмитриев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.31@gmail.com','0E86F46DBEF09F5568BE601DD427425A11A4C060D8A5FC3B15BE04780CB5F211ABABDD6E78F8A16065351AB881200CCE4A7358A284A44F9792E7CF04CACCE750','Денис','Никонов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.32@gmail.com','7BF4F6F508345922CEBDD58C2B3BA9EA9F3A718EF0AEA616788E9762C5FE59BAD9AB5078B51C9ED03C0C2DECB03D4A33343FD7EDF5D0321C372B20153B1F880B','Антон','Королёв','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.33@gmail.com','0156E8CD85D00AE924099C3FC72FE639523C3126E9E5023ECE1E0B47A4D961BFB946C2D32CF1EC4ECD2CCA5C579B4121C9EF3C514C89D348171A5413146C613C','Иван','Иванов','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.34@gmail.com','93DF13BC84530382ECD37071DCAC7A410E2DAE3670815D51CC13AD4DFDA2402BCDBB17E51657238D08D9BED6E74CCBCB5174F059AF07200F99D9E2089FEF1EC7','Георгий','Овчинников','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.35@gmail.com','ADBFD7F380E926568B1ABE98BCF0102F0A07A7572A5C47B66E07607C127E2002D855677700216784F1504F48F8960024BD4019D98253CE897EAFE8F7E0341C61','Daniel','Green','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.36@gmail.com','38ECD1F5D968ED23AEF3FB349885AF6E10DEE464051ACABC852CC536517A6D534D8C06516359A5A66B8BC1F1087311C51B42F9590DC710E99D291C0B3BF079FC','Денис','Щербаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.37@gmail.com','6FAE297C88084FF49BAAB165F11A064F8065D86E175AEF5DA631ED15AA5AB315C76214D9295F45D088568F986BAC356511818DF7DED39D25079B8430F0EB6B3D','Иван','Ширяев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.38@gmail.com','D62DF28C502930DD3A2C7B859C4AEE29B6E4CD8D39EEBC4627081B8BB5D4E4E6E3AF6D1008BD47BB2484BD6FFCAB50806256E8251A8C78A7D63EFA80CF325DEF','Виктор','Ермаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.39@gmail.com','B6F738F8A7DA994A104EB70EA8EE52E70D2E1FBFF6D81134D9F6845AF748FD423B76D5E689A52203F6E750E46A4E71F6971B7CC830956CEFCE243E749B2C0D62','Иван','Мишин','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.40@gmail.com','1BF45CC7EF2D0064ED6E1F18EA5B0C5FAA84ACFE3F5B728A21F7D31B4F32327E98E58DF222BF91F91812C0670851DA0875AF3CECBC41F92C9B75C02F0A12ACF0','Владислав','Уваров','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.41@gmail.com','87DD5066844A1E6877E56E2D99A7F9619C1B3D0407C720F9665390AAB0942C0CD0BE4F4335BFEEC6331672D9C95B535ACC56DADD1D1D225ADBADF7C8B556057F','Edward','Hughes','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.42@gmail.com','60643170D9E18226BE5526911FDF261BAB776E8EEEC8815D0F8FE36E218CC3B30C7366C1AC4815E60E48FD930CECC3C96743606EDBAA317F2F2B4D7B36422FE6','Thomas','Williams','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.43@gmail.com','7D9E9CC0E18ED83F9FAF883E6BE6121442D5B4E1B03FFDF87B3420B820CCDB2EE0FE7F734CEEFA8780F5C61681B40701594525418900CDA6B9D27818BA34E44E','Lucas','Roberts','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.44@gmail.com','F52A55DAA93B0B5685979E5B854107AF98A270E11A20A7D9C24B131830A9A22052D64FCE3764AD725DD488276B8E0B58CDFAA8E91432E1F72408FF3934466A6D','Joseph','Williams','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.45@gmail.com','1FF8141EF9823690D236E1D8D48EB35F8A012B2C9535938F2863044043B973D860B2177B08E2EB577FE404D7CEB5E94598E1419A8C9E2FC663ECFE8A5A4F6CD9','James','Lewis','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.46@gmail.com','0F6E3130167DA3F8B0997260F31D886EE424A4205254CF67C8D4F72A078E3A67FB0EA0C59EDF239847D0AD15B82FA24286EFF568E1E5CF763B1FC3FB1A989DCE','Павел','Гурев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.47@gmail.com','907707405359B229447213099AB24F442E0FEF238456F2F5C218CD732F679C7D5B6BB395EA05CE842600D13BE6CCE3EAADA4A890CC81AA8A8B3FF84695B8661D','Владислав','Ермаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.48@gmail.com','84CBB6AD91F4D7DFF43838AEC90E85B026A906A898FEDE31694752A96B6060B732A3342AF79BBB8F127DF40B8AB022ED83CDAABF43D413425672B4636B44289A','Сергей','Мишин','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.49@gmail.com','2F395EAE4CAEB59A53F8B73F9C9F82DE5B66E127DA850F648218375BCC2B77F06FA1F06D3C20E1F7B3A70DB99D4D1772FF1E97679A1160FF7EF31E9E8D6F4E1B','Роман','Мишин','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.50@gmail.com','7778B31A7C807A8FFA6AA87A0764701111AD8BB2CF6881538C0E2DB673785A10BB70598B53B65C37178FB217F00FD744DA1E7437AD42415E76171E6B9D926AF7','Harry','Wood','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.51@gmail.com','AC72F46D123F3D09F42111579A4504DD52AD4289F32EF39CB44792C8D437495D901078CBC6E3A977E6D4D5A80A84B6062679188729E6018E4A4922ABB294CF2B','Сергей','Дмитриев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.52@gmail.com','2C58D0B9C7C35A504EADFB92456EB534222832EBB72F3C9F6605321D799062AD15721C662895C81E41355ADFA8FD9BDD11A8187755737CF4E21B8920AFB1D0B0','Тимофей','Дементьев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.53@gmail.com','415413DFE20E0B3A85AE933FE95FAE201E0D264E2301DD475D24A8B93AB06FDC447F9957F4D9806AAD3B1E696BC7CE78C44CD08D275759ABE99BC62A2658EE97','Артур','Мишин','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.54@gmail.com','72610F8265C95ED97D4D778509DEEC98C4B1F57CE4453AFDD4FCC686B8D5D55E7D100DD71046D82441A95EEED8E3A0E9607956FB096EB3E14951940923F8B3DB','Daniel','Evans','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.55@gmail.com','A0658382D08A43976D3EBACC0B565D023FC2F83BC02D7C9DF0E8E52E36FE2A7BC38D5FC5F84F12E462EE330929F40C0BD91050CB10DAD8C1D47924D3C1D7C36D','Семён','Гусев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.56@gmail.com','42FAD67E00C6CAA21C42FC35C1E9487ECE0816AF93E24EB07ED20B5528DA607C306E96990B14BABC9DF49922F49E7A6DF3E9E0BACB35817545C2678D51D3D213','Leo','Wood','client','en');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.57@gmail.com','3A464D70BCAB8233B2A8C3D58C66E9959C119BC544D8A3BB475E1C5AB0A86A824BB9EC9AC53D775094CD3BCD270CBFC49AD5D0E0806CA33046DF59FE95F56C03','Владислав','Ермаков','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.58@gmail.com','B25E448AC551BC1AFF27095A1FBF7E7D24EB34D31871BE9A04C907CD77347D27AB28D9E5BA9380387F68C7C06E386A455F4A6C34EA18A31387864CE57FF7E51F','Александр','Овчинников','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.59@gmail.com','13C30C48E30E088BE3C703DEB70B43BB1E5590D8113808B4BCC36BA58F30BA1B821C5B72575E0B123B72AB6BC5D36D8CAA8FFEBEA12AAEA8EC07E2DBC59F65FA','Генадий','Сысоев','client','ru');
INSERT INTO `admission_committee`.`users` (`email`,`password`,`first_name`,`last_name`,`role`,`lang`) VALUES ('example.60@gmail.com','99082B73B66896B4505B1B70CDBECF246AAB9EADCD08C47DA815C34BF31483B3FE31360823FD96202BB921E864787126FBE4BDD94FD478F2ECDC4803C1B1DCA0','Samuel','Smith','client','en');

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`enrollees`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Екатеринбург',9,2);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','Los Angeles',61,3);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Екатеринбург',76,4);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',53,5);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('France','Paris',79,6);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','Houston',76,7);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Munich',86,8);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Калининград',70,9);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Munich',96,10);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',69,11);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',56,12);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Ростов',30,13);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Минск',14,14);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Витебск',57,15);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Калининград',21,16);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',11,17);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Калининград',99,18);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Москва',78,19);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Ростов',17,20);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Гродно',99,21);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Минск',58,22);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('France','Marseille',13,23);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Гродно',66,24);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Cologne',65,25);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Munich',84,26);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Ростов',10,27);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Москва',83,28);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Калининград',76,29);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Витебск',51,30);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',87,31);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Гродно',81,32);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Санкт-Петербург',33,33);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Гродно',94,34);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','Houston',24,35);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Могилёв',65,36);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Москва',92,37);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Могилёв',98,38);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',53,39);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Минск',83,40);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Cologne',10,41);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Cologne',72,42);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','Phoenix',97,43);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Germany','Frankfurt',41,44);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','Los Angeles',43,45);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Ростов',29,46);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Екатеринбург',33,47);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Гродно',19,48);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Минск',43,49);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('France','Paris',74,50);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Витебск',17,51);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Витебск',6,52);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Могилёв',26,53);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','New York',89,54);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Екатеринбург',0,55);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('USA','Houston',63,56);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Могилёв',82,57);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Россия','Москва',18,58);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('Беларусь','Брест',67,59);
INSERT INTO `admission_committee`.`enrollees` (`country`,`city`,`school_certificate`,`users_id`) VALUES ('France','Paris',79,60);

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`enrollees_has_subjects`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (1,101,71);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (1,102,77);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (1,103,48);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (2,101,51);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (2,103,47);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (2,104,71);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (3,102,29);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (3,103,48);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (3,104,49);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (4,101,79);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (4,102,30);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (4,104,90);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (5,101,93);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (5,102,89);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (5,103,68);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (6,103,73);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (6,104,30);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (6,105,74);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (7,101,17);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (7,103,91);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (7,105,43);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (8,102,73);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (8,103,70);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (8,105,84);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (9,101,60);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (9,103,22);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (9,104,26);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (10,101,80);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (10,102,18);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (10,103,33);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (11,101,59);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (11,103,51);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (11,104,68);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (12,102,17);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (12,103,94);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (12,104,58);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (13,101,15);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (13,103,95);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (13,105,74);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (14,102,15);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (14,103,56);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (14,105,83);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (15,101,65);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (15,102,80);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (15,104,80);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (16,101,59);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (16,102,84);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (16,103,94);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (17,101,37);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (17,102,92);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (17,104,16);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (18,101,52);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (18,103,16);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (18,105,48);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (19,102,31);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (19,103,18);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (19,104,61);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (20,102,41);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (20,103,21);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (20,105,49);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (21,101,11);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (21,102,67);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (21,103,71);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (22,103,52);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (22,104,44);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (22,105,15);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (23,101,42);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (23,103,86);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (23,104,18);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (24,101,17);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (24,102,71);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (24,103,65);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (25,101,60);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (25,102,59);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (25,103,63);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (26,102,57);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (26,103,43);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (26,105,58);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (27,101,13);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (27,103,31);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (27,104,57);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (28,101,11);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (28,102,89);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (28,104,87);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (29,103,62);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (29,104,94);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (29,105,42);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (30,101,22);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (30,102,7);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (30,103,19);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (31,101,58);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (31,102,8);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (31,104,44);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (32,103,32);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (32,104,51);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (32,105,79);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (33,101,77);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (33,103,80);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (33,104,11);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (34,101,43);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (34,102,86);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (34,103,7);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (35,102,23);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (35,103,12);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (35,104,78);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (36,102,15);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (36,103,35);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (36,105,93);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (37,101,51);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (37,103,80);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (37,105,62);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (38,101,53);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (38,102,62);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (38,103,39);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (39,101,97);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (39,102,4);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (39,104,90);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (40,103,16);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (40,104,40);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (40,105,92);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (41,101,53);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (41,103,25);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (41,104,28);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (42,101,78);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (42,102,77);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (42,103,28);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (43,102,78);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (43,103,48);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (43,104,82);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (44,102,91);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (44,103,87);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (44,105,28);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (45,101,83);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (45,103,88);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (45,105,57);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (46,101,18);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (46,102,5);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (46,103,95);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (47,101,59);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (47,102,84);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (47,104,62);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (48,103,44);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (48,104,98);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (48,105,24);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (49,101,7);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (49,103,79);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (49,104,61);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (50,101,38);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (50,102,51);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (50,103,17);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (51,102,91);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (51,103,85);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (51,104,34);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (52,102,58);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (52,103,93);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (52,105,90);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (53,101,18);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (53,103,62);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (53,105,63);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (54,101,30);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (54,102,99);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (54,103,62);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (55,101,18);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (55,102,55);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (55,104,65);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (56,103,9);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (56,104,6);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (56,105,31);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (57,101,20);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (57,103,21);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (57,104,89);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (58,101,39);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (58,102,90);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (58,103,49);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (59,102,35);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (59,103,1);
INSERT INTO `admission_committee`.`enrollees_has_subjects` (`enrollees_id`,`subjects_id`,`score`) VALUES (59,104,97);

COMMIT;


-- -----------------------------------------------------
-- Data for table `admission_committee`.`admission_list`
-- -----------------------------------------------------
START TRANSACTION;
USE `admission_committee`;
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (1, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (2, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (3, 206);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (4, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (5, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (6, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (7, 208);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (8, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (9, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (10, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (11, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (12, 206);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (13, 208);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (14, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (15, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (16, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (18, 208);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (19, 206);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (20, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (21, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (22, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (23, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (24, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (25, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (26, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (27, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (28, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (29, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (30, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (31, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (32, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (33, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (34, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (35, 206);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (36, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (37, 208);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (38, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (39, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (40, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (41, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (42, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (43, 206);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (44, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (45, 208);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (46, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (47, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (48, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (49, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (50, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (51, 206);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (52, 207);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (53, 208);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (54, 201);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (55, 202);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (56, 203);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (57, 204);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (58, 205);
INSERT INTO `admission_committee`.`admission_list` (`enrollees_id`,`faculties_id`) VALUES (59, 206);

COMMIT;