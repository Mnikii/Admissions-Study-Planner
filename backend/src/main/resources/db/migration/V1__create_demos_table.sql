-- Создание таблицы demos
CREATE TABLE IF NOT EXISTS demos (
    id UUID PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    long_description VARCHAR(5000) NOT NULL,
    demo_path VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Индексы для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_demos_is_active ON demos(is_active);
CREATE INDEX IF NOT EXISTS idx_demos_created_at ON demos(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_demos_description ON demos(description);

-- Комментарии к таблице и колонкам
COMMENT ON TABLE demos IS 'Demo объекты для демонстрации архитектуры';
COMMENT ON COLUMN demos.id IS 'Уникальный идентификатор UUID';
COMMENT ON COLUMN demos.description IS 'Краткое описание (до 255 символов)';
COMMENT ON COLUMN demos.long_description IS 'Полное описание (до 5000 символов)';
COMMENT ON COLUMN demos.demo_path IS 'Путь к demo файлу (опционально)';
COMMENT ON COLUMN demos.is_active IS 'Статус активности demo';
COMMENT ON COLUMN demos.created_at IS 'Дата и время создания';
COMMENT ON COLUMN demos.updated_at IS 'Дата и время последнего обновления';
