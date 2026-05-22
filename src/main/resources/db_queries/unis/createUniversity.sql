INSERT INTO universities (id, name, address, country, website, created_at)
VALUES (
    gen_random_uuid(),
    'Uni University',
    'ul. Ulichnaya, Moscow',
    'Russia',
    'https://univer.ru',
    NOW()
)
RETURNING *;