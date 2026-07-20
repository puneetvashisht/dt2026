CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    category VARCHAR(50),
    amount NUMERIC(10, 2)
);

-- Seed with mixed sales data
INSERT INTO sales (customer_id, order_date, category, amount) VALUES
(101, '2026-07-01', 'Electronics', 1200.00),
(102, '2026-07-01', 'Electronics', 800.00),
(101, '2026-07-01', 'Apparel',     150.00),
(103, '2026-07-02', 'Electronics',  450.00),
(102, '2026-07-02', 'Apparel',      80.00),
(104, '2026-07-02', 'Home',         300.00),
(101, '2026-07-03', 'Electronics', 1100.00),
(103, '2026-07-03', 'Home',         600.00),
(104, '2026-07-03', 'Apparel',      120.00);

-- A Window Function performs a calculation across a set of table rows that are related to the current row. Unlike regular aggregate functions (SUM, AVG), window functions do not collapse rows into a single output row. Every row retains its unique identity.
-- The running total of sales for that specific category over time.
-- The rank of that sale within its category (highest amount = Rank 1).

-- SELECT 
--     id, 
--     order_date, 
--     category, 
--     amount,
--     -- 1. Running total grouped by category, ordered by date
--     SUM(amount) OVER(
--         PARTITION BY category 
--         ORDER BY order_date
--     ) AS category_running_total,
--     -- 2. Rank within the category based on amount descending
--     DENSE_RANK() OVER(
--         PARTITION BY category 
--         ORDER BY amount DESC
--     ) AS rank_in_category
-- FROM sales
-- ORDER BY category, order_date;


-- A CTE acts as a temporary result set that you can reference within another SQL statement. Think of it as creating a readable, named variable out of a query.
-- We want to find our "VIP Customers"—specifically, customers whose total spend is higher than the average total spend across all customers. Doing this in a single query with subqueries is messy and hard to read.


WITH customer_totals AS (
    -- CTE 1: Calculate total spend per customer
    SELECT customer_id, SUM(amount) AS total_spend
    FROM sales
    GROUP BY customer_id
),
overall_average AS (
    -- CTE 2: Calculate the average of those totals
    SELECT AVG(total_spend) AS avg_spend 
    FROM customer_totals
)
-- Main Query: Compare individual totals to the overall average
SELECT c.customer_id, c.total_spend
FROM customer_totals c, overall_average a
WHERE c.total_spend > a.avg_spend
ORDER BY c.total_spend DESC;