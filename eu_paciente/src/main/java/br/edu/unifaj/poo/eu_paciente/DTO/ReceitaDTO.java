package br.edu.unifaj.poo.eu_paciente.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceitaDTO {
    private Long pacienteId;

    private String nomeMedicamento;
    private String descricaoUsoMedicamento;
}
