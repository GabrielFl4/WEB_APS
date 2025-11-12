package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DAO.ConsultaDAO;
import br.edu.unifaj.poo.eu_paciente.DAO.PacienteDAO;
import br.edu.unifaj.poo.eu_paciente.DAO.ReceitaDAO;
import br.edu.unifaj.poo.eu_paciente.DTO.PacienteRequest;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    PacienteDAO dao;

    @Autowired
    ConsultaDAO consultaDAO;

    @Autowired
    ReceitaDAO receitaDAO;

    public List<Paciente> listaDePacientes = new ArrayList<>();

    public Optional<Paciente> loginPaciente(String email, String senha) throws Exception{
        listaDePacientes = dao.selectPacientes();

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

    public int atualizarPaciente(Long id, String tel, String comp) throws Exception {
        return dao.atualizarPaciente(id, tel, comp);
    }

    public Paciente buscarPacienteId(Long idUsuario) throws Exception {
        Paciente p = dao.selectPacienteId(idUsuario);
        return p;
    }

    // Função do Igor
    public Paciente BuscarCpf(String cpf) throws Exception {
        return dao.findByCpf(cpf);
    }

    public Paciente buscarPorId(Long id) throws Exception {
        return dao.selectPacienteId(id);
    }

    public Paciente buscarFichaCompleta(Long idPaciente) throws Exception {

        Paciente paciente = dao.selectPacienteId(idPaciente);
        if (paciente == null) {
            throw new RuntimeException("Paciente não encontrado com ID: " + idPaciente);
        }

        List<Consulta> todasConsultas = consultaDAO.selectConsultasPorPaciente(idPaciente);
        LocalDateTime agora = LocalDateTime.now();

        List<Consulta> historicoConsultas = todasConsultas.stream()
                .filter(consulta ->
                        consulta.getDataHora().isBefore(agora) ||
                                consulta.getStatusAndamento() == br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta.REALIZADA
                )
                .collect(Collectors.toList());

        paciente.setConsultas(historicoConsultas);


        List<Receita> receitas = receitaDAO.receitaPorPaciente(idPaciente);

        for (Receita r : receitas) {

            r.setMedicamentos(receitaDAO.medicamentosPorReceita(r.getId()));
        }
        paciente.setReceitas(receitas);

        return paciente;
    }
}
