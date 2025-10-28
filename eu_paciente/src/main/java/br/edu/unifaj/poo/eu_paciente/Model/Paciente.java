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

    private String nome;

    private String idade;

    private String cpf;

    private String email;

    private String senha;

    private List<Receita> receitas;
    private List<Consulta> consultas;

    public Paciente(){
    }

    public Paciente(Long id, String nome, String idade, String cpf, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }
}
