// ==========================================================
// 1. DECLARAÇÃO DAS CONSTANTES GLOBAIS
// ==========================================================
let medicamentosDaReceitaAtual = [];


const btnAddMedicamento = document.getElementById('btn-add-medicamento');
const listaMedicamentosAtualUI = document.getElementById('lista-medicamentos-atual');
const inputCpf = document.getElementById('input-cpf');
const inputPacienteId = document.getElementById('input-paciente-id');
const inputPacienteNome = document.getElementById('input-paciente-nome');
const inputMedicamento = document.getElementById('input-medicamento');
const inputObservacoes = document.getElementById('input-observacoes');

// Pegamos todos os elementos da página que vamos manipular
const tituloPagina = document.getElementById('titulo-pagina');

const cardConsultas = document.getElementById('card-consultas');
const cardReceitas = document.getElementById('card-receitas');
const cardGanhos = document.getElementById('card-ganhos');

const tabelaAgenda = document.getElementById('tabela-agenda');

const formReceita = document.getElementById('form-receita');
const listaReceitas = document.querySelector("#lista-receitas ul");


const botaoSair = document.querySelector('.logout');

// ==========================================================
// 2. FUNÇÕES PARA BUSCAR DADOS DO BACKEND (API CALLS)
// ==========================================================


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

// --- CARREGA OS DADOS DO DASHBOARD ---
async function carregarDashboard(){

    const idMedicoLogado = sessionStorage.getItem('idMedicoLogado');

    if(!idMedicoLogado){
    cardConsultas.textContent = 'Erro de id';
    return;
    }

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

    cardReceitas.textContent = 'N/A';
    cardGanhos.textContent = 'N/A';

}

// --- CARREGA A AGENDA DO DIA ---
async function carregarAgenda() {
  tabelaAgenda.innerHTML = `<tr><td colspan="4">Carregando agenda...</td></tr>`;
  const medicoId = 1;

  try {
    const response = await fetch(`http://localhost:8080/api/consultas/medico/${medicoId}/hoje`);
    if (!response.ok) throw new Error('Erro ao buscar consultas.');
    const consultas = await response.json();
    
    tabelaAgenda.innerHTML = '';
    if (consultas.length === 0) {
      tabelaAgenda.innerHTML = `<tr><td colspan="4">Nenhuma consulta para hoje.</td></tr>`;
      return;
    }
    consultas.forEach(consulta => {
      const linha = tabelaAgenda.insertRow();
      const horaFormatada = new Date(consulta.dataHora).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
      linha.insertCell(0).textContent = horaFormatada;
      linha.insertCell(1).textContent = consulta.paciente.nome;
      linha.insertCell(2).textContent = consulta.statusMotivo.replace('_', ' ');
      
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

          item.textContent = `Paciente: ${receita.paciente.nome} - ${nomeMedicamento}`;
          listaReceitas.appendChild(item);
      });
  } catch (error) {
      console.error("Erro ao carregar receitas:", error);
      listaReceitas.innerHTML = '<li>Erro ao carregar receitas.</li>';
  }
}

async function buscarPacientePorCpf(cpf) {
    if (cpf.length !== 11) return;

    inputPacienteNome.value = 'Buscando...';
    inputPacienteId.value = '';

    try {
        const response = await fetch(`http://localhost:8080/api/pacientes/cpf/${cpf}`);

        if (response.ok) {
            const paciente = await response.json();
            inputPacienteNome.value = paciente.nome;
            inputPacienteId.value = paciente.id;
            return paciente;
        } else {
            inputPacienteNome.value = 'Paciente não encontrado';
            alert('CPF não encontrado no sistema.');
            return null;
        }
    } catch (error) {
        console.error("Erro ao buscar paciente:", error);
        inputPacienteNome.value = 'Erro de comunicação';
        return null;
    }
}

// ==========================================================
// 3. LÓGICA DE EVENTOS (Cliques, Envios de Formulário)
// ==========================================================

btnAddMedicamento?.addEventListener('click', () => {
    const nome = inputMedicamento.value;
    const dosagem = inputObservacoes.value;

    if (!nome || !dosagem) {
        alert('Por favor, preencha o nome do medicamento e a dosagem.');
        return;
    }

    // Adiciona ao array
    medicamentosDaReceitaAtual.push({ nome: nome, dosagem: dosagem });

    // Atualiza a UI da direita
    renderMedicamentosAtuais();

    // Limpa os campos para o próximo
    inputMedicamento.value = '';
    inputObservacoes.value = '';
    inputMedicamento.focus();
});

