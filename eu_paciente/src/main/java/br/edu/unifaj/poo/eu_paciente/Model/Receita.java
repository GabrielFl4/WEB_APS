package br.edu.unifaj.poo.eu_paciente.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Receita {

    private Long idReceita;

    @NotBlank(message = "O nome do paciente é obrigatório!")
    private String nomePaciente;
    //todo, Uma Receita precisa pertencer a um Paciente - Não apenas a seu nome!

    @NotBlank(message = "O preenchimento do campo é obrigatório!")
    private String nomeMedicamento;

    @NotBlank(message = "A especificação de uso do medicamento é obrigatória!")
    private String descricaoUsoMedicamento;

    public Receita(){
    }

    public Receita(Long idReceita, String nomePaciente, String nomeMedicamento, String descricaoUsoMedicamento) {
        this.nomePaciente = nomePaciente;
        this.nomeMedicamento = nomeMedicamento;
        this.descricaoUsoMedicamento = descricaoUsoMedicamento;
        this.idReceita = idReceita;
    }

}
