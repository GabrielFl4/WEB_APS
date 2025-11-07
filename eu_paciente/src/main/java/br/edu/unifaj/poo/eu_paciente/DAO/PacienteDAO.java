package br.edu.unifaj.poo.eu_paciente.DAO;

import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
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
        String querySql = "select id, nome, email, senha, cpf, data_nasc, telefone, complemento from databaseBonitinho.paciente";

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

    public Paciente selectPacienteId(Long idPaciente) throws Exception{
        String querySql = "select id, nome, email, senha, cpf, data_nasc, telefone, complemento " +
                "from paciente where id = ?";

        try (Connection con = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement(querySql);
            ps.setLong(1, idPaciente);
            {
                Paciente p = new Paciente();

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return getPaciente(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public int  atualizarPaciente(Long idPaciente, String telefone, String complemento) throws Exception {
        String updateSQL = "UPDATE paciente " +
                "SET telefone = ?, complemento = ? " +
                "WHERE id = ?";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(updateSQL)) {
            ps.setString(1, telefone);
            ps.setString(2, complemento);
            ps.setLong(3, idPaciente);
            return ps.executeUpdate();
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

