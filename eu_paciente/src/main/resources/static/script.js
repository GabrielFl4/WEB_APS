// ==========================================================
// 1. DECLARAÇÃO DAS CONSTANTES GLOBAIS
// ==========================================================
let medicamentosDaReceitaAtual = [];

// Elementos da Receita
const btnAddMedicamento = document.getElementById('btn-add-medicamento');
const listaMedicamentosAtualUI = document.getElementById('lista-medicamentos-atual');
const inputCpf = document.getElementById('input-cpf');
const inputPacienteId = document.getElementById('input-paciente-id');
const inputPacienteNome = document.getElementById('input-paciente-nome');
const inputMedicamento = document.getElementById('input-medicamento');
const inputObservacoes = document.getElementById('input-observacoes');
const formReceita = document.getElementById('form-receita');
const listaReceitas = document.querySelector("#lista-receitas ul");

// Elementos Gerais
const tituloPagina = document.getElementById('titulo-pagina');
const botaoSair = document.querySelector('.logout');

// Elementos do Dashboard
const cardConsultas = document.getElementById('card-consultas');
const cardReceitas = document.getElementById('card-receitas');
const cardGanhos = document.getElementById('card-ganhos');

// Elementos da Agenda
const tabelaAgenda = document.getElementById('tabela-agenda');

// === NOVAS CONSTANTES (Ficha Médica) ===
const fichaInputCpf = document.getElementById('ficha-input-cpf');
const fichaBtnBuscar = document.getElementById('ficha-btn-buscar');
const fichaResultadoContainer = document.getElementById('ficha-resultado-container');


// ==========================================================
// 2. FUNÇÕES DE RENDERIZAÇÃO E API (API CALLS)
// ==========================================================

// --- FUNÇÃO HELPER (Receitas) ---
function renderMedicamentosAtuais() {
    // Limpa a lista
    listaMedicamentosAtualUI.innerHTML = '';

    if (medicamentosDaReceitaAtual.length === 0) {
        listaMedicamentosAtualUI.innerHTML = '<p>Nenhum medicamento adicionado.</p>';
        return;
    }

    // Adiciona cada medicamento na UI
    medicamentosDaReceitaAtual.forEach((med, index) => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'medicamento-item-novo';

        itemDiv.innerHTML = `
            <strong>${med.nome}</strong>
            <p>${med.dosagem}</p>
            <button type="button" class="btn-remover-med" data-index="${index}">Remover</button>
        `;

        listaMedicamentosAtualUI.appendChild(itemDiv);
    });
}

// --- FUNÇÃO HELPER (Ficha Médica) ---
function calcularIdade(dataNasc) {
    if (!dataNasc) return 'N/A';
    const hoje = new Date();
    const nasc = new Date(dataNasc);
    let idade = hoje.getFullYear() - nasc.getFullYear();
    const m = hoje.getMonth() - nasc.getMonth();
    if (m < 0 || (m === 0 && hoje.getDate() < nasc.getDate())) {
        idade--;
    }
    return idade;
}

// --- CARREGA OS DADOS DO DASHBOARD ---
async function carregarDashboard(){

    const idMedicoLogado = sessionStorage.getItem('idMedicoLogado');

    if(!idMedicoLogado){
        cardConsultas.textContent = 'Erro de ID';
        cardReceitas.textContent = 'Erro de ID';
        return;
    }

    // --- Bloco para Consultas ---
    try {
        const responseConsultas = await fetch(`http://localhost:8080/api/resumo/consultas-hoje/${idMedicoLogado}`);
        if (!responseConsultas.ok) {
            throw new Error('Falha ao buscar total de consultas');
        }
        const totalConsultas = await responseConsultas.json();
        cardConsultas.textContent = totalConsultas;

    } catch (error) {
        console.error('Erro ao carregar card de consultas:', error);
        cardConsultas.textContent = 'Erro';
    }

    // --- Bloco para Receitas ---
    try {
        const responseReceitas = await fetch(`http://localhost:8080/api/resumo/receitas-hoje/${idMedicoLogado}`);
        if (!responseReceitas.ok) {
            throw new Error('Falha ao buscar total de receitas');
        }
        const totalReceitas = await responseReceitas.json();
        cardReceitas.textContent = totalReceitas;

    } catch (error) {
        console.error('Erro ao carregar card de receitas:', error);
        cardReceitas.textContent = 'Erro';
    }

    // Ganhos (ainda por fazer)
    cardGanhos.textContent = 'N/A';
}

