DROP TABLE IF EXISTS demo_trades;

CREATE TABLE demo_trades (
    trade_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL,
    volume INT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    trade_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step A: Change a data type
ALTER TABLE demo_trades 
    ALTER COLUMN volume TYPE NUMERIC(18, 6);

-- Step B: Add a new column with a default value
ALTER TABLE demo_trades 
    ADD COLUMN status VARCHAR(20) DEFAULT 'PENDING' NOT NULL;

CREATE TABLE type_showdown (
    exact_money NUMERIC(18,4),
    approx_money DOUBLE PRECISION,
    local_time TIMESTAMP,
    global_time TIMESTAMPTZ
);

SELECT 0.1::DOUBLE PRECISION + 0.2::DOUBLE PRECISION = 0.3::DOUBLE PRECISION AS double_result,
       0.1::NUMERIC + 0.2::NUMERIC = 0.3::NUMERIC AS numeric_result;
