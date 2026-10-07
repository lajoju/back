CREATE TABLE agendamentos (
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL,
    funcionario_id UUID NOT NULL,
    servico_id UUID NOT NULL,
    data DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    CHECK (hora_fim > hora_inicio)
);

CREATE INDEX idx_agendamentos_funcionario_periodo
    ON agendamentos (funcionario_id, data, hora_inicio, hora_fim);
