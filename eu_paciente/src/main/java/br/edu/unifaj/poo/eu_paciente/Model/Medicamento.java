package br.edu.unifaj.poo.eu_paciente.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Setter
@Getter
public class Medicamento {

    @Id
    private Long id;

    @Column("nome")
    private String nome;

    @Column("dosagem")
    private String dosagem;

    @Column("id_receita")
    private int id_receita;

    public Medicamento() {
    }

    public Medicamento(int id_receita, String dosagem, String nome, Long id) {
        this.id_receita = id_receita;
        this.dosagem = dosagem;
        this.nome = nome;
        this.id = id;
    }
}
