INSERT INTO users (username, email) VALUES
                                        ('alex_dev', 'alex@example.com'),
                                        ('maria_pm', 'maria@example.com'),
                                        ('ivan_backend', 'ivan@example.com'),
                                        ('olga_qa', 'olga@example.com'),
                                        ('oleg_ds', 'oleg@example.com');

INSERT INTO tasks (user_id, title, priority, due_date) VALUES
                                                           (1, 'Изучить Spring Boot', 5, '2026-05-01'),
                                                           (2, 'Подготовить roadmap на квартал', 4, '2026-05-03'),
                                                           (3, 'Реализовать REST API для задач', 5, '2026-05-07'),
                                                           (4, 'Составить чек-лист регресса', 3, '2026-05-10'),
                                                           (5, 'Собрать датасет для анализа', 4, '2026-05-12');

UPDATE tasks SET is_completed = TRUE WHERE id = 1;

SELECT title, due_date
FROM tasks
WHERE is_completed = FALSE AND priority >= 4
ORDER BY due_date ASC;