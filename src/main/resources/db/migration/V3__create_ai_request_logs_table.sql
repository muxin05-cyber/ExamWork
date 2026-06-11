CREATE TABLE IF NOT EXISTS ai_request_logs (
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    request_type VARCHAR(50) NOT NULL,
    request_prompt TEXT,
    response_summary TEXT,
    tokens_used INTEGER DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_ai_request_logs PRIMARY KEY (id),
    CONSTRAINT fk_ai_request_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_ai_request_logs_status CHECK (status IN ('SUCCESS', 'FAILED')),
    CONSTRAINT chk_ai_request_logs_type CHECK (request_type IN ('TOKEN', 'GENERATE'))
);

CREATE INDEX idx_ai_request_logs_user_id ON ai_request_logs(user_id);
CREATE INDEX idx_ai_request_logs_created_at ON ai_request_logs(created_at DESC);