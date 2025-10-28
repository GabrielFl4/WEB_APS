package br.edu.unifaj.poo.eu_paciente.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Receita {

    private Long idReceita;

    private Paciente paciente;

    private String nomeMedicamento;
    private String descricaoUsoMedicamento;

    public Receita(){
    }

    public Receita(Long idReceita, Paciente paciente, String nomeMedicamento, String descricaoUsoMedicamento) {
        this.idReceita = idReceita;
        this.paciente = paciente;
        this.nomeMedicamento = nomeMedicamento;
        this.descricaoUsoMedicamento = descricaoUsoMedicamento;
    }

    public Receita(Paciente paciente, String nomeMedicamento, String descricaoUsoMedicamento) {
        this.paciente = paciente;
        this.nomeMedicamento = nomeMedicamento;
        this.descricaoUsoMedicamento = descricaoUsoMedicamento;
    }
}
