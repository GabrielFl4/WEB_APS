package br.edu.unifaj.poo.eu_paciente.DAO;

import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicoDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Medico> selectMedicos() throws Exception{
        String querySQL = "SELECT * " +
                "FROM medico;";

        try (Connection con = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement(querySQL);
            {
                List<Medico> medicos = new ArrayList<>();

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Medico med = getMedico(rs);
                        medicos.add(med);
                    }
                }
                return medicos;
            }
        }
    }

    private static Medico getMedico(ResultSet rs) throws Exception{
            Medico m = new Medico();
            m.setId(rs.getLong("id"));
            m.setNome(rs.getString("nome"));
            m.setEmail(rs.getString("email"));
            m.setSenha(rs.getString("senha"));
            m.setEspecialidade(rs.getString("especialidade"));
            return m;
    }
}
