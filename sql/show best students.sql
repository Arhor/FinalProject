SET @freeSeats = (select seats_budget from faculties where faculties.id = 202);
SET @paidSeats = (select seats_paid from faculties where faculties.id = 202);

PREPARE STMT FROM 
'SELECT enrollees_id ,(COALESCE(SUM(enrollees_has_subjects.score), 0) + enrollees.school_certificate) AS `total_score`, faculties.id AS `faculty ID` 
FROM enrollees_has_subjects 
RIGHT JOIN enrollees ON enrollees.id = enrollees_has_subjects.enrollees_id 
JOIN admission_list using (enrollees_id)
JOIN faculties ON admission_list.faculties_id = faculties.id
WHERE faculties.id = 202
GROUP BY enrollees_has_subjects.enrollees_id
ORDER BY total_score DESC
LIMIT ? OFFSET ?';

EXECUTE STMT USING @paidSeats, @freeSeats