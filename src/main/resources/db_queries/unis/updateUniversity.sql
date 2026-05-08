UPDATE universities
SET
    name = 'Uni University',
    website = 'https://univer.ru',
    address = 'ul. Ulichnaya, Moscow',
WHERE id = '550e8400-e29b-41d4-a716-446655440000'
AND deleted_at IS NULL
RETURNING *;