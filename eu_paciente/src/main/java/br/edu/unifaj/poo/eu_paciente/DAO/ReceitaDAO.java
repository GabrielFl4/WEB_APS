package br.edu.unifaj.poo.eu_paciente.DAO;


import br.edu.unifaj.poo.eu_paciente.Model.Medicamento;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReceitaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int contarReceitasPorDia(Long idMedico, LocalDate data) {
        String querySQL = "SELECT COUNT(*) FROM receita WHERE id_medico = ? AND data = ?;";

        try {
            Integer count = jdbcTemplate.queryForObject(
                    querySQL,
                    new Object[]{idMedico, java.sql.Date.valueOf(data)},
                    Integer.class
            );

            return (count != null) ? count : 0;

        } catch (Exception e) {
            System.err.println("Erro ao contar receitas: " + e.getMessage());
            return 0;
        }
    }

    public Long adicionarReceita(Receita receita) throws Exception {

        String insertSQL = "INSERT INTO receita (data, id_paciente, id_medico) VALUES (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                ps.setDate(1, Date.valueOf(receita.getData()));
                ps.setInt(2, receita.getId_paciente());
                ps.setInt(3, receita.getId_medico());
                return ps;
            }, keyHolder);

            // Retorna o ID que o banco acabou de gerar
            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            } else {
                throw new Exception("Falha ao obter o ID da receita gerada.");
            }
        } catch (Exception e) {
            throw new Exception("Erro ao adicionar receita: " + e.getMessage(), e);
        }
    }

    public void adicionarMedicamento(Medicamento medicamento) throws Exception {

        String insertSQL = "INSERT INTO medicamento (nome, dosagem, id_receita) VALUES (?, ?, ?);";

        try {
            // REMOVIDA a conexão manual e usado o update direto.
            // Isso garante que ele participe da transação (@Transaction) do ReceitaService.
            jdbcTemplate.update(insertSQL,
                    medicamento.getNome(),
                    medicamento.getDosagem(),
                    medicamento.getId_receita()
            );
        } catch (Exception e) {
            throw new Exception("Erro ao adicionar medicamento: " + e.getMessage(), e);
        }
    }

    public List<Receita> receitasPorMedico(Long id_medico) throws Exception {
        String querySQL = "SELECT receita.id, receita.data, receita.id_paciente, medico.id AS id_medico, medico.nome AS nome_medico, medico.especialidade, paciente.nome AS nome_paciente " +
                "FROM receita " +
                "LEFT JOIN medico ON receita.id_medico = medico.id " +
                "LEFT JOIN paciente ON receita.id_paciente = paciente.id " +
                "WHERE receita.id_medico = ? " +
                "ORDER BY receita.data DESC;";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(querySQL)) {

            ps.setLong(1, id_medico);
            List<Receita> receitas = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Receita r = getReceitaDoMedico(rs);
                    receitas.add(r);
                }
            }
            return receitas;
        }
    }

    public List<Receita> receitaPorPaciente(Long id_paciente) throws Exception {
        String querySQL = "SELECT receita.id, receita.data, receita.id_paciente, medico.id AS id_medico, medico.nome, medico.especialidade, paciente.id AS id_paciente " +
                "FROM receita " +
                "LEFT JOIN medico ON receita.id_medico = medico.id " +
                "LEFT JOIN paciente ON receita.id_paciente = paciente.id " +
                "WHERE id_paciente = ? " +
                "ORDER BY receita.data;";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(querySQL)) {
            ps.setLong(1, id_paciente);
            List<Receita> receitas = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Receita r = getReceita(rs);
                    receitas.add(r);
                }
            }
            return receitas;
        }
    }


    public List<Medicamento> medicamentosPorReceita(Long id_receita) throws Exception {
        String querySQL = "SELECT medicamento.id, nome, dosagem, id_receita " +
                "FROM medicamento " +
                "LEFT JOIN receita ON medicamento.id_receita = receita.id " +
                "WHERE receita.id = ?;";

        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(querySQL)) {
            ps.setLong(1, id_receita);
            List<Medicamento> medicamentos = new ArrayList<>();
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    Medicamento m = getMedicamentos(rs);
                    medicamentos.add(m);
                }
            }
            return medicamentos;
        }
    }

    private static Receita getReceita(ResultSet rs) throws Exception{
        Receita r = new Receita();
        r.setId((long) rs.getInt("id"));
        r.setData(rs.getDate("data").toLocalDate());
        r.setMedico(new Medico(rs.getString("nome"), rs.getString("especialidade")));
        r.setId_paciente(rs.getInt("id_paciente"));
        r.setId_medico(rs.getInt("id_medico"));
    return r;
    }

    private static Receita getReceitaDoMedico(ResultSet rs) throws Exception{
        Receita r = new Receita();
        r.setId((long) rs.getInt("id"));
        r.setData(rs.getDate("data").toLocalDate());
        r.setId_paciente(rs.getInt("id_paciente"));
        r.setId_medico(rs.getInt("id_medico"));

        if (rs.getString("nome_paciente") != null) {
            r.setPaciente(new Paciente(rs.getString("nome_paciente")));
        }

        return r;
    }

    private static Medicamento getMedicamentos(ResultSet rs) throws Exception{
        Medicamento m = new Medicamento();
        m.setId((long) rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setDosagem(rs.getString("dosagem"));
        m.setId_receita(rs.getInt("id_receita"));
        return m;
    }
}

