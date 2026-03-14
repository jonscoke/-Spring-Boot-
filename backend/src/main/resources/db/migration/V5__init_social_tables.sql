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

CREATE INDEX idx_user_goal_user_status ON user_goal(user_id, status);

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

CREATE INDEX idx_user_checkin_user_date ON user_checkin(user_id, checkin_date);

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

CREATE INDEX idx_social_post_status_created ON social_post(status, created_at);

CREATE TABLE IF NOT EXISTS social_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_social_comment_post FOREIGN KEY (post_id) REFERENCES social_post(id) ON DELETE CASCADE,
    CONSTRAINT fk_social_comment_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

CREATE INDEX idx_social_comment_post_created ON social_comment(post_id, created_at);

CREATE TABLE IF NOT EXISTS social_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_social_like_post FOREIGN KEY (post_id) REFERENCES social_post(id) ON DELETE CASCADE,
    CONSTRAINT fk_social_like_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT uk_social_like_post_user UNIQUE (post_id, user_id)
);

CREATE INDEX idx_social_like_post_user ON social_like(post_id, user_id);
