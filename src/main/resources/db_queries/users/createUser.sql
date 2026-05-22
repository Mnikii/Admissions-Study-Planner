INSERT INTO users (id, username, email, first_name, last_name, phone_number, birthday, status, created_at)
VALUES (
    gen_random_uuid(),
    'vasvasya',
    'vasya@example.com',
    'Vasya',
    'Vasiliev',
    '+1234567890',
    '1990-05-15',
    'active',
    NOW()
)
RETURNING *;