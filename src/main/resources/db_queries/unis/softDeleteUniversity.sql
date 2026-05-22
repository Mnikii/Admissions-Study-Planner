UPDATE universities
SET deleted_at = NOW()
WHERE id = '550e8400-e29b-41d4-a716-446655440000'
AND deleted_at IS NULL
RETURNING id, name;