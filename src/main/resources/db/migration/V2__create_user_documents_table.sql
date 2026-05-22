-- Create user_documents table
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
