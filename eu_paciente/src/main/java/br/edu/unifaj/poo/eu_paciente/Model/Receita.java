package br.edu.unifaj.poo.eu_paciente.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Receita {

    private Long idReceita;

    @NotBlank(message = "O nome do paciente é obrigatório!")
    private String nomePaciente;

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

    public Long getIdReceita() {return idReceita;}

    public void setIdReceita(Long idReceita) {this.idReceita = idReceita;}

    public String getNomePaciente() {return nomePaciente;}

    public void setNomePaciente(String nomePaciente) {this.nomePaciente = nomePaciente;}

    public String getNomeMedicamento() {return nomeMedicamento;}

    public void setNomeMedicamento(String nomeMedicamento) {this.nomeMedicamento = nomeMedicamento;}

    public String getDescricaoUsoMedicamento() {return descricaoUsoMedicamento;}

    public void setDescricaoUsoMedicamento(String descricaoUsoMedicamento) {this.descricaoUsoMedicamento = descricaoUsoMedicamento;}
}