// --- CARREGA A AGENDA DO DIA ---
async function carregarAgenda() {
    tabelaAgenda.innerHTML = `<tr><td colspan="4">Carregando agenda...</td></tr>`;

    // 1. CORREÇÃO: Pega o ID do médico que fez o login
    const idMedicoLogado = sessionStorage.getItem('idMedicoLogado');

    // 2. Validação
    if (!idMedicoLogado) {
        tabelaAgenda.innerHTML = `<tr><td colspan="4">Erro: Médico não logado.</td></tr>`;
        return;
    }

    try {
        // 3. Usa o ID dinâmico na chamada da API
        const response = await fetch(`http://localhost:8080/api/consultas/medico/${idMedicoLogado}/hoje`);

        if (!response.ok) throw new Error('Erro ao buscar consultas.');

        const consultas = await response.json();

        tabelaAgenda.innerHTML = ''; // Limpa a tabela

        if (consultas.length === 0) {
            tabelaAgenda.innerHTML = `<tr><td colspan="4">Nenhuma consulta para hoje.</td></tr>`;
            return;
        }

        // 4. Preenche a tabela com os dados
        consultas.forEach(consulta => {
            const linha = tabelaAgenda.insertRow();

            const horaFormatada = new Date(consulta.dataHora).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });

            linha.insertCell(0).textContent = horaFormatada;
            linha.insertCell(1).textContent = consulta.paciente ? consulta.paciente.nome : 'Paciente não informado';
            linha.insertCell(2).textContent = consulta.statusMotivo ? consulta.statusMotivo.replace('_', ' ') : '';

            const cellStatus = linha.insertCell(3);
            const selectStatus = document.createElement('select');
            selectStatus.className = 'status-select';
            selectStatus.dataset.consultaId = consulta.id;

            const statusOpcoes = ['PENDENTE', 'CONFIRMADA', 'CANCELADA', 'REALIZADA', 'AGENDADA'];
            statusOpcoes.forEach(opt => {
                const option = document.createElement('option');
                option.value = opt;
                option.textContent = opt;
                if (opt === consulta.statusAndamento) {
                    option.selected = true;
                }
                selectStatus.appendChild(option);
            });
            cellStatus.appendChild(selectStatus);
        });
    } catch (error) {
        console.error("Erro ao carregar agenda:", error);
        tabelaAgenda.innerHTML = `<tr><td colspan="4">Não foi possível carregar a agenda.</td></tr>`;
    }
}


