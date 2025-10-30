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
  rotina BIT NOT NULL,
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
  FOREIGN KEY (id_paciente) REFERENCES paciente(id)
);

CREATE TABLE IF NOT EXISTS medicamento (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  dosagem FLOAT NOT NULL, -- DOSAGEM É EM MG OU ML, LEMBRAR DESSA BOMBA
  periodo_uso VARCHAR(100) NOT NULL,
  id_receita INT NOT NULL,
  FOREIGN KEY (id_receita) REFERENCES receita(id)
);

INSERT INTO paciente (nome, email, senha, cpf, data_nasc, telefone, complemento) VALUES
  ('Gabriel Morandim Rodrigues', 'gabriel@gmail.com', 'senha', '42178693806', '2005-10-26', '19 99847-4687', 'Tenho alergia a ignorância do saber, um sintoma crônico de quem percebe a apatia intelectual como a mais grave das condições preexistentes e cujo único prognóstico aceitável é a busca incessante pela lucidez.'),
  ('Igor Cremasco Viotto', 'igor@gmail.com', '123456', '48478291822', '2006-09-02', null, 'Observo a certeza cega como uma patologia contagiosa da alma, por isso cultivo uma imunidade ativa contra dogmas, preferindo os efeitos colaterais da dúvida constante à falsa segurança de uma verdade que não respira'),
  ('Felipe Perez Marine', 'felipe@gmail.com', '123456', '12345678912', '2003-10-15', null, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua');

INSERT INTO medico (nome, email, senha, especialidade) VALUES
  ('Dr. Jascinto Pinto de Oliveira', 'jascinto@gmail.com', '123456', 'Pediatra'),
  ('Dra. Paula Vadinho Santana',     'paula@gmail.com',    '123456', 'Urologista'),
  ('Dr. Sujiro Kimimame Nakamura',   'sujiro@gmail.com',   '123456', 'Clínico Geral'),
  ('Dra. Eva Gina de Souza',         'eva@gmail.com',      '123456', 'Neurologista'),
  ('Dr. Takamassa Numuro',           'takamassa@gmail.com','123456', 'Ortopedista');

INSERT INTO consulta (data, valor, pago, rotina, sintomas, status, id_paciente, id_medico) VALUES
  (TIMESTAMP '2025-11-03 08:00:00', 325.00, TRUE,  FALSE, 'Gripe comum',            'CONFIRMADA', 3, 3),
  (TIMESTAMP '2025-11-03 13:45:00', 550.00, FALSE, FALSE, 'Filho com conjuntivite', 'CONFIRMADA', 3, 1),
  (TIMESTAMP '2025-11-07 15:30:00', 375.00, FALSE, TRUE,  'Impotência sexual',      'PENDENTE',   3, 2),
  (TIMESTAMP '2025-11-03 09:30:00', 250.00, TRUE,  TRUE,  'Dor no joelho',          'CONFIRMADA', 2, 5),
  (TIMESTAMP '2025-11-04 08:00:00', 425.00, TRUE,  FALSE, 'Dor de cabeça',          'CONFIRMADA', 2, 4),
  (TIMESTAMP '2025-11-03 10:00:00', 187.00, TRUE,  TRUE,  'Dor na coluna',          'CONFIRMADA', 1, 5),
  (TIMESTAMP '2025-11-04 09:30:00', 225.00, FALSE, FALSE, 'Dor ao urinar',          'PENDENTE',   1, 2),
  (TIMESTAMP '2025-11-06 13:30:00', 350.00, FALSE, TRUE,  'Memória curta',          'PENDENTE',   1, 4);