-- Create database
CREATE DATABASE IF NOT EXISTS posdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE posdb;

-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50) UNIQUE NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    reset_token     VARCHAR(255),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- Create Products table
CREATE TABLE IF NOT EXISTS products (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    price           DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    category        VARCHAR(50),
    supplier        VARCHAR(100),
    stock           INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_category (category)
);

-- Create Inventory Transactions table
CREATE TABLE IF NOT EXISTS inventory_transactions (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    product_id      INT NOT NULL,
    user_id         INT NOT NULL,
    type            VARCHAR(20) NOT NULL CHECK (type IN ('RESTOCK', 'SALE', 'ADJUST')),
    quantity        INT NOT NULL CHECK (quantity != 0),
    notes           TEXT,
    trans_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_product_id (product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_trans_at (trans_at),
    INDEX idx_product_user (product_id, user_id)
);
