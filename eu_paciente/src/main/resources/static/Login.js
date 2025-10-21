// Pega os elementos do formulário
const loginForm = document.getElementById('login-form');
const emailInput = document.getElementById('email');
const senhaInput = document.getElementById('senha');
const errorMessage = document.getElementById('error-message');

loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const email = emailInput.value;
  const senha = senhaInput.value;

  errorMessage.textContent = '';

  // Cria o objeto de dados para enviar ao backend
  const loginData = {
    email: email,
    senha: senha
  };

  try {
    // Faz a chamada para a API
    const response = await fetch('http://localhost:8080/api/medicos/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginData)
    });

    // Verifica se a resposta foi bem-sucedida
    if (response.ok) {
      // SUCESSO! Redireciona o usuário para a página principal
      alert('Login bem-sucedido!');
      window.location.href = 'index.html';
    } else {
      // ERRO! A resposta não foi OK
      const errorText = await response.text();
      errorMessage.textContent = errorText;
    }
  } catch (error) {
    // ERRO DE CONEXÃO!
    console.error('Erro de conexão:', error);
    errorMessage.textContent = 'Não foi possível conectar ao servidor. Tente novamente.';
  }
});