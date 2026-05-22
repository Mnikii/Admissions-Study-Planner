UPDATE users
SET
    first_name = 'Vasya',
    last_name = 'Vasyi',
    phone_number = '+9876543210',
WHERE id = '550e8400-e29b-41d4-a716-446655440000'
AND deleted_at IS NULL
RETURNING *;