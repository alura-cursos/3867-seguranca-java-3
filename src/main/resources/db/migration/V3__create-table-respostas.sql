CREATE TABLE respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem TEXT NOT NULL,
    autor VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    solucao BOOLEAN DEFAULT FALSE,
    topico_id BIGINT,
    CONSTRAINT fk_topico FOREIGN KEY (topico_id) REFERENCES topicos(id) ON DELETE CASCADE
);