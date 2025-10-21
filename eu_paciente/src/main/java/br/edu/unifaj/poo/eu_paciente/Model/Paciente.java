package br.edu.unifaj.poo.eu_paciente.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Paciente {

    private Long idPaciente;

    @NotBlank(message = "O preenchimento do Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "O preenchimento da Idade é obrigatório!")
    private String Idade;

    @NotBlank(message = "O preenchimento do Email é obrigatório!")
    private String email;

    @NotBlank(message = "O preenchimento da Senha é obrigatório!")
    private String senha;

    private List<Receita> receita;
    private List<Consulta> consulta;

    public Paciente(){
    }

    public Paciente(Long idPaciente, String nome, String idade, String email, String senha, List<Receita> receita, List<Consulta> consulta) {
        this.idPaciente = idPaciente;
        this.nome = nome;
        Idade = idade;
        this.email = email;
        this.senha = senha;
        this.receita = receita;
        this.consulta = consulta;
    }
}
