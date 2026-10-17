ALTER TABLE servicos
    ADD COLUMN descricao VARCHAR(1000);

UPDATE servicos
SET descricao = 'Serviço cadastrado anteriormente; entre em contato com o salão para obter mais detalhes.'
WHERE descricao IS NULL;

ALTER TABLE servicos
    ALTER COLUMN descricao SET NOT NULL;
