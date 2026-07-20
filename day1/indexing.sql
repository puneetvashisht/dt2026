-- 1. Create the table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50),
    email VARCHAR(100),
    is_active BOOLEAN
);

-- 2. Insert 99,000 ACTIVE users
INSERT INTO users (username, email, is_active)
SELECT 
    'user_' || i, 
    'active_' || i || '@example.com', 
    true
FROM generate_series(1, 99000) i;

-- 3. Insert 1,000 INACTIVE users (the 1%)
INSERT INTO users (username, email, is_active)
SELECT 
    'inactive_' || i, 
    'pending_' || i || '@example.com', 
    false
FROM generate_series(1, 1000) i;



EXPLAIN ANALYZE 
SELECT * FROM users WHERE email = 'active_50000@example.com';

CREATE INDEX idx_users_email ON users(email


-- partial indexing

CREATE INDEX idx_users_inactive 
ON users(email) 
WHERE is_active = false;


EXPLAIN ANALYZE 
SELECT * FROM users 
WHERE email = 'pending_500@example.com' AND is_active = false;


SELECT 
    relname AS index_name,
    pg_size_pretty(pg_relation_size(oid)) AS index_size
FROM pg_class 
WHERE relname IN ('idx_users_email', 'idx_users_inactive');

-- Because the partial index completely ignored the 99,000 active users, it takes up almost no space (64 KB vs 3.1 MB). It achieves the exact same blistering lookup speed for your administrative tasks while placing virtually zero memory or performance burden on your main database write operations.


