SELECT id, username, email, first_name, last_name, created_at
FROM users
WHERE deleted_at IS NULL
ORDER BY created_at DESC
LIMIT 20 OFFSET 40; -- 3 страница (по 20 зап. каждая)