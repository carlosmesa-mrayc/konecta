--DROP TABLE product_entity;
--DROP TABLE product_sale_entity;
CREATE TABLE IF NOT EXISTS product_entity (id SERIAL PRIMARY KEY, name VARCHAR(255), reference VARCHAR(255), price INTEGER, weight INTEGER, category VARCHAR(255), stock INTEGER, created_date TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_sale_entity (id SERIAL PRIMARY KEY, product_id INTEGER, count INTEGER, created_date TIMESTAMP);