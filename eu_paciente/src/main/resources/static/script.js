function mostrarTela(id) {
  const telas = document.querySelectorAll(".conteudo");
  telas.forEach(t => t.classList.remove("ativo"));

  const titulo = document.getElementById("titulo-pagina");
  const novaTela = document.getElementById(id);
  novaTela.classList.add("ativo");

  // Atualiza título
  titulo.textContent =
    id.charAt(0).toUpperCase() + id.slice(1).replace("-", " ");
}

// Simulação de criação de receita
const form = document.getElementById("form-receita");
const lista = document.querySelector("#lista-receitas ul");

form?.addEventListener("submit", (e) => {
  e.preventDefault();
  const paciente = form.querySelector("input[placeholder='Nome do paciente']").value;
  const medicamento = form.querySelector("input[placeholder='Nome do medicamento']").value;

  const item = document.createElement("li");
  item.textContent = `${paciente} - ${medicamento}`;
  lista.appendChild(item);

  form.reset();
  alert("Receita gerada com sucesso!");
});


