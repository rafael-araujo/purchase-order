CREATE TABLE client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    protocol VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    client_id BIGINT,
    date TIMESTAMP,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE order_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    amount INT,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES purchase_order(id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE reseller (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj BIGINT NOT NULL UNIQUE,
    corporate_name VARCHAR(255) NOT NULL,
    fantasy_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE phone_number (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reseller_id BIGINT NOT NULL,
    phone_number BIGINT,
    CONSTRAINT fk_reseller_phone FOREIGN KEY (reseller_id) REFERENCES reseller(id)
);

CREATE TABLE contact (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reseller_id BIGINT NOT NULL,
    contact_name VARCHAR(255) NOT NULL,
    principal BOOLEAN NOT NULL,
    CONSTRAINT fk_reseller_contact FOREIGN KEY (reseller_id) REFERENCES reseller(id)
);

CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reseller_id BIGINT NOT NULL,
    street VARCHAR(255) NOT NULL,
    number INT NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    CONSTRAINT fk_reseller_address FOREIGN KEY (reseller_id) REFERENCES reseller(id)
);
