SELECT * FROM universities
WHERE name ILIKE '%moscow%'
AND deleted_at IS NULL
ORDER BY name
LIMIT 20;