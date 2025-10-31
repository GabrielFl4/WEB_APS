package br.edu.unifaj.poo.eu_paciente.DAO;

import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PacienteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Paciente> selectPacientes() throws Exception {
        String querySql = "select id, nome, email, senha, cpf, data_nasc, telefone, complemento from databasebonitinho.Paciente";

        try (Connection con = jdbcTemplate.getDataSource().getConnection()) {
            //Comandos JDBC
            PreparedStatement ps = con.prepareStatement(querySql);
            {
                List<Paciente> pacientes = new ArrayList<>();

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Paciente p = getPaciente(rs);
                        pacientes.add((p));
                    }
                }
                return pacientes;
            }
        }
    }

    private static Paciente getPaciente(ResultSet rs) throws Exception {
        Paciente paciente = new Paciente();
        paciente.setId((long) rs.getInt("id"));
        paciente.setNome(rs.getString("nome"));
        paciente.setEmail(rs.getString("email"));
        paciente.setSenha(rs.getString("senha"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setData_nasc(rs.getString("data_nasc"));
        paciente.setTelefone(rs.getString("telefone"));
        paciente.setComplemento(rs.getString("complemento"));
        return paciente;
    }



}

