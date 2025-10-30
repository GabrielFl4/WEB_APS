package br.edu.unifaj.poo.eu_paciente.DAO;

import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PacienteDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Paciente> selectPacientes() {
        String querySql = "select id, nome, email, senha, cpf, data_nasc, telefone, complemento from databasebonitinho.Paciente";


        try (Connection con = jdbcTemplate.getDataSource().getConnection()){
        //Comandos JDBC







        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Paciente>().stream().toList();
    }























    // Eu fiz no service, então aqui tá vazio...

    public Optional<Paciente> loginPaciente(String email, String senha){
        PacienteService service = new PacienteService();
        return service.loginPaciente(email, senha);
    }





}
