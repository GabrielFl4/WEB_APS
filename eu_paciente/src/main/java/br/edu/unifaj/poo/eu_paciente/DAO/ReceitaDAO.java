package br.edu.unifaj.poo.eu_paciente.DAO;


import br.edu.unifaj.poo.eu_paciente.Model.Medicamento;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReceitaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    private static Medicamento getMedicamentos(ResultSet rs) throws Exception{
        Medicamento m = new Medicamento();
        m.setId((long) rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setDosagem(rs.getString("dosagem"));
        m.setId_receita(rs.getInt("id_receita"));
        return m;
    }
}

