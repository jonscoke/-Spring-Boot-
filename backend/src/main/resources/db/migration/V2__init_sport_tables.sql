CREATE TABLE IF NOT EXISTS sport_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    sport_type VARCHAR(50) NOT NULL,
    steps INT NOT NULL DEFAULT 0,
    distance_km DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    duration_min INT NOT NULL DEFAULT 0,
    pace DECIMAL(6,2) NULL,
    calories_burned DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    source_type VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
    external_id VARCHAR(100) NULL,
    record_date DATE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sport_record_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT uk_sport_record_source_external UNIQUE (user_id, source_type, external_id)
);

CREATE INDEX idx_sport_record_user_date ON sport_record(user_id, record_date);

CREATE TABLE IF NOT EXISTS sport_sync_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    source_type VARCHAR(30) NOT NULL,
    total_count INT NOT NULL DEFAULT 0,
    inserted_count INT NOT NULL DEFAULT 0,
    duplicate_count INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    message VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sport_sync_log_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);
