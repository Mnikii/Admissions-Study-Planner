CREATE TABLE study_plans (
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

CREATE INDEX idx_study_plans_user_id ON study_plans (user_id);
CREATE INDEX idx_study_plans_user_id_status ON study_plans (user_id, status);
CREATE INDEX idx_study_plans_deleted_at ON study_plans (deleted_at);


