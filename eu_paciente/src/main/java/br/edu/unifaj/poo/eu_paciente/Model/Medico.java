package br.edu.unifaj.poo.eu_paciente.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("medico")
public class Medico {
    @Id
    private Long id;

    @Column("nome")
    private String nome;

    @Column("email")
    private String email;

    @Column("senha")
    private String senha;

    @Column("especialidade")
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
