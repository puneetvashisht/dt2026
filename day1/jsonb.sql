CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(50) UNIQUE NOT NULL,      -- Rigid column
    price NUMERIC(10, 2) NOT NULL,        -- Rigid column
    status VARCHAR(20) DEFAULT 'active',  -- Rigid column
    attributes JSONB                      -- Flexible document column
);

INSERT INTO products (sku, price, attributes) VALUES
(
    'LAP-MAC-01', 
    1299.99, 
    '{"brand": "Apple", "specs": {"ram": "16GB", "storage": "512GB"}, "tags": ["electronics", "premium"]}'
),
(
    'TSHIRT-RED-M', 
    24.99, 
    '{"brand": "Nike", "color": "Red", "size": "M", "tags": ["apparel", "sportswear"]}'
);

SELECT sku, attributes->>'brand' AS brand, attributes#>>'{specs,ram}' AS ram
FROM products;


EXPLAIN ANALYZE
SELECT * FROM products WHERE attributes @> '{"brand": "Apple"}';

CREATE INDEX idx_products_attributes_default 
ON products USING gin (attributes);

EXPLAIN ANALYZE
SELECT * FROM products WHERE attributes @> '{"brand": "Apple"}';