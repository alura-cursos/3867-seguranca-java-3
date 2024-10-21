ALTER TABLE usuarios
ADD COLUMN refresh_token VARCHAR(64),
ADD COLUMN expiracao_refresh_token TIMESTAMP;