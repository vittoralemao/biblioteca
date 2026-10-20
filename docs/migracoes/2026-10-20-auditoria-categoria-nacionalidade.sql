BEGIN;

-- 1. Cria as colunas SEM obrigatoriedade (as linhas antigas ficam com NULL)
ALTER TABLE categorias     ADD COLUMN IF NOT EXISTS data_cadastro    timestamp(6);
ALTER TABLE categorias     ADD COLUMN IF NOT EXISTS data_atualizacao timestamp(6);
ALTER TABLE nacionalidades ADD COLUMN IF NOT EXISTS data_cadastro    timestamp(6);
ALTER TABLE nacionalidades ADD COLUMN IF NOT EXISTS data_atualizacao timestamp(6);

-- 2. Preenche as linhas antigas
UPDATE categorias     SET data_cadastro = now(), data_atualizacao = now() WHERE data_cadastro IS NULL;
UPDATE nacionalidades SET data_cadastro = now(), data_atualizacao = now() WHERE data_cadastro IS NULL;

-- 3. Agora sim, torna obrigatório
ALTER TABLE categorias     ALTER COLUMN data_cadastro    SET NOT NULL;
ALTER TABLE categorias     ALTER COLUMN data_atualizacao SET NOT NULL;
ALTER TABLE nacionalidades ALTER COLUMN data_cadastro    SET NOT NULL;
ALTER TABLE nacionalidades ALTER COLUMN data_atualizacao SET NOT NULL;

COMMIT;