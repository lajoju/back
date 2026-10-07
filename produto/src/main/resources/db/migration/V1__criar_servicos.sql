CREATE TABLE servicos (
    id UUID PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    tempo_medio_minutos INTEGER NOT NULL CHECK (tempo_medio_minutos > 0),
    preco NUMERIC(10, 2) NOT NULL CHECK (preco >= 0)
);
