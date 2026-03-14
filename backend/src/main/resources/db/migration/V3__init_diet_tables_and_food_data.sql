CREATE TABLE IF NOT EXISTS food_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    food_name VARCHAR(100) NOT NULL,
    food_category VARCHAR(50) NULL,
    unit VARCHAR(20) NOT NULL DEFAULT 'g',
    calorie_per_100g DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    protein_per_100g DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    fat_per_100g DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    carb_per_100g DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_food_item_name UNIQUE (food_name)
);

CREATE INDEX idx_food_item_name_status ON food_item(food_name, status);

CREATE TABLE IF NOT EXISTS diet_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    meal_type VARCHAR(30) NOT NULL,
    record_date DATE NOT NULL,
    total_calories DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_protein DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_fat DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_carb DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    remark VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_diet_record_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

CREATE INDEX idx_diet_record_user_date ON diet_record(user_id, record_date);

CREATE TABLE IF NOT EXISTS diet_record_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    diet_record_id BIGINT NOT NULL,
    food_item_id BIGINT NOT NULL,
    food_name VARCHAR(100) NOT NULL,
    intake_gram DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    calories DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    protein DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    fat DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    carb DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_diet_record_item_record FOREIGN KEY (diet_record_id) REFERENCES diet_record(id) ON DELETE CASCADE,
    CONSTRAINT fk_diet_record_item_food FOREIGN KEY (food_item_id) REFERENCES food_item(id)
);

CREATE INDEX idx_diet_record_item_record ON diet_record_item(diet_record_id);

INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Rice', 'Staple', 'g', 116, 2.60, 0.30, 25.90, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Rice');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Noodle', 'Staple', 'g', 138, 4.50, 1.80, 25.10, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Noodle');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Chicken Breast', 'Meat', 'g', 133, 24.00, 3.00, 0.00, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Chicken Breast');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Beef Lean', 'Meat', 'g', 125, 20.20, 4.20, 0.00, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Beef Lean');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Salmon', 'Fish', 'g', 139, 22.30, 4.80, 0.00, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Salmon');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Egg', 'Protein', 'g', 144, 13.30, 8.80, 1.30, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Egg');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Milk', 'Dairy', 'g', 54, 3.20, 3.20, 3.40, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Milk');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Yogurt', 'Dairy', 'g', 72, 2.50, 3.30, 8.20, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Yogurt');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Tofu', 'Protein', 'g', 81, 8.10, 4.20, 1.90, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Tofu');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Broccoli', 'Vegetable', 'g', 34, 3.60, 0.40, 4.30, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Broccoli');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Spinach', 'Vegetable', 'g', 24, 2.60, 0.30, 3.10, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Spinach');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Tomato', 'Vegetable', 'g', 20, 0.90, 0.20, 3.90, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Tomato');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Cucumber', 'Vegetable', 'g', 16, 0.80, 0.10, 2.90, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Cucumber');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Apple', 'Fruit', 'g', 53, 0.30, 0.20, 13.70, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Apple');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Banana', 'Fruit', 'g', 93, 1.40, 0.20, 22.00, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Banana');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Orange', 'Fruit', 'g', 47, 0.90, 0.20, 11.20, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Orange');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Oatmeal', 'Staple', 'g', 367, 15.00, 6.70, 61.00, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Oatmeal');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Sweet Potato', 'Staple', 'g', 86, 1.60, 0.10, 20.10, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Sweet Potato');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Peanut', 'Nut', 'g', 567, 25.80, 49.20, 16.10, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Peanut');
INSERT INTO food_item(food_name, food_category, unit, calorie_per_100g, protein_per_100g, fat_per_100g, carb_per_100g, status)
SELECT 'Almond', 'Nut', 'g', 578, 21.20, 50.60, 20.50, 1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE food_name = 'Almond');
