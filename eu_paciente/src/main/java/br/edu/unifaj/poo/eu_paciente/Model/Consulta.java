package br.edu.unifaj.poo.eu_paciente.Model;

import br.edu.unifaj.poo.eu_paciente.Enum.StatusAndamentoConsulta;
import br.edu.unifaj.poo.eu_paciente.Enum.StatusMotivoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Table("consulta")
public class Consulta {

    @Id
    private Long id;

    @Column("data")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;

    private String sintomas;

    @Column("status")
    private StatusAndamentoConsulta statusAndamento;

    @Column("rotina")
    private StatusMotivoConsulta statusMotivo;

    private BigDecimal valor;

    @Column("pago")
    private boolean consultaPaga;

    @Column("id_paciente")
    private int id_paciente;

    @Column("id_medico")
    private int id_medico;

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




