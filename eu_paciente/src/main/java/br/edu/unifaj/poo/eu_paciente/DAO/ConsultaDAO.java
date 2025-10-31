package br.edu.unifaj.poo.eu_paciente.DAO;

import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
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
public class ConsultaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Consulta> selectConsultasPorPaciente(Long idPaciente) throws Exception {
        String querySql = "SELECT consulta.id, consulta.data, consulta.status, consulta.sintomas, medico.nome, medico.especialidade " +
                "FROM paciente " +
                "LEFT JOIN consulta ON consulta.id_paciente = paciente.id " +
                "LEFT JOIN medico ON medico.id = consulta.id_medico " +
                "WHERE paciente.id = ?;";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             //Comandos JDBC
             PreparedStatement ps = con.prepareStatement(querySql)) {
            ps.setLong(1, idPaciente);
            List<Consulta> consultas = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consulta c = getConsulta(rs);
                    consultas.add((c));
                }
            }
            return consultas;
        }
    }

    public Consulta atualizarStatusConsulta(int id_Consulta, String status) throws Exception {
        String updateSQL = "UPDATE consulta " +
                "SET status = ? " +
                "WHERE id = ?;";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(updateSQL)) {
            ps.setString(1, status);
            ps.setInt(2, id_Consulta);
            int res = ps.executeUpdate();
            if (res == 1) {
                System.out.println("Status da consulta " + id_Consulta + " atualizado para " + status);
                String querySql = "SELECT consulta.id, consulta.data, consulta.status, consulta.sintomas, medico.nome, medico.especialidade " +
                        "FROM consulta " +
                        "LEFT JOIN medico ON medico.id = consulta.id_medico " +
                        "WHERE consulta.id = ?;";
                PreparedStatement ps1 = con.prepareStatement(querySql);{
                    ps1.setInt(1, id_Consulta);
                    try (ResultSet rs = ps1.executeQuery()) {
                        rs.next();
                        return getConsulta(rs);
                    }
                }
            }
            throw new Exception("Erro no update");
        }
    }

    private static Consulta getConsulta(ResultSet rs) throws Exception {
        Consulta c = new Consulta();
        c.setId((long) rs.getInt("id"));
        c.setDataHora(rs.getTimestamp("data").toLocalDateTime());
        c.setStatusAndamento(StatusAndamentoConsulta.valueOf(rs.getString("status")));
        c.setSintomas(rs.getString("sintomas"));
        c.setMedico(new Medico(rs.getString("nome"), rs.getString("especialidade")));
        return c;
    }
}
