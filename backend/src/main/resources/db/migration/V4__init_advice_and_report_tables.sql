CREATE TABLE IF NOT EXISTS advice_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    energy_advice TEXT NOT NULL,
    diet_advice TEXT NOT NULL,
    sport_advice TEXT NOT NULL,
    advice_text TEXT NOT NULL,
    summary_json TEXT NOT NULL,
    source_type VARCHAR(20) NOT NULL DEFAULT 'RULE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_advice_log_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

SET @advice_log_user_created_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'advice_log'
      AND index_name = 'idx_advice_log_user_created'
);
SET @advice_log_user_created_index_sql = IF(
    @advice_log_user_created_index_exists = 0,
    'CREATE INDEX idx_advice_log_user_created ON advice_log(user_id, created_at)',
    'SELECT 1'
);
PREPARE advice_log_user_created_index_stmt FROM @advice_log_user_created_index_sql;
EXECUTE advice_log_user_created_index_stmt;
DEALLOCATE PREPARE advice_log_user_created_index_stmt;

CREATE TABLE IF NOT EXISTS weekly_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,
    summary_json TEXT NOT NULL,
    report_text TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_weekly_report_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

SET @weekly_report_user_week_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'weekly_report'
      AND index_name = 'idx_weekly_report_user_week'
);
SET @weekly_report_user_week_index_sql = IF(
    @weekly_report_user_week_index_exists = 0,
    'CREATE INDEX idx_weekly_report_user_week ON weekly_report(user_id, week_start_date, week_end_date)',
    'SELECT 1'
);
PREPARE weekly_report_user_week_index_stmt FROM @weekly_report_user_week_index_sql;
EXECUTE weekly_report_user_week_index_stmt;
DEALLOCATE PREPARE weekly_report_user_week_index_stmt;

CREATE TABLE IF NOT EXISTS monthly_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    report_month VARCHAR(7) NOT NULL,
    month_start_date DATE NOT NULL,
    month_end_date DATE NOT NULL,
    summary_json TEXT NOT NULL,
    report_text TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_monthly_report_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

SET @monthly_report_user_month_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'monthly_report'
      AND index_name = 'idx_monthly_report_user_month'
);
SET @monthly_report_user_month_index_sql = IF(
    @monthly_report_user_month_index_exists = 0,
    'CREATE INDEX idx_monthly_report_user_month ON monthly_report(user_id, report_month)',
    'SELECT 1'
);
PREPARE monthly_report_user_month_index_stmt FROM @monthly_report_user_month_index_sql;
EXECUTE monthly_report_user_month_index_stmt;
DEALLOCATE PREPARE monthly_report_user_month_index_stmt;
