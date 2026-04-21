CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                       title VARCHAR(255) NOT NULL,
                       priority INTEGER CHECK (priority BETWEEN 1 AND 5),
                       is_completed BOOLEAN DEFAULT FALSE,
                       due_date DATE
);