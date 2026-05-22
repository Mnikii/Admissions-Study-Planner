SELECT id, name, country, website
FROM universities
WHERE deleted_at IS NULL
ORDER BY name ASC;