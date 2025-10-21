package br.edu.unifaj.poo.eu_paciente.Service;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
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

    public List<Consulta> listaDeConsultas = new ArrayList<>();
    public AtomicLong contadorId = new AtomicLong();

    public ConsultaService(){

        //Todo, HARDCODE GERADO POR IA, MOTIVO: OTIMIZAÇÃO DE TEMPO (PREGUIÇA)

        Medico medicoTeste = new Medico(1L, "Dr. João Silva", "joao.silva@email.com", "1234", "Cardiologista");
        Paciente paciente1 = new Paciente(1L, "Ana Souza", "32", "ana.souza@email.com", "senha1");
        Paciente paciente2 = new Paciente(2L, "Bruno Lima", "40", "bruno.lima@email.com", "senha2");

        listaDeConsultas.add(new Consulta(
                false,
                new BigDecimal("250.00"),
                "Paciente relata dores no peito.",
                LocalDateTime.now().withHour(9).withMinute(0),
                contadorId.incrementAndGet(),
                StatusAndamentoConsulta.CONFIRMADA,
                StatusMotivoConsulta.RETORNO,
                paciente1,
                medicoTeste
        ));

        listaDeConsultas.add(new Consulta(
                true,
                new BigDecimal("350.00"),
                "Check-up anual.",
                LocalDateTime.now().withHour(10).withMinute(30),
                contadorId.incrementAndGet(),
                StatusAndamentoConsulta.PENDENTE,
                StatusMotivoConsulta.CONSULTA_INICIAL,
                paciente2,
                medicoTeste
        ));
    }

    public List<Consulta> buscarConsultaPorDia(Long idMedico){
        LocalDate hoje = LocalDate.now();
        return listaDeConsultas.stream()
                .filter(consulta -> consulta.getMedico().getId().equals(idMedico))
                .filter(consulta -> consulta.getDataHora().toLocalDate().isEqual(hoje))
                .collect(Collectors.toList());
    }

    public Consulta atualizarStatusConsulta(Long idConsulta, StatusAndamentoConsulta novoStatus){
        for (Consulta consulta: listaDeConsultas){
            if (consulta.getId().equals(idConsulta)){
                consulta.setStatusAndamento(novoStatus);
                System.out.println("Status da consulta " + idConsulta + " atualizado para " + novoStatus);
                return consulta;
            }
        }
        return null;
    }


}
