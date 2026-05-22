UPDATE users
SET status = 'blocked'
WHERE id IN (SELECT unnest(ARRAY['id1', 'id2', 'id3']::uuid[]))
AND deleted_at IS NULL;