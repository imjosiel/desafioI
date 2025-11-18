ALTER TABLE IF EXISTS pedido ADD COLUMN IF NOT EXISTS data_criacao timestamp(6);
UPDATE pedido SET data_criacao = NOW() WHERE data_criacao IS NULL;
ALTER TABLE pedido ALTER COLUMN data_criacao SET NOT NULL;