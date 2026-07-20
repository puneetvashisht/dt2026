-- Seed additional comparative data into counterparties
INSERT INTO step3_3nf.counterparties (name, contact_email) VALUES 
('Barclays', 'settlements@barclays.com'),
('Citigroup', 'otc@citi.com'),
('Unassigned Partner Inc', 'test@void.com'); -- To demonstrate NULL/Left/Right join behaviors

-- Seed additional diverse instruments 
INSERT INTO step3_3nf.instruments (ticker, company_name, exchange) VALUES 
('MSFT', 'Microsoft Corp', 'NASDAQ'),
('GOOGL', 'Alphabet Inc', 'NASDAQ'),
('INFY', 'Infosys Ltd', 'NSE'); -- Indian exchange ticker example

-- Seed transactions across multiple tickers and counterparties
INSERT INTO step3_3nf.trades (trade_id, ticker, cp_id, qty, price) VALUES 
(104, 'MSFT', 1, 400.0000, 420.5000),  -- Goldman trading MSFT
(105, 'GOOGL', 2, 800.0000, 175.2500), -- MorganS trading GOOGL
(106, 'AAPL', 3, 150.0000, 182.0000),  -- Barclays trading AAPL
(107, 'MSFT', 2, 600.0000, 419.0000);  -- MorganS trading MSFT


-- A. SELECT with Aliases & Expression Columns
SELECT 
    trade_id,
    ticker,
    qty AS trade_volume,
    price AS unit_price,
    (qty * price) AS total_notional_value
FROM step3_3nf.trades;

-- B. ORDER BY (Sorting Multiple Levels & Directions)
SELECT trade_id, ticker, qty, price
FROM step3_3nf.trades
ORDER BY ticker ASC, qty DESC;

-- C. UPDATE (Targeted modification with WHERE clause)
UPDATE step3_3nf.trades
SET qty = 550.0000, price = 181.2500
WHERE trade_id = 101;

-- D. DELETE (Safely dropping a specific record)
-- First insert a dummy trade to kill
INSERT INTO step3_3nf.trades (trade_id, ticker, cp_id, qty, price) 
VALUES (999, 'INFY', 1, 10.0000, 20.0000);

DELETE FROM step3_3nf.trades
WHERE trade_id = 999;

-- A. INNER JOIN: Only returns rows where a match exists on both sides
-- (Will hide 'INFY' because it has no trades, and hide 'Unassigned Partner Inc' because it has no trades)
SELECT t.trade_id, t.ticker, c.name AS counterparty
FROM step3_3nf.trades t
INNER JOIN step3_3nf.counterparties c ON t.cp_id = c.cp_id;

-- B. LEFT JOIN: Keeps all rows from the left table (Trades), pulls matches from right
SELECT t.trade_id, t.ticker, c.name AS counterparty
FROM step3_3nf.trades t
LEFT JOIN step3_3nf.counterparties c ON t.cp_id = c.cp_id;

-- C. RIGHT JOIN: Keeps all counterparties, even if they have NEVER executed a trade
-- (Watch out for 'Unassigned Partner Inc' appearing with NULL trade metrics)
SELECT t.trade_id, t.ticker, c.name AS counterparty
FROM step3_3nf.trades t
RIGHT JOIN step3_3nf.counterparties c ON t.cp_id = c.cp_id;

-- D. FULL OUTER JOIN: Keeps all rows from both tables. 
-- Shows all trades and all instruments, filling unlinked mappings with NULL values.
SELECT t.trade_id, i.ticker, i.company_name
FROM step3_3nf.trades t
FULL OUTER JOIN step3_3nf.instruments i ON t.ticker = i.ticker;


-- Calculate volume and risk metrics per ticker
-- Enforce the rule: HAVING filters aggregated groups, WHERE filters raw rows
SELECT 
    ticker,
    COUNT(trade_id) AS total_trades,
    SUM(qty) AS total_shares_traded,
    ROUND(AVG(price), 2) AS average_execution_price,
    MAX(price) AS highest_paid,
    MIN(price) AS lowest_paid
FROM step3_3nf.trades
WHERE price > 100.00 -- Operational Filter: Only analyze premium assets
GROUP BY ticker
HAVING SUM(qty) > 500.00 -- Aggregation Filter: Only keep heavy-volume tickers
ORDER BY total_shares_traded DESC;


-- A. Scalar Subquery (Used inside WHERE clause to pull the global average price)
SELECT trade_id, ticker, price
FROM step3_3nf.trades
WHERE price > (SELECT AVG(price) FROM step3_3nf.trades);

-- B. Correlated Subquery (Evaluates line-by-line using contextual mapping from parent)
-- Finds trades where the volume is greater than the average volume *for that specific ticker*
SELECT t1.trade_id, t1.ticker, t1.qty
FROM step3_3nf.trades t1
WHERE t1.qty > (
    SELECT AVG(t2.qty) 
    FROM step3_3nf.trades t2 
    WHERE t2.ticker = t1.ticker
);

-- C. Common Table Expression (CTE) - Clean, readable alternative to nested subqueries
WITH ticker_summary AS (
    SELECT ticker, SUM(qty * price) AS total_notional
    FROM step3_3nf.trades
    GROUP BY ticker
)
SELECT ticker, total_notional
FROM ticker_summary
WHERE total_notional > 100000.00;


-- Create a secure, reusable layer hiding the underlying entity splits from analysts
CREATE OR REPLACE VIEW step3_3nf.v_trade_ledger_manifest AS
SELECT 
    t.trade_id,
    t.ticker,
    i.company_name,
    i.exchange,
    c.name AS counterparty_name,
    t.qty,
    t.price,
    (t.qty * t.price) AS total_cost
FROM step3_3nf.trades t
JOIN step3_3nf.instruments i ON t.ticker = i.ticker
JOIN step3_3nf.counterparties c ON t.cp_id = c.cp_id;

-- Query the view just like a standard table
SELECT * 
FROM step3_3nf.v_trade_ledger_manifest 
WHERE exchange = 'NASDAQ';


-- Demonstration A: The ROLLBACK (Simulating a failed validation checkout script)
BEGIN;

INSERT INTO step3_3nf.trades (trade_id, ticker, cp_id, qty, price) 
VALUES (888, 'AAPL', 1, 100.0000, 185.0000);

-- Query inside the transaction block confirms row 888 exists here locally
SELECT * FROM step3_3nf.trades WHERE trade_id = 888;

-- Something went wrong! Discard the uncommitted buffer state cleanly
ROLLBACK;

-- Verify row 888 was completely dropped and never hit persistent disk arrays
SELECT * FROM step3_3nf.trades WHERE trade_id = 888;


-- Demonstration B: The COMMIT (Successful data persistence write)
BEGIN;

UPDATE step3_3nf.trades 
SET price = price + 5.00 
WHERE ticker = 'MSFT';

-- Flush, seal state, and safely apply write modifications to disk
COMMIT;

