package br.edu.unifaj.poo.eu_paciente.DAO;

import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Service.PacienteService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PacienteDao {
    // Eu fiz no service, então aqui tá vazio...

    public Optional<Paciente> loginPaciente(String email, String senha){
        PacienteService service = new PacienteService();
        return service.loginPaciente(email, senha);
    }





}
