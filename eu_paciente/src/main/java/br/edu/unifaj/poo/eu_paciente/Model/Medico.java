package br.edu.unifaj.poo.eu_paciente.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medico {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String especialidade;

    public Medico(){
    }

    public Medico(String nome, String especialidade){
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public Medico(Long id, String nome, String email, String senha, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.especialidade = especialidade;
    }


}
