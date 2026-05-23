--liquibase formatted sql

--changeset eduplan:001-create-users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT users_username_unique UNIQUE (username),
    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS users_username_idx ON users (username);
CREATE INDEX IF NOT EXISTS users_email_idx ON users (email);

--rollback DROP TABLE IF EXISTS users;

--changeset eduplan:002-create-universities
CREATE TABLE IF NOT EXISTS universities (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    website VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT universities_name_unique UNIQUE (name)
);

--rollback DROP TABLE IF EXISTS universities;

--changeset eduplan:003-create-auth-users
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

--rollback DROP TABLE IF EXISTS auth_users;

--changeset eduplan:004-create-study-plans
CREATE TABLE IF NOT EXISTS study_plans (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    target_country VARCHAR(100) NOT NULL,
    degree_level VARCHAR(50) NOT NULL,
    field_of_study VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    start_date DATE,
    deadline DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_study_plans_user_id ON study_plans (user_id);
CREATE INDEX IF NOT EXISTS idx_study_plans_user_id_status ON study_plans (user_id, status);
CREATE INDEX IF NOT EXISTS idx_study_plans_deleted_at ON study_plans (deleted_at);

--rollback DROP TABLE IF EXISTS study_plans;

--changeset eduplan:005-create-plan-tasks
CREATE TABLE IF NOT EXISTS plan_tasks (
    id UUID PRIMARY KEY,
    plan_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    task_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    deadline DATE,
    completed_at DATE,
    university_id UUID,
    program_id UUID,
    order_index INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_plan_tasks_plan_id ON plan_tasks (plan_id);
CREATE INDEX IF NOT EXISTS idx_plan_tasks_plan_id_status ON plan_tasks (plan_id, status);
CREATE INDEX IF NOT EXISTS idx_plan_tasks_deadline ON plan_tasks (deadline);
CREATE INDEX IF NOT EXISTS idx_plan_tasks_plan_id_deleted_at ON plan_tasks (plan_id, deleted_at);

--rollback DROP TABLE IF EXISTS plan_tasks;

--changeset eduplan:006-create-user-documents
CREATE TABLE IF NOT EXISTS user_documents (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    task_id uuid,
    document_type varchar(64) NOT NULL,
    file_name varchar(512) NOT NULL,
    file_url varchar(2048) NOT NULL,
    file_size bigint NOT NULL,
    mime_type varchar(128) NOT NULL,
    expiry_date date,
    is_verified boolean NOT NULL DEFAULT false,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    deleted_at timestamp
);

CREATE INDEX IF NOT EXISTS idx_user_documents_user_id ON user_documents(user_id);
CREATE INDEX IF NOT EXISTS idx_user_documents_task_id ON user_documents(task_id);
CREATE INDEX IF NOT EXISTS idx_user_documents_user_type ON user_documents(user_id, document_type);

--rollback DROP TABLE IF EXISTS user_documents;
