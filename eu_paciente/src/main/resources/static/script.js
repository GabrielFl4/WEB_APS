function mostrarTela(id) {
  const telas = document.querySelectorAll(".conteudo");
  telas.forEach(t => t.classList.remove("ativo"));
  const titulo = document.getElementById("titulo-pagina");
  const novaTela = document.getElementById(id);
  novaTela.classList.add("ativo");
  titulo.textContent =
    id.charAt(0).toUpperCase() + id.slice(1).replace("-", " ");
}

const form = document.getElementById("form-receita");
const lista = document.querySelector("#lista-receitas ul");

function carregarReceitas() {
  lista.innerHTML = '<li>Carregando...</li>';

  fetch('http://localhost:8080/api/receitas')
    .then(response => response.json())
    .then(dados => {
      lista.innerHTML = '';
      dados.forEach(receita => {
        const item = document.createElement("li");
        item.textContent = `${receita.nomePaciente} - ${receita.nomeMedicamento}`;
        lista.appendChild(item);
      });
    })
    .catch(error => {
        console.error("Erro ao carregar receitas:", error);
        lista.innerHTML = '<li>Erro ao carregar receitas.</li>';
    });
}

form?.addEventListener("submit", (e) => {
  e.preventDefault();
  const paciente = form.querySelector("input[placeholder='Nome do paciente']").value;
  const medicamento = form.querySelector("input[placeholder='Nome do medicamento']").value;

  const dados = {
    nomePaciente: paciente,
    nomeMedicamento: medicamento,
    descricaoUsoMedicamento: form.querySelector("textarea").value
  };

  fetch('http://localhost:8080/api/receitas', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dados)
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Falha ao salvar. O servidor respondeu com um erro.');
    }
    carregarReceitas();
    form.reset();
    alert("Receita gerada e salva com sucesso!");
  })
  .catch(error => {
    console.error("Erro:", error);
    alert("Não foi possível salvar a receita. Verifique o console para mais detalhes.");
  });
});

// Chama a função para carregar as receitas assim que a página é aberta.
document.addEventListener('DOMContentLoaded', carregarReceitas);