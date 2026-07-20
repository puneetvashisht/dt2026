-- role based access
-- create current_db

-- Connect as a superuser/admin
CREATE TABLE financials (
    id SERIAL PRIMARY KEY,
    quarter VARCHAR(10),
    revenue NUMERIC(12, 2),
    confidential_notes TEXT
);

INSERT INTO financials (quarter, revenue, confidential_notes) VALUES
('Q1-2026', 1500000.00, 'Projected growth beat by 5%'),
('Q2-2026', 1750000.00, 'Acquisition costs tracking higher');

-- 1. Create a limited access user account
CREATE ROLE data_analyst WITH LOGIN PASSWORD 'SecurePass123!';

-- 2. Grant connection privileges to the database
GRANT CONNECT ON DATABASE current_db TO data_analyst;
GRANT USAGE ON SCHEMA public TO data_analyst;

-- 3. Grant explicit read-only access to our table
GRANT SELECT ON financials TO data_analyst;


REVOKE SELECT ON financials FROM data_analyst;




 --- row level


 CREATE TABLE sales_leads (
    id SERIAL PRIMARY KEY,
    lead_name VARCHAR(100),
    region VARCHAR(50),
    deal_value NUMERIC(10,2)
);

INSERT INTO sales_leads (lead_name, region, deal_value) VALUES
('Acme Corp', 'US-EAST', 50000.00),
('Globex Corp', 'US-WEST', 75000.00),
('Initech LLC', 'US-EAST', 20000.00);

-- 1. Create regional roles
CREATE ROLE us_east_manager WITH LOGIN PASSWORD 'EastPass!';
GRANT SELECT ON sales_leads TO us_east_manager;

-- 2. Turn on RLS for this table (CRITICAL STEP)
ALTER TABLE sales_leads ENABLE ROW LEVEL SECURITY;

-- 3. Define the security policy
CREATE POLICY region_isolation_policy ON sales_leads
    FOR SELECT
    TO us_east_manager
    USING (region = 'US-EAST');



-- Swapping context to test as the manager
SET ROLE us_east_manager;
SELECT * FROM sales_leads;