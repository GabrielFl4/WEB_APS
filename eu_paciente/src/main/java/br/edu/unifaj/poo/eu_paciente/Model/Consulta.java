package br.edu.unifaj.poo.eu_paciente.Model;

import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class Consulta {

    private Long id;
    private LocalDateTime dataHora;
    private String sintomas;
    private StatusAndamentoConsulta statusAndamento;
    private StatusMotivoConsulta statusMotivo;
    private BigDecimal valor;
    private boolean consultaPaga;

    private Paciente paciente;
    private Medico medico;


    public Consulta(){
    }

    public Consulta(boolean consultaPaga, BigDecimal valor, String sintomas, LocalDateTime dataHora, Long id, StatusAndamentoConsulta statusAndamento, StatusMotivoConsulta statusMotivo, Paciente paciente, Medico medico) {
        this.consultaPaga = consultaPaga;
        this.valor = valor;
        this.sintomas = sintomas;
        this.dataHora = dataHora;
        this.id = id;
        this.statusAndamento = statusAndamento;
        this.statusMotivo = statusMotivo;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Consulta(LocalDateTime dataHora, StatusAndamentoConsulta statusAndamento, StatusMotivoConsulta statusMotivo, Paciente paciente) {
        this.dataHora = dataHora;
        this.statusAndamento = statusAndamento;
        this.statusMotivo = statusMotivo;
        this.paciente = paciente;
    }
}




