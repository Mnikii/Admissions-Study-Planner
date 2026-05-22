SELECT * FROM universities
WHERE country = 'Russia'
AND deleted_at IS NULL
ORDER BY name;