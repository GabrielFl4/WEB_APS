package br.edu.unifaj.poo.eu_paciente.Service;
import br.edu.unifaj.poo.eu_paciente.DAO.ConsultaDAO;
import br.edu.unifaj.poo.eu_paciente.DAO.MedicoDAO;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    ConsultaDAO dao;

    @Autowired
    MedicoDAO medicoDAO;

    public List<Consulta> listaDeConsultas = new ArrayList<>();
    public AtomicLong contadorId = new AtomicLong();

    public Consulta addConsulta(Consulta c) throws Exception {
        c.setValor(decidirValor(c.getStatusMotivo(), (long) c.getId_medico()));
        c.setConsultaPaga(seRetorno(c.getStatusMotivo()));
        c.setStatusAndamento(StatusAndamentoConsulta.PENDENTE);
        return dao.adicionarConsulta(c);
    }

    public int obterConsultaDeHoje(Long idMedico){
    LocalDate hoje = LocalDate.now();

    return dao.contarConsultasPorDia(idMedico, hoje);
    }

    public List<Consulta> buscarConsultaPorDia(Long idMedico) throws Exception {
        LocalDate hoje = LocalDate.now();
        listaDeConsultas = dao.selectConsultasPorDia(idMedico);
        return listaDeConsultas.stream()
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

    public Consulta atualizarPagoConsulta(Long idConsulta, boolean pago) throws Exception{
        return dao.atualizarPagaConsulta(idConsulta.intValue(), pago);
    }


    private BigDecimal decidirValor(StatusMotivoConsulta motivo, Long id_medico) throws Exception {
        BigDecimal valor = BigDecimal.ZERO;
        String especialidade = null;

        ArrayList<Medico> listaMedicos = medicoDAO.selectEspecialidades();
        for (Medico m : listaMedicos){
            if (Objects.equals(m.getId(), id_medico)) {
                especialidade = m.getEspecialidade().trim().toLowerCase();
            }
        }

        if (especialidade == null || especialidade.isBlank()) {
            especialidade = "";
            valor = valor.add(new BigDecimal("100.00"));
        }

        switch (especialidade){
            case "pediatra":       valor = valor.add(new BigDecimal("40.00")); break;
            case "urologista":     valor = valor.add(new BigDecimal("60.00")); break;
            case "clínico geral":  valor = valor.add(new BigDecimal("30.00")); break;
            case "neurologista":   valor = valor.add(new BigDecimal("100.00")); break;
            case "ortopedista":    valor = valor.add(new BigDecimal("130.00")); break;
            default:               valor = valor.add(new BigDecimal("80.00"));
        }

        switch (motivo) {
            case CONSULTA_INICIAL, RETORNO:  valor = valor.add(new BigDecimal("150.00")); break;
            case EXAMES:            valor = valor.add(new BigDecimal("80.00")); break;
            default:                valor = valor.add(new BigDecimal("100.00")); break;
        }
        return valor;
    }

    private boolean seRetorno(StatusMotivoConsulta motivo){
        String status = motivo.toString();
        return status.equals("RETORNO");
    }
}
