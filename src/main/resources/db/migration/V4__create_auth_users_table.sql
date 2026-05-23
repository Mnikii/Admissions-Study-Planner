CREATE TABLE IF NOT EXISTS auth_users (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT fk_auth_users_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS auth_users_username_idx ON auth_users (username);
CREATE INDEX IF NOT EXISTS auth_users_user_id_idx ON auth_users (user_id);
