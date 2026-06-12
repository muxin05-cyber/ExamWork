CREATE TABLE IF NOT EXISTS users (
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_email CHECK (email LIKE '%_@_%._%'),
    CONSTRAINT chk_users_username CHECK (LENGTH(username) >= 3));

CREATE INDEX idx_users_email ON users(email);
