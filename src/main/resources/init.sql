CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     fio VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT,
                                          role_id BIGINT,
                                          PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
    );

CREATE TABLE IF NOT EXISTS products (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        category VARCHAR(255) NOT NULL,
    model_name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    loan_amount DOUBLE,
    redemption_date DATE,
    confiscation_date DATE,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS price_history (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             old_price DOUBLE NOT NULL,
                                             new_price DOUBLE NOT NULL,
                                             changed_by BIGINT,
                                             product_id BIGINT,
                                             FOREIGN KEY (changed_by) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
    );

INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

INSERT INTO users (fio, phone, email, password) VALUES
                                                    ('Admin Adminovich', '1234567890', 'admin@example.com', '$2a$10$2jQ4Z6Z8Z8Z8Z8Z8Z8Z8ZuZ8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8'),
                                                    ('User Userovich', '0987654321', 'user@example.com', '$2a$10$2jQ4Z6Z8Z8Z8Z8Z8Z8Z8ZuZ8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8');

INSERT INTO user_roles (user_id, role_id) VALUES
                                              ((SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM roles WHERE name = 'ADMIN')),
                                              ((SELECT id FROM users WHERE email = 'user@example.com'), (SELECT id FROM roles WHERE name = 'USER'));

INSERT INTO products (category, model_name, price, status, loan_amount, redemption_date, user_id) VALUES
                                                                                                      ('Electronics', 'Smartphone', 500.0, 'PAWNED', 350.0, DATE_SUB(CURDATE(), INTERVAL 5 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Electronics', 'Laptop', 1500.0, 'FOR_SALE', 1050.0, DATE_ADD(CURDATE(), INTERVAL 30 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Home Appliances', 'Microwave', 200.0, 'PAWNED', 140.0, DATE_SUB(CURDATE(), INTERVAL 2 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Home Appliances', 'Refrigerator', 800.0, 'SOLD', 560.0, DATE_ADD(CURDATE(), INTERVAL 15 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Furniture', 'Sofa', 1200.0, 'CONFISCATED', 840.0, DATE_SUB(CURDATE(), INTERVAL 10 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Furniture', 'Desk', 400.0, 'EVALUATION', 280.0, DATE_ADD(CURDATE(), INTERVAL 7 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Clothing', 'Jacket', 150.0, 'FOR_SALE', 105.0, DATE_ADD(CURDATE(), INTERVAL 20 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Clothing', 'Jeans', 100.0, 'PAWNED', 70.0, DATE_SUB(CURDATE(), INTERVAL 3 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Jewelry', 'Gold Ring', 2500.0, 'SOLD', 1750.0, DATE_ADD(CURDATE(), INTERVAL 60 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Jewelry', 'Silver Necklace', 500.0, 'CONFISCATED', 350.0, DATE_SUB(CURDATE(), INTERVAL 7 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Books', 'Science Fiction Book', 30.0, 'FOR_SALE', 21.0, DATE_ADD(CURDATE(), INTERVAL 10 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Books', 'Cookbook', 25.0, 'EVALUATION', 17.5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Sports Equipment', 'Treadmill', 1000.0, 'PAWNED', 700.0, DATE_SUB(CURDATE(), INTERVAL 1 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Sports Equipment', 'Yoga Mat', 50.0, 'FOR_SALE', 35.0, DATE_ADD(CURDATE(), INTERVAL 15 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Toys', 'Toy Car', 20.0, 'EVALUATION', 14.0, DATE_ADD(CURDATE(), INTERVAL 3 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Toys', 'Doll', 15.0, 'PAWNED', 10.5, DATE_SUB(CURDATE(), INTERVAL 4 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Music Instruments', 'Guitar', 600.0, 'FOR_SALE', 420.0, DATE_ADD(CURDATE(), INTERVAL 25 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Music Instruments', 'Piano', 3000.0, 'CONFISCATED', 2100.0, DATE_SUB(CURDATE(), INTERVAL 20 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Art', 'Painting', 2000.0, 'SOLD', 1400.0, DATE_ADD(CURDATE(), INTERVAL 90 DAY), (SELECT id FROM users WHERE email = 'user@example.com')),
                                                                                                      ('Art', 'Sculpture', 5000.0, 'EVALUATION', 3500.0, DATE_ADD(CURDATE(), INTERVAL 14 DAY), (SELECT id FROM users WHERE email = 'user@example.com'));

INSERT INTO price_history (old_price, new_price, changed_by, product_id) VALUES
                                                                             (450.0, 500.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Smartphone')),
                                                                             (1350.0, 1500.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Laptop')),
                                                                             (180.0, 200.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Microwave')),
                                                                             (720.0, 800.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Refrigerator')),
                                                                             (1080.0, 1200.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Sofa')),
                                                                             (360.0, 400.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Desk')),
                                                                             (135.0, 150.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Jacket')),
                                                                             (90.0, 100.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Jeans')),
                                                                             (2250.0, 2500.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Gold Ring')),
                                                                             (450.0, 500.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Silver Necklace')),
                                                                             (27.0, 30.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Science Fiction Book')),
                                                                             (22.5, 25.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Cookbook')),
                                                                             (900.0, 1000.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Treadmill')),
                                                                             (45.0, 50.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Yoga Mat')),
                                                                             (18.0, 20.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Toy Car')),
                                                                             (13.5, 15.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Doll')),
                                                                             (540.0, 600.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Guitar')),
                                                                             (2700.0, 3000.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Piano')),
                                                                             (1800.0, 2000.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Painting')),
                                                                             (4500.0, 5000.0, (SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM products WHERE model_name = 'Sculpture'));
