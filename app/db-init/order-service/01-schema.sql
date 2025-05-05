CREATE TYPE order_status AS ENUM ('PENDING', 'CONFIRMED', 'FAILED');

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        product_id BIGINT NOT NULL,
                        quantity INT NOT NULL CHECK (quantity > 0),
                        status order_status NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