// --- ATUALIZA O STATUS DA CONSULTA ---
async function atualizarStatusConsulta(consultaId, novoStatus) {
    try {
        const response = await fetch(`http://localhost:8080/api/consultas/${consultaId}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: novoStatus })
        });
        if (!response.ok) throw new Error('Falha ao atualizar status.');
        alert('Status atualizado com sucesso!');
    } catch (error) {
        console.error("Erro ao atualizar status:", error);
        alert('Não foi possível atualizar o status.');
    }
}


// --- CARREGA AS RECEITAS EMITIDAS ---
async function carregarReceitas() {
  listaReceitas.innerHTML = '<li>Carregando...</li>';

  const idMedicoLogado = sessionStorage.getItem('idMedicoLogado');
  if (!idMedicoLogado) {
      listaReceitas.innerHTML = '<li>Erro: Médico não logado.</li>';
      return;
  }

  try {
      const response = await fetch(`http://localhost:8080/api/receitas/medico/${idMedicoLogado}`);
      if (!response.ok) throw new Error('Falha ao buscar receitas.');

      const dados = await response.json();
      listaReceitas.innerHTML = '';

      if (dados.length === 0) {
              listaReceitas.innerHTML = '<li>Nenhuma receita emitida.</li>';
              return;
      }

      dados.forEach(receita => {
          const item = document.createElement("li");

          let nomeMedicamento = "Receita sem medicamentos";
          if (receita.medicamentos && receita.medicamentos.length > 0) {
              nomeMedicamento = receita.medicamentos[0].nome;
              if (receita.medicamentos.length > 1) {
                nomeMedicamento += ` (e mais ${receita.medicamentos.length - 1})`;
              }
          }

          item.innerHTML = `
            <span>Paciente: ${receita.paciente.nome} - ${nomeMedicamento}</span>
            <button class="btn-apagar-receita" data-id="${receita.id}">Apagar 🗑️</button>
          `;

          listaReceitas.appendChild(item);
      });
  } catch (error) {
      console.error("Erro ao carregar receitas:", error);
      listaReceitas.innerHTML = '<li>Erro ao carregar receitas.</li>';
  }
}

// --- APAGA UMA RECEITA ---
async function apagarReceita(idReceita) {
    if (!confirm('Tem certeza que deseja apagar esta receita? Esta ação não pode ser desfeita.')) {
        return;
    }
    try {
        const response = await fetch(`http://localhost:8080/api/receitas/${idReceita}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('Falha ao apagar a receita.');
        }
        alert('Receita apagada com sucesso!');
        carregarReceitas();
    } catch (error) {
        console.error("Erro ao apagar receita:", error);
        alert("Não foi possível apagar a receita.");
    }
}

// --- BUSCA PACIENTE (USADO PELA ABA RECEITA E FICHA) ---
async function buscarPacientePorCpf(cpf) {
    if (cpf.length !== 11) return;

    // Atualiza os campos de ambas as abas, se existirem
    if (inputPacienteNome) inputPacienteNome.value = 'Buscando...';
    if (inputPacienteId) inputPacienteId.value = '';

    try {
        const response = await fetch(`http://localhost:8080/api/pacientes/cpf/${cpf}`);

        if (response.ok) {
            const paciente = await response.json();
            if (inputPacienteNome) inputPacienteNome.value = paciente.nome;
            if (inputPacienteId) inputPacienteId.value = paciente.id;
            return paciente; // Retorna o objeto paciente (importante para a Ficha)
        } else {
            if (inputPacienteNome) inputPacienteNome.value = 'Paciente não encontrado';
            alert('CPF não encontrado no sistema.');
            return null;
        }
    } catch (error) {
        console.error("Erro ao buscar paciente:", error);
        if (inputPacienteNome) inputPacienteNome.value = 'Erro de comunicação';
        return null;
    }
}


// --- (NOVA) BUSCA E EXIBE A FICHA MÉDICA ---
async function buscarEExibirFicha() {
    const cpf = fichaInputCpf.value.replace(/[^0-9]/g, '');
    if (cpf.length !== 11) {
        alert('Por favor, digite um CPF válido.');
        return;
    }

    fichaResultadoContainer.innerHTML = '<p>Buscando...</p>';
    fichaResultadoContainer.style.display = 'block';

    try {
        // 1. Busca o paciente pelo CPF (função que já temos!)
        const pacienteBasico = await buscarPacientePorCpf(cpf);
        if (!pacienteBasico || !pacienteBasico.id) {
            fichaResultadoContainer.innerHTML = '<p>Paciente não encontrado.</p>';
            return;
        }

        // 2. Busca a FICHA COMPLETA usando o ID (novo endpoint!)
        const responseFicha = await fetch(`http://localhost:8080/api/pacientes/ficha/${pacienteBasico.id}`);
        if (!responseFicha.ok) {
            throw new Error('Não foi possível carregar a ficha completa.');
        }

        const paciente = await responseFicha.json(); // Este é o objeto Paciente completo

        // 3. Renderiza os dados na tela
        let idade = calcularIdade(paciente.data_nasc);

        // Constrói o HTML
        let htmlFicha = `
            <h2>Ficha de: ${paciente.nome}</h2>
            <div class="ficha-layout">
                <div class="ficha-coluna">
                    <h3>Dados Pessoais</h3>
                    <div class="ficha-box">
                        <p><strong>Nome:</strong> ${paciente.nome}</p>
                        <p><strong>Idade:</strong> ${idade} anos (Nasc: ${paciente.data_nasc})</p>
                        <p><strong>CPF:</strong> ${paciente.cpf}</p>
                        <p><strong>Email:</strong> ${paciente.email}</p>
                        <p><strong>Telefone:</strong> ${paciente.telefone || 'N/A'}</p>
                        <p><strong>Histórico/Observações:</strong></p>
                        <p>${paciente.complemento || 'Nenhuma observação.'}</p>
                    </div>
                </div>

                <div class="ficha-coluna">
                    <h3>Histórico de Consultas (${paciente.consultas.length})</h3>
                    <div class="ficha-box lista-scroll">
                        ${paciente.consultas.length === 0 ? '<p>Nenhuma consulta encontrada.</p>' : paciente.consultas.map(c => `
                            <div class="item-historico">
                                <strong>Data: ${new Date(c.dataHora).toLocaleDateString('pt-BR')}</strong>
                                <p>Médico: ${c.medico.nome}</p>
                                <p>Sintomas: ${c.sintomas}</p>
                                <p>Status: ${c.statusAndamento}</p>
                            </div>
                        `).join('')}
                    </div>

                    <h3>Histórico de Receitas (${paciente.receitas.length})</h3>
                    <div class="ficha-box lista-scroll">
                        ${paciente.receitas.length === 0 ? '<p>Nenhuma receita encontrada.</p>' : paciente.receitas.map(r => `
                            <div class="item-historico">
                                <strong>Data: ${new Date(r.data).toLocaleDateString('pt-BR')}</strong>
                                <p>Médico: ${r.medico.nome}</p>
                                <ul>
                                    ${r.medicamentos.map(m => `<li>${m.nome} (${m.dosagem})</li>`).join('')}
                                </ul>
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        `;

        fichaResultadoContainer.innerHTML = htmlFicha;

    } catch (error) {
        console.error("Erro ao carregar ficha:", error);
        fichaResultadoContainer.innerHTML = `<p style="color: red;">${error.message}</p>`;
    }
}


// ==========================================================
// 3. LÓGICA DE EVENTOS (Cliques, Envios de Formulário)
// ==========================================================

// --- CONTROLA A NAVEGAÇÃO ENTRE AS TELAS ---
function mostrarTela(id) {
  document.querySelectorAll(".conteudo").forEach(t => t.classList.remove("ativo"));
  const novaTela = document.getElementById(id);
  if (novaTela) { // Adiciona verificação
      novaTela.classList.add("ativo");
      tituloPagina.textContent = id.charAt(0).toUpperCase() + id.slice(1);
  } else {
      console.error("Não foi encontrada a tela com ID:", id);
  }

  // A MÁGICA ESTÁ AQUI: Ao mostrar uma tela, carrega os dados dela
  switch(id) {
    case 'dashboard':
      carregarDashboard();
      break;
    case 'agenda':
      carregarAgenda();
      break;
    case 'receitas':
      carregarReceitas();
      break;
    // Ficha é carregada 'sob demanda' (ao clicar em buscar)
    case 'ficha':
      fichaResultadoContainer.style.display = 'none'; // Esconde resultados antigos
      fichaInputCpf.value = ''; // Limpa o CPF
      break;
  }
}

// --- EVENTOS DA ABA "RECEITAS" ---

// Adicionar Medicamento (Botão Cinza)
btnAddMedicamento?.addEventListener('click', () => {
    const nome = inputMedicamento.value;
    const dosagem = inputObservacoes.value;
    if (!nome || !dosagem) {
        alert('Por favor, preencha o nome do medicamento e a dosagem.');
        return;
    }
    medicamentosDaReceitaAtual.push({ nome: nome, dosagem: dosagem });
    renderMedicamentosAtuais();
    inputMedicamento.value = '';
    inputObservacoes.value = '';
    inputMedicamento.focus();
});

// Remover Medicamento (Botão Vermelho Pequeno)
listaMedicamentosAtualUI?.addEventListener('click', (e) => {
    if (e.target.classList.contains('btn-remover-med')) {
        const indexParaRemover = parseInt(e.target.dataset.index);
        medicamentosDaReceitaAtual.splice(indexParaRemover, 1);
        renderMedicamentosAtuais();
    }
});

// Gerar Receita (Botão Azul)
formReceita?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const idPaciente = inputPacienteId.value;
  const idMedico = sessionStorage.getItem('idMedicoLogado');

  if (!idPaciente) {
        alert("Por favor, busque um paciente pelo CPF primeiro.");
        return;
  }
  if (!idMedico) {
        alert("Erro crítico: Médico não logado.");
        return;
  }
  if (medicamentosDaReceitaAtual.length === 0) {
        alert("Você precisa adicionar pelo menos um medicamento à receita antes de gerá-la.");
        return;
  }

try {
    const novaReceita = {
        id_paciente: parseInt(idPaciente),
        id_medico: parseInt(idMedico),
        medicamentos: medicamentosDaReceitaAtual
    };

    const response = await fetch('http://localhost:8080/api/receitas', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(novaReceita)
    });

    if (!response.ok) {
      throw new Error('Falha ao salvar. O servidor respondeu com um erro.');
    }

    alert("Receita gerada e salva com sucesso!");
    formReceita.reset();
    inputPacienteId.value = '';
    inputPacienteNome.value = '';
    medicamentosDaReceitaAtual = [];
    renderMedicamentosAtuais();
    carregarReceitas();

  } catch (error) {
    console.error("Erro ao gerar receita:", error);
    alert("Não foi possível salvar a receita. Verifique o console.");
  }
});

// Buscar Paciente por CPF (na aba Receitas)
inputCpf?.addEventListener('blur', (e) => {
    const cpf = e.target.value.replace(/[^0-9]/g, '');
    if (cpf.length === 11) {
        buscarPacientePorCpf(cpf);
    }
});

// Apagar Receita (Botão Vermelho na lista de emitidas)
listaReceitas?.addEventListener('click', (e) => {
    if (e.target.classList.contains('btn-apagar-receita')) {
        const idReceita = e.target.dataset.id;
        apagarReceita(idReceita);
    }
});

// --- EVENTOS DA ABA "AGENDA" ---

// Atualizar Status da Consulta
tabelaAgenda.addEventListener('change', (event) => {
  if (event.target.classList.contains('status-select')) {
    const select = event.target;
    const consultaId = select.dataset.consultaId;
    const novoStatus = select.value;
    atualizarStatusConsulta(consultaId, novoStatus);
  }
});


// --- (NOVOS) EVENTOS DA ABA "FICHA MÉDICA" ---

// Ativa a busca ao clicar no botão
fichaBtnBuscar?.addEventListener('click', buscarEExibirFicha);

// (Opcional) Ativa a busca ao pressionar "Enter" no campo
fichaInputCpf?.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        buscarEExibirFicha();
    }
});

// --- EVENTOS GERAIS ---

// Botão Sair
botaoSair?.addEventListener('click', () => {
  // Limpa o storage ao sair
  sessionStorage.removeItem('idMedicoLogado');
  sessionStorage.removeItem('nomeMedicoLogado');

  alert('Você foi desconectado.');
  window.location.href = 'Login.html';
});

// ==========================================================
// 4. INICIALIZAÇÃO DA PÁGINA
// ==========================================================
document.addEventListener('DOMContentLoaded', () => {

  const idMedicoLogado = sessionStorage.getItem('idMedicoLogado');
  const nomeMedicoLogado = sessionStorage.getItem('nomeMedicoLogado');

  if (!idMedicoLogado) {
    alert('Você não está logado. Por favor, faça o login.');
    window.location.href = 'Login.html';
    return;
  }

  // Coloca a mensagem de Boas-Vindas
  const elementoBoasVindas = document.getElementById('boas-vindas');
  if (elementoBoasVindas) {
      elementoBoasVindas.textContent = `Bem-vindo(a), Dr(a). ${nomeMedicoLogado}`;
  } else {
      console.warn("Elemento 'boas-vindas' não encontrado no HTML.");
  }

  // Mostra a tela inicial (Dashboard)
  mostrarTela('dashboard');
});