CREATE TABLE plan_tasks (
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

CREATE INDEX idx_plan_tasks_plan_id ON plan_tasks (plan_id);
CREATE INDEX idx_plan_tasks_plan_id_status ON plan_tasks (plan_id, status);
CREATE INDEX idx_plan_tasks_deadline ON plan_tasks (deadline);
CREATE INDEX idx_plan_tasks_plan_id_deleted_at ON plan_tasks (plan_id, deleted_at);

