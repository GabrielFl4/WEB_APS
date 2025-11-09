CREATE SCHEMA IF NOT EXISTS databaseBonitinho;
SET SCHEMA databaseBonitinho;

CREATE TABLE IF NOT EXISTS paciente (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  email VARCHAR(200) NOT NULL,
  senha VARCHAR(20),
  cpf VARCHAR(11),
  data_nasc DATE,
  telefone VARCHAR(20),
  complemento VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS medico (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  email VARCHAR(200) NOT NULL,
  senha VARCHAR(20),
  especialidade VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS consulta (
  id INT AUTO_INCREMENT PRIMARY KEY,
  data TIMESTAMP NOT NULL,
  valor FLOAT NOT NULL,
  pago BIT NOT NULL, -- PAGA | PENDENTE
  rotina VARCHAR(20) NOT NULL, -- RETORNO | CONSULTA_INICIAL | EXAMES
  sintomas VARCHAR(200),
  status VARCHAR(10) NOT NULL, -- CONFIRMADA | PENDENTE | CANCELADA | REALIZADA
  id_paciente INT NOT NULL,
  id_medico INT NOT NULL,
  FOREIGN KEY (id_paciente) REFERENCES paciente(id),
  FOREIGN KEY (id_medico) REFERENCES medico(id)
);

CREATE TABLE IF NOT EXISTS receita (
  id INT AUTO_INCREMENT PRIMARY KEY,
  data DATE NOT NULL,
  id_paciente INT NOT NULL,
  id_medico INT NOT NULL,
  FOREIGN KEY (id_paciente) REFERENCES paciente(id),
  FOREIGN KEY (id_medico) REFERENCES medico(id)
);

CREATE TABLE IF NOT EXISTS medicamento (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  dosagem VARCHAR(250) NOT NULL,
  id_receita INT NOT NULL,
  FOREIGN KEY (id_receita) REFERENCES receita(id)
);

-- Crio index para manter unico o email e evitar repetições.
CREATE UNIQUE INDEX IF NOT EXISTS uq_paciente_email ON paciente(email);
CREATE UNIQUE INDEX IF NOT EXISTS uq_medico_email   ON medico(email);
CREATE UNIQUE INDEX IF NOT EXISTS uq_consulta_horario
  ON consulta(data, id_paciente, id_medico);
CREATE UNIQUE INDEX IF NOT EXISTS uq_medicamento_receita_nome_dose
  ON medicamento (id_receita, nome, dosagem);

MERGE INTO paciente (nome, email, senha, cpf, data_nasc, telefone, complemento)
KEY (email) VALUES
  ('Gabriel Morandim Rodrigues', 'gabriel@gmail.com', 'senha', '42178693806', '2005-10-26', '19 99847-4687', 'Tenho alergia a ignorância do saber, um sintoma crônico de quem percebe a apatia intelectual como a mais grave das condições preexistentes e cujo único prognóstico aceitável é a busca incessante pela lucidez.'),
  ('Igor Cremasco Viotto', 'igor@gmail.com', '123456', '48478291822', '2006-09-02', null, 'Observo a certeza cega como uma patologia contagiosa da alma, por isso cultivo uma imunidade ativa contra dogmas, preferindo os efeitos colaterais da dúvida constante à falsa segurança de uma verdade que não respira'),
  ('Felipe Perez Marine', 'felipe@gmail.com', '123456', '12345678912', '2003-10-15', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua');

MERGE INTO medico (nome, email, senha, especialidade)
KEY (email) VALUES
  ('Dr. Jascinto Pinto de Oliveira', 'jascinto@gmail.com', '123456', 'Pediatra'),
  ('Dra. Paula Vadinho Santana',     'paula@gmail.com',    '123456', 'Urologista'),
  ('Dr. Sujiro Kimimame Nakamura',   'sujiro@gmail.com',   '123456', 'Clínico Geral'),
  ('Dra. Eva Gina de Souza',         'eva@gmail.com',      '123456', 'Neurologista'),
  ('Dr. Takamassa Numuro',           'takamassa@gmail.com','123456', 'Ortopedista');

MERGE INTO consulta (data, valor, pago, rotina, sintomas, status, id_paciente, id_medico)
KEY (data, id_paciente, id_medico) VALUES
  (TIMESTAMP '2025-11-03 08:00:00', 325.00, TRUE,  'CONSULTA_INICIAL', 'Gripe comum',            'CONFIRMADA', 3, 3),
  (TIMESTAMP '2025-10-31 13:45:00', 550.00, FALSE, 'CONSULTA_INICIAL', 'Filho com conjuntivite', 'CONFIRMADA', 3, 1),
  (TIMESTAMP '2025-11-07 15:30:00', 375.00, TRUE, 'RETORNO',  'Impotência sexual',      'PENDENTE',   3, 2),
  (TIMESTAMP '2025-11-03 09:30:00', 250.00, TRUE,  'RETORNO',  'Dor no joelho',          'CONFIRMADA', 2, 5),
  (TIMESTAMP '2025-11-04 08:00:00', 425.00, TRUE,  'EXAMES', 'Dor de cabeça',          'CONFIRMADA', 2, 4),
  (TIMESTAMP '2025-11-03 10:00:00', 187.00, TRUE,  'RETORNO',  'Dor na coluna',          'CONFIRMADA', 1, 5),
  (TIMESTAMP '2025-11-04 09:30:00', 225.00, FALSE, 'EXAMES', 'Dor ao urinar',          'PENDENTE',   1, 2),
  (TIMESTAMP '2025-11-06 13:30:00', 350.00, TRUE, 'RETORNO',  'Memória curta',          'PENDENTE',   1, 4),
  (TIMESTAMP '2025-11-03 10:00:00', 265.00, TRUE,  'RETORNO',  'Teste Retorno 1',          'CONFIRMADA', 1, 1),
    (TIMESTAMP '2025-11-04 09:30:00', 225.00, FALSE, 'EXAMES', 'Teste Exames 2',          'CONFIRMADA',   1, 1),
    (TIMESTAMP '2025-11-06 13:30:00', 350.00, FALSE, 'CONSULTA_INICIAL',  'Teste Inicial 3',          'CONFIRMADA',   1, 1);

MERGE INTO receita (id, data, id_paciente, id_medico) KEY (id) VALUES
  (1, '2025-10-10', 1, 1),
  (2, '2025-11-01', 1, 4),
  (3, '2025-11-03', 3, 5);


MERGE INTO medicamento (nome, dosagem, id_receita)
KEY (id_receita, nome, dosagem) VALUES
  ('Amoxicilina',        '500 mg - 1 cápsula a cada 8h por 7 dias',         1),
  ('Ibuprofeno',         '400 mg - 1 comprimido a cada 8h se dor/febre',    1),
  ('Omeprazol',          '20 mg - 1 cápsula em jejum por 14 dias',          1),
  ('Paracetamol',        '750 mg - 1 comprimido a cada 6h se febre',        2),
  ('Dipirona',           '500 mg - 1 comprimido a cada 6h se dor/febre',    2),
  ('Soro fisiológico',   '5 mL por nebulização - 3x/dia por 5 dias',        2),
  ('Losartana',          '50 mg - 1 comprimido 1x/dia',                     3),
  ('Hidroclorotiazida',  '25 mg - 1 comprimido pela manhã',                 3),
  ('Atorvastatina',      '20 mg - 1 comprimido à noite',                    3);
