SELECT `users`.`first_name`,
       `users`.`last_name`,
       (`score` + `enrollees`.`school_certificate`) AS 'total_score',
       `faculties`.`name_ru`,
       `admission_list`.`is_passed`
FROM   `enrollees`
	   JOIN `users`     ON `users`.`id` = `enrollees`.`users_id`
	   JOIN `admission_list` ON `enrollees`.`id` = `admission_list`.`enrollees_id`
	   JOIN `faculties` ON `faculties`.`id` = `admission_list`.`faculties_id`
	   JOIN (
				SELECT `enrollees_has_subjects`.`enrollees_id` ,sum(`score`) as `score`
				FROM `enrollees_has_subjects`
				GROUP BY `enrollees_has_subjects`.`enrollees_id`
			) AS `total_score` ON `enrollees`.`id` = `total_score`.`enrollees_id`