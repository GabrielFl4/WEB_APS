package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DAO.PacienteDAO;
import br.edu.unifaj.poo.eu_paciente.DTO.PacienteRequest;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    PacienteDAO dao;

    public List<Paciente> listaDePacientes = new ArrayList<>();

    public Optional<Paciente> loginPaciente(String email, String senha) throws Exception{
        listaDePacientes = dao.selectPacientes();

        if (email == null){
            email = "";
            System.out.println("ANDROID - Email vazio.");
        } else {
            email = email.trim().toLowerCase();
        }

        if (senha == null){
            senha = "";
            System.out.println("ANDROID - Senha vazia.");
        } else {
            senha = senha.trim();
        }


        for (Paciente p : listaDePacientes){
            if(p.getEmail().equals(email) && p.getSenha().equals(senha)){
                System.out.println("ANDROID - Usuário '" + email + "' autenticado com sucesso!");
                return Optional.of(p);
            } else if (p.getEmail().equals(email)) {
                System.out.println("ANDROID - A senha '" + senha + "' não corresponde ao email: " + email);
            }
        }
        return Optional.empty();
    }

    // Função do Igor
    public Paciente BuscarCpf(String cpf) throws Exception {
        listaDePacientes = dao.selectPacientes();
        Optional<Paciente> paciente = listaDePacientes.stream()
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst();

        return paciente.orElse(null);
    }

    public Paciente buscarPorId(Long id) throws Exception {
        listaDePacientes = dao.selectPacientes();
        return listaDePacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
