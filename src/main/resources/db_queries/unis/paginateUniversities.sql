SELECT * FROM universities
WHERE deleted_at IS NULL
ORDER BY name ASC
LIMIT 20 OFFSET 40;  -- 3 страница (по 20 зап. на странице)