CREATE TABLE IF NOT EXISTS horoscope_requests (
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    characteristic VARCHAR(500) NOT NULL,
    tone VARCHAR(50),
    formality VARCHAR(50),
    absurdity_level INTEGER,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    general_forecast TEXT,
    career_block TEXT,
    dangerous_days TEXT,
    what_not_to_do TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMP,
    CONSTRAINT pk_horoscope_requests PRIMARY KEY (id),
    CONSTRAINT fk_horoscope_requests_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_horoscope_requests_absurdity CHECK (absurdity_level >= 0 AND absurdity_level <= 100),
    CONSTRAINT chk_horoscope_requests_status CHECK (status IN ('DRAFT', 'COMPLETED', 'SAVED', 'FAILED')),
    CONSTRAINT chk_horoscope_requests_tone CHECK (tone IN ('sarcastic', 'motivational', 'mystical', 'serious')),
    CONSTRAINT chk_horoscope_requests_formality CHECK (formality IN ('low', 'medium', 'high'))
);

CREATE INDEX idx_horoscope_requests_user_id ON horoscope_requests(user_id);
CREATE INDEX idx_horoscope_requests_status ON horoscope_requests(status);
CREATE INDEX idx_horoscope_requests_created_at ON horoscope_requests(created_at DESC);

