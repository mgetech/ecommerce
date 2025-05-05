CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description TEXT NOT NULL,
                         price DOUBLE PRECISION NOT NULL CHECK (price > 0)
);
