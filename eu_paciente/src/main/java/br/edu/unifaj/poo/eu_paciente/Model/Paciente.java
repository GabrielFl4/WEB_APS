package br.edu.unifaj.poo.eu_paciente.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Table("paciente")
public class Paciente {

    @Id
    private Long id;

    @Column("nome")
    private String nome;

    @Column("data_nasc")
    private String data_nasc;

    @Column("cpf")
    private String cpf;

    @Column("email")
    private String email;

    @Column("senha")
    private String senha;

    @Column("telefone")
    private String telefone;

    @Column("complemento")
    private String complemento;

    private List<Receita> receitas;
    private List<Consulta> consultas;

    public Paciente(){
    }

    public Paciente(String nome, String cpf){
        this.nome = nome;
        this.cpf = cpf;
    }

    public Paciente(Long id, String nome, String data_nasc, String cpf, String email, String senha, String telefone, String complemento) {
        this.id = id;
        this.nome = nome;
        this.data_nasc = data_nasc;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.complemento = complemento;
    }
}