// Evento para os botões "Remover" (usando delegação de evento)
listaMedicamentosAtualUI?.addEventListener('click', (e) => {
    if (e.target.classList.contains('btn-remover-med')) {
        const indexParaRemover = parseInt(e.target.dataset.index);

        // Remove o item do array
        medicamentosDaReceitaAtual.splice(indexParaRemover, 1);

        // Atualiza a UI
        renderMedicamentosAtuais();
    }
});

// --- CONTROLA A NAVEGAÇÃO ENTRE AS TELAS E CHAMA A FUNÇÃO DE CARREGAMENTO CORRETA ---
function mostrarTela(id) {
  document.querySelectorAll(".conteudo").forEach(t => t.classList.remove("ativo"));
  const novaTela = document.getElementById(id);
  novaTela.classList.add("ativo");
  tituloPagina.textContent = id.charAt(0).toUpperCase() + id.slice(1);

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
    // Adicione casos para 'ficha' e 'financeiro' quando os endpoints existirem
  }
}

// --- ENVIO DO FORMULÁRIO DE NOVA RECEITA ---
formReceita?.addEventListener("submit", async (e) => {
  e.preventDefault();

const idPaciente = inputPacienteId.value;
  const idMedico = sessionStorage.getItem('idMedicoLogado');

  // --- Validações ---
  if (!idPaciente) {
        alert("Por favor, busque um paciente pelo CPF primeiro.");
        return;
  }
  if (!idMedico) {
        alert("Erro crítico: Médico não logado.");
        return;
  }
  // Validação mais importante:
  if (medicamentosDaReceitaAtual.length === 0) {
        alert("Você precisa adicionar pelo menos um medicamento à receita antes de gerá-la.");
        return;
  }

try {
    // 1. O objeto `novaReceita` agora usa o ARRAY
    const novaReceita = {
        id_paciente: parseInt(idPaciente),
        id_medico: parseInt(idMedico),
        medicamentos: medicamentosDaReceitaAtual // <-- MUDANÇA PRINCIPAL
    };

    // 2. Enviar os dados para o backend (O endpoint está correto)
    const response = await fetch('http://localhost:8080/api/receitas', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(novaReceita)
    });

    if (!response.ok) {
      throw new Error('Falha ao salvar. O servidor respondeu com um erro.');
    }

    // 5. Se deu tudo certo:
    alert("Receita gerada e salva com sucesso!");

    // Limpa o formulário e o array
    formReceita.reset();
    inputPacienteId.value = '';
    inputPacienteNome.value = '';
    medicamentosDaReceitaAtual = []; // Limpa o array
    renderMedicamentosAtuais(); // Limpa a UI da direita

    carregarReceitas(); // Atualiza a lista de "Receitas Emitidas"

  } catch (error) {
    // 6. Se deu algum erro:
    console.error("Erro ao gerar receita:", error);
    alert("Não foi possível salvar a receita. Verifique o console.");
  }
});


// --- ATUALIZAÇÃO DE STATUS DA CONSULTA ---
tabelaAgenda.addEventListener('change', (event) => {
  if (event.target.classList.contains('status-select')) {
    const select = event.target;
    const consultaId = select.dataset.consultaId;
    const novoStatus = select.value;
    atualizarStatusConsulta(consultaId, novoStatus);
  }
});

botaoSair?.addEventListener('click', () => {
  alert('Você foi desconectado.');
  window.location.href = 'login.html';
});

inputCpf?.addEventListener('blur', (e) => {
    const cpf = e.target.value.replace(/[^0-9]/g, '');
    if (cpf.length === 11) { // Só busca se tiver 11 dígitos
        buscarPacientePorCpf(cpf);
    }
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

const elementoBoasVindas = document.getElementById('boas-vindas');
  if (elementoBoasVindas) {
      elementoBoasVindas.textContent = `Bem-vindo(a), Dr(a). ${nomeMedicoLogado}`;
  } else {
      console.warn("Elemento 'boas-vindas' não encontrado no HTML.");
  }

  mostrarTela('dashboard');
});