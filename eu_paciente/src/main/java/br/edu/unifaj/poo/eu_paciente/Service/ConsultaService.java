package br.edu.unifaj.poo.eu_paciente.Service;
import br.edu.unifaj.poo.eu_paciente.DAO.ConsultaDAO;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import io.micrometer.common.util.internal.logging.InternalLogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    ConsultaDAO dao;

    public List<Consulta> listaDeConsultas = new ArrayList<>();
    public AtomicLong contadorId = new AtomicLong();

    public List<Consulta> buscarConsultaPorDia(Long idMedico){
        LocalDate hoje = LocalDate.now();
        return listaDeConsultas.stream()
                .filter(consulta -> consulta.getMedico().getId().equals(idMedico))
                .filter(consulta -> consulta.getDataHora().toLocalDate().isEqual(hoje))
                .collect(Collectors.toList());
    }

    public List<Consulta> exibirConsultas(Long idUsuario) throws Exception {
        listaDeConsultas = dao.selectConsultasPorPaciente(idUsuario);
        return listaDeConsultas;
    }

    public Consulta atualizarStatusConsulta(Long idConsulta, StatusAndamentoConsulta novoStatus) throws Exception {
        return dao.atualizarStatusConsulta(idConsulta.intValue(), novoStatus.toString());
    }
}
