package br.edu.unifaj.poo.eu_paciente.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Paciente {

    private Long id;

    @NotBlank(message = "O preenchimento do Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "O preenchimento da Idade é obrigatório!")
    private String idade;

    @NotBlank(message = "O preenchimento do Email é obrigatório!")
    private String email;

    @NotBlank(message = "O preenchimento da Senha é obrigatório!")
    private String senha;

    private List<Receita> receitas;
    private List<Consulta> consultas;

    public Paciente(){
    }

    public Paciente(Long id, String nome, String idade, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.email = email;
        this.senha = senha;
    }
}
