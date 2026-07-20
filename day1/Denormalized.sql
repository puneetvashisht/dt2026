CREATE SCHEMA IF NOT EXISTS step0_blob;

CREATE TABLE step0_blob.flat_trades (
    trade_id INT,
    ticker VARCHAR(10),
    company_name VARCHAR(100),
    exchange VARCHAR(50),
    counterparty VARCHAR(100),
    cp_contact VARCHAR(100),
    qty INT,
    price NUMERIC(10, 2)
);

-- DML: Insert the raw, highly redundant data records
INSERT INTO step0_blob.flat_trades 
VALUES 
(101, 'AAPL', 'Apple Inc.', 'NASDAQ', 'Goldman', 'sales@gs.com', 500, 180.00),
(102, 'AAPL', 'Apple Inc.', 'NASDAQ', 'MorganS', 'deals@ms.com', 1200, 180.50),
(103, 'TSLA', 'Tesla Inc.', 'NASDAQ', 'Goldman', 'sales@gs.com', 300, 220.00);


-- 1NF
CREATE SCHEMA IF NOT EXISTS step1_1nf;

CREATE TABLE step1_1nf.trades (
    -- Rule: Primary key defined to uniquely identify rows
    trade_id INT PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    exchange VARCHAR(50) NOT NULL,
    counterparty VARCHAR(100) NOT NULL,
    cp_contact VARCHAR(100) NOT NULL,
    qty INT NOT NULL,
    -- Rule: Using numeric for exact math precision
    price NUMERIC(10, 2) NOT NULL 
);

-- Migration DML from Step 0 to Step 1
INSERT INTO step1_1nf.trades 
SELECT * FROM step0_blob.flat_trades;


CREATE SCHEMA IF NOT EXISTS step1_1nf;

CREATE TABLE step1_1nf.trades (
    -- Rule: Primary key defined to uniquely identify rows
    trade_id INT PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    exchange VARCHAR(50) NOT NULL,
    counterparty VARCHAR(100) NOT NULL,
    cp_contact VARCHAR(100) NOT NULL,
    qty INT NOT NULL,
    -- Rule: Using numeric for exact math precision
    price NUMERIC(10, 2) NOT NULL 
);

-- Migration DML from Step 0 to Step 1
INSERT INTO step1_1nf.trades 
SELECT * FROM step0_blob.flat_trades;

CREATE SCHEMA IF NOT EXISTS step2_2nf;

-- 1. Create the standalone instruments table
CREATE TABLE step2_2nf.instruments (
    ticker VARCHAR(10) PRIMARY KEY, -- The attributes now depend ENTIRELY on this PK
    company_name VARCHAR(100) NOT NULL,
    exchange VARCHAR(50) NOT NULL
);

-- 2. Create the evolved trades table linked via Foreign Key
CREATE TABLE step2_2nf.trades (
    trade_id INT PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL REFERENCES step2_2nf.instruments(ticker),
    counterparty VARCHAR(100) NOT NULL,
    cp_contact VARCHAR(100) NOT NULL,
    qty INT NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

-- Migration DML: Extract the distinct structural instruments first
INSERT INTO step2_2nf.instruments (ticker, company_name, exchange)
SELECT DISTINCT ticker, company_name, exchange 
FROM step1_1nf.trades;

-- Migration DML: Insert trades referencing the new instruments table
INSERT INTO step2_2nf.trades (trade_id, ticker, counterparty, cp_contact, qty, price)
SELECT trade_id, ticker, counterparty, cp_contact, qty, price 
FROM step1_1nf.trades;



CREATE SCHEMA IF NOT EXISTS step3_3nf;

-- 1. Retain the instruments definition from 2NF
CREATE TABLE step3_3nf.instruments (
    ticker VARCHAR(10) PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    exchange VARCHAR(50) NOT NULL
);

-- 2. Create the separate counterparties table to isolate contact updates
CREATE TABLE step3_3nf.counterparties (
    cp_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Standard clean surrogate key
    name VARCHAR(100) NOT NULL UNIQUE,
    contact_email VARCHAR(100) NOT NULL
);

-- 3. The final normalized core transaction ledger
CREATE TABLE step3_3nf.trades (
    trade_id INT PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL REFERENCES step3_3nf.instruments(ticker),
    cp_id INT NOT NULL REFERENCES step3_3nf.counterparties(cp_id),
    qty NUMERIC(18, 4) NOT NULL, -- Upgraded column type to handle partial/fractional volumes
    price NUMERIC(18, 4) NOT NULL
);

-- Migration DML: Step A - Populate instruments
INSERT INTO step3_3nf.instruments 
SELECT * FROM step2_2nf.instruments;

-- Migration DML: Step B - Extract unique counterparties
INSERT INTO step3_3nf.counterparties (name, contact_email)
SELECT DISTINCT counterparty, cp_contact 
FROM step2_2nf.trades;

-- Migration DML: Step C - Connect everything into the transaction ledger via lookups
INSERT INTO step3_3nf.trades (trade_id, ticker, cp_id, qty, price)
SELECT 
    t.trade_id, 
    t.ticker, 
    c.cp_id, 
    t.qty, 
    t.price
FROM step2_2nf.trades t
JOIN step3_3nf.counterparties c ON t.counterparty = c.name;


SELECT 
    t.trade_id,
    t.ticker,
    i.company_name,
    i.exchange,
    c.name AS counterparty,
    c.contact_email AS cp_contact,
    t.qty,
    t.price
FROM step3_3nf.trades t
JOIN step3_3nf.instruments i ON t.ticker = i.ticker
JOIN step3_3nf.counterparties c ON t.cp_id = c.cp_id
ORDER BY t.trade_id;