package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DTO.PacienteRequest;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    public List<Paciente> listaDePacientes = new ArrayList<>();


    // TODO -> Aqui vai ser substituido pela consulta do BD
    public PacienteService() {
        // Tudo hardcoded por culpa do Igão, valeu aí
        Paciente p1 = new Paciente(1L, "Gabriel Morandim Rodrigues", "20", "39328392833", "gabrielmorandim05@gmail.com", "NesseJogoTemSexo");
        Paciente p2 = new Paciente(2L, "Igor Cremasco Viotto", "20","48478291822", "email_do_igao@gmail.com", "123");
        Paciente p3 = new Paciente(3L, "jon", "19","31552744861", "Jaozin@gmail.com", "123");
        Paciente pdev = new Paciente(4L, "DEV", "0","99999999999", "1@gmail.com", "1");

        listaDePacientes.add(p1);
        listaDePacientes.add(p2);
        listaDePacientes.add(p3);
        listaDePacientes.add(pdev);
    }


    public Optional<Paciente> loginPaciente(String email, String senha){
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

    public Paciente BuscarCpf(String cpf){
        Optional<Paciente> paciente = listaDePacientes.stream()
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst();

        return paciente.orElse(null);
    }

    public Paciente buscarPorId(Long id) {
        return listaDePacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
