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

CREATE INDEX idx_advice_log_user_created ON advice_log(user_id, created_at);

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

CREATE INDEX idx_weekly_report_user_week ON weekly_report(user_id, week_start_date, week_end_date);

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

CREATE INDEX idx_monthly_report_user_month ON monthly_report(user_id, report_month);
