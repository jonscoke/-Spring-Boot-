CREATE TABLE IF NOT EXISTS user_goal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    goal_type VARCHAR(30) NOT NULL,
    goal_value DECIMAL(10,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_goal_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

SET @user_goal_user_status_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'user_goal'
      AND index_name = 'idx_user_goal_user_status'
);
SET @user_goal_user_status_index_sql = IF(
    @user_goal_user_status_index_exists = 0,
    'CREATE INDEX idx_user_goal_user_status ON user_goal(user_id, status)',
    'SELECT 1'
);
PREPARE user_goal_user_status_index_stmt FROM @user_goal_user_status_index_sql;
EXECUTE user_goal_user_status_index_stmt;
DEALLOCATE PREPARE user_goal_user_status_index_stmt;

CREATE TABLE IF NOT EXISTS user_checkin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    checkin_date DATE NOT NULL,
    content VARCHAR(255) NOT NULL,
    mood_score INT NULL,
    goal_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_checkin_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_user_checkin_goal FOREIGN KEY (goal_id) REFERENCES user_goal(id),
    CONSTRAINT uk_user_checkin_user_date UNIQUE (user_id, checkin_date)
);

SET @user_checkin_user_date_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'user_checkin'
      AND index_name = 'idx_user_checkin_user_date'
);
SET @user_checkin_user_date_index_sql = IF(
    @user_checkin_user_date_index_exists = 0,
    'CREATE INDEX idx_user_checkin_user_date ON user_checkin(user_id, checkin_date)',
    'SELECT 1'
);
PREPARE user_checkin_user_date_index_stmt FROM @user_checkin_user_date_index_sql;
EXECUTE user_checkin_user_date_index_stmt;
DEALLOCATE PREPARE user_checkin_user_date_index_stmt;

CREATE TABLE IF NOT EXISTS social_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    like_count INT NOT NULL DEFAULT 0,
    comment_count INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_social_post_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

SET @social_post_status_created_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'social_post'
      AND index_name = 'idx_social_post_status_created'
);
SET @social_post_status_created_index_sql = IF(
    @social_post_status_created_index_exists = 0,
    'CREATE INDEX idx_social_post_status_created ON social_post(status, created_at)',
    'SELECT 1'
);
PREPARE social_post_status_created_index_stmt FROM @social_post_status_created_index_sql;
EXECUTE social_post_status_created_index_stmt;
DEALLOCATE PREPARE social_post_status_created_index_stmt;

CREATE TABLE IF NOT EXISTS social_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_social_comment_post FOREIGN KEY (post_id) REFERENCES social_post(id) ON DELETE CASCADE,
    CONSTRAINT fk_social_comment_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

SET @social_comment_post_created_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'social_comment'
      AND index_name = 'idx_social_comment_post_created'
);
SET @social_comment_post_created_index_sql = IF(
    @social_comment_post_created_index_exists = 0,
    'CREATE INDEX idx_social_comment_post_created ON social_comment(post_id, created_at)',
    'SELECT 1'
);
PREPARE social_comment_post_created_index_stmt FROM @social_comment_post_created_index_sql;
EXECUTE social_comment_post_created_index_stmt;
DEALLOCATE PREPARE social_comment_post_created_index_stmt;

CREATE TABLE IF NOT EXISTS social_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_social_like_post FOREIGN KEY (post_id) REFERENCES social_post(id) ON DELETE CASCADE,
    CONSTRAINT fk_social_like_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT uk_social_like_post_user UNIQUE (post_id, user_id)
);

SET @social_like_post_user_index_exists = (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'social_like'
      AND index_name = 'idx_social_like_post_user'
);
SET @social_like_post_user_index_sql = IF(
    @social_like_post_user_index_exists = 0,
    'CREATE INDEX idx_social_like_post_user ON social_like(post_id, user_id)',
    'SELECT 1'
);
PREPARE social_like_post_user_index_stmt FROM @social_like_post_user_index_sql;
EXECUTE social_like_post_user_index_stmt;
DEALLOCATE PREPARE social_like_post_user_index_stmt;
