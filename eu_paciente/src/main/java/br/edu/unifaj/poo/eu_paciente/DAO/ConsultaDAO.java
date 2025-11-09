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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConsultaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int contarConsultasPorDia(Long idMedico, LocalDate data) {
        String querySQL = "SELECT COUNT(*) FROM consulta WHERE id_medico = ? AND CAST(data AS DATE) = ?";

        try {
            // queryForObject é ideal para quando você espera um único valor de retorno
            Integer count = jdbcTemplate.queryForObject(
                    querySQL,
                    new Object[]{idMedico, java.sql.Date.valueOf(data)}, // Argumentos da query
                    Integer.class // O tipo que esperamos de volta
            );

            return (count != null) ? count : 0;

        } catch (Exception e) {
            System.err.println("Erro ao contar consultas: " + e.getMessage());
            return 0;
        }
    }

    public Consulta adicionarConsulta(Consulta consulta) throws Exception{
        String insertSQL = "INSERT INTO consulta (data, valor, pago, rotina, sintomas, status, id_paciente, id_medico) VALUES " +
                "(?, ?, ?, ?, ?, 'PENDENTE', ?, ?);";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(insertSQL)) {
            ps.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            ps.setBigDecimal(2, consulta.getValor());
            ps.setBoolean(3, consulta.isConsultaPaga());
            ps.setString(4, consulta.getStatusMotivo().toString());
            ps.setString(5, consulta.getSintomas());
            ps.setInt(6, consulta.getId_paciente());
            ps.setInt(7, consulta.getId_medico());
            ps.executeUpdate();
        }
        return consulta;
    }


    public List<Consulta> selectConsultasPorPaciente(Long idPaciente) throws Exception {
        String querySQL = "SELECT consulta.id, consulta.data, consulta.status, consulta.sintomas, consulta.pago, consulta.valor, medico.nome, medico.especialidade " +
                "FROM paciente " +
                "LEFT JOIN consulta ON consulta.id_paciente = paciente.id " +
                "LEFT JOIN medico ON medico.id = consulta.id_medico " +
                "WHERE paciente.id = ? " +
                "ORDER BY data;";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             //Comandos JDBC
             PreparedStatement ps = con.prepareStatement(querySQL)) {
            ps.setLong(1, idPaciente);
            List<Consulta> consultas = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consulta c = getConsultaPaciente(rs);
                    consultas.add((c));
                }
            }
            return consultas;
        }
    }

    public List<Consulta> selectConsultasPorDia(Long idMedico) throws Exception{
        String querySQL = "SELECT consulta.id, consulta.data, consulta.pago, consulta.status, consulta.rotina, paciente.nome, paciente.cpf " +
                "FROM paciente " +
                "LEFT JOIN consulta ON consulta.id_paciente = paciente.id " +
                "LEFT JOIN medico ON medico.id = consulta.id_medico " +
                "WHERE medico.id = ? " +
                "ORDER BY data;";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(querySQL)) {
                 ps.setLong(1, idMedico);
                 List<Consulta> consultasDia = new ArrayList<>();
                 try(ResultSet rs = ps.executeQuery()) {
                     while(rs.next()){
                         Consulta c = getConsultaDoDia(rs);
                         consultasDia.add(c);
                     }
                 }
                 return consultasDia;
        }
    }

    public Consulta atualizarPagaConsulta(int idConsulta, boolean pago) throws Exception{
        String updateSQL = "UPDATE consulta " +
                "SET pago = ? " + // TRUE ou FALSE
                "WHERE id = ?;";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(updateSQL)) {
            ps.setBoolean(1, pago);
            ps.setInt(2, idConsulta);
            int res = ps.executeUpdate();
            if (res == 1) {
                String querySql = "SELECT consulta.id, consulta.data, consulta.status, consulta.pago, consulta.valor, medico.nome, medico.especialidade " +
                        "FROM consulta " +
                        "LEFT JOIN medico ON medico.id = consulta.id_medico " +
                        "WHERE consulta.id = ?;";
                PreparedStatement ps1 = con.prepareStatement(querySql);{
                    ps1.setInt(1, idConsulta);
                    try (ResultSet rs = ps1.executeQuery()) {
                        rs.next();
                        return getConsultaPaga(rs);
                    }
                }
            }
            throw new Exception("Erro no update");
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
                String querySql = "SELECT consulta.id, consulta.data, consulta.status, consulta.pago, consulta.sintomas, consulta.valor, medico.nome, medico.especialidade " +
                        "FROM consulta " +
                        "LEFT JOIN medico ON medico.id = consulta.id_medico " +
                        "WHERE consulta.id = ?;";
                PreparedStatement ps1 = con.prepareStatement(querySql);{
                    ps1.setInt(1, id_Consulta);
                    try (ResultSet rs = ps1.executeQuery()) {
                        rs.next();
                        return getConsultaPaciente(rs);
                    }
                }
            }
            throw new Exception("Erro no update");
        }
    }

    private static Consulta getConsultaPaciente(ResultSet rs) throws Exception {
        Consulta c = new Consulta();
        c.setId((long) rs.getInt("id"));
        c.setDataHora(rs.getTimestamp("data").toLocalDateTime());
        c.setStatusAndamento(StatusAndamentoConsulta.valueOf(rs.getString("status")));
        c.setSintomas(rs.getString("sintomas"));
        c.setConsultaPaga(rs.getBoolean("pago"));
        c.setValor(rs.getBigDecimal("valor"));
        c.setMedico(new Medico(rs.getString("nome"), rs.getString("especialidade")));
        return c;
    }

    private static Consulta getConsultaPaga(ResultSet rs) throws Exception{
        Consulta c = new Consulta();
        c.setId((long) rs.getInt("id"));
        c.setDataHora(rs.getTimestamp("data").toLocalDateTime());
        c.setStatusAndamento(StatusAndamentoConsulta.valueOf(rs.getString("status")));
        c.setConsultaPaga(rs.getBoolean("pago"));
        c.setValor(rs.getBigDecimal("valor"));
        c.setMedico(new Medico(rs.getString("nome"), rs.getString("especialidade")));
        return c;
    }

    private static Consulta getConsultaDoDia(ResultSet rs) throws Exception{
        Consulta c = new Consulta();
        c.setId((long) rs.getInt("id"));
        c.setDataHora(rs.getTimestamp("data").toLocalDateTime());
        c.setConsultaPaga(rs.getBoolean("pago"));
        c.setStatusAndamento(StatusAndamentoConsulta.valueOf(rs.getString("status")));
        c.setStatusMotivo(StatusMotivoConsulta.valueOf(rs.getString("rotina")));
        c.setPaciente(new Paciente(rs.getString("nome"), rs.getString("cpf")));
        return c;
    }
}
